package com.squad.view.lobby;

import android.support.v4.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;

import org.apache.commons.lang3.tuple.Triple;

import rx.Observable;

public class LobbyModel {

    private static final String LOBBY_REFERENCE_STRING = "/lobbies/%s";
    private static final String LOBBY_USER_NUMBER_REFERENCE_STRING = "/lobbies/%s/numberOfUsers";
    private static final String LOBBY_USERS_REFERENCE_STRING = "/lobbies/%s/users";
    private static final String LOBBY_USER_REFERENCE = "/lobbies/%s/users/%s";
    private static final String LOBBY_READY_REFERENCE_STRING = "/lobbies/%s/ready";
    private static final String USER_REFERENCE_STRING = "/users/%s";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference lobbyReference;
    private DatabaseReference lobbyReadyReference;
    private DatabaseReference lobbyUsersReference;
    private DatabaseReference lobbyUserNumberReference;
    private DatabaseReference lobbyUserReference;
    private String lobbyId;
    private boolean shouldSkip = false;

    LobbyModel(String id) {
        this.lobbyId = id;
        firebaseDatabase = FirebaseDatabase.getInstance();
        lobbyReference = firebaseDatabase.getReference(String.format(LOBBY_REFERENCE_STRING, id));
        lobbyReadyReference = firebaseDatabase.getReference(String.format(LOBBY_READY_REFERENCE_STRING, id));
        lobbyUsersReference = firebaseDatabase.getReference(String.format(LOBBY_USERS_REFERENCE_STRING, id));
        lobbyUserNumberReference = firebaseDatabase.getReference(String.format(LOBBY_USER_NUMBER_REFERENCE_STRING, id));
    }

    Observable<Boolean> onReady() {
        return RxFirebaseDatabase.observeValueEvent(lobbyReadyReference)
                .map((snapshot) -> snapshot.getValue() != null && (boolean) snapshot.getValue());
    }

    Observable<Triple<FacebookGraphResponse, RxFirebaseChildEvent.EventType, String>> onUserEvent() {
        return RxFirebaseDatabase.observeChildEvent(lobbyUsersReference)
                .filter(snapshot -> !shouldSkip)
                .map(dataSnapshot -> Triple.of(FacebookGraphResponse.create(dataSnapshot.getValue()), dataSnapshot.getEventType(), dataSnapshot.getKey()));
    }

    Observable<Pair<Lobby,FacebookGraphResponse>> getLobby() {
        return RxFirebaseDatabase.observeSingleValueEvent(lobbyReference)
                .map((dataSnapshot) -> Lobby.create(dataSnapshot))
                .flatMap((lobby) -> RxFirebaseDatabase.observeValueEvent(userReference(lobby.host()))
                        .map(FacebookGraphResponse::create)
                        .map(user -> new Pair<>(lobby, user)));
    }

    void startSquad() {
        lobbyReadyReference.setValue(true);
    }

    void addUserToSquad(FacebookGraphResponse user) {
        shouldSkip = true;
        lobbyUsersReference.push().setValue(user.toFirebaseValue());
    }

    void removeUserFromSquad(FacebookGraphResponse user) {
        shouldSkip = true;
        RxFirebaseDatabase.observeSingleValueEvent(lobbyUsersReference)
                .subscribe(snapshot -> {
                   for (DataSnapshot userSnap : snapshot.getChildren()) {
                       if (FacebookGraphResponse.create(userSnap).fbId().equals(user.fbId())) {
                           String userKey = userSnap.getKey();
                           lobbyUserReference = firebaseDatabase.getReference(String.format(LOBBY_USER_REFERENCE, lobbyId, userKey));
                           lobbyUserReference.removeValue().addOnCompleteListener(a -> {
                               shouldSkip = false;
                           });
                           return;
                       };
                   }
                });
    }

    private DatabaseReference userReference(String host){
        return firebaseDatabase.getReference(String.format(USER_REFERENCE_STRING, host));
    }
}
