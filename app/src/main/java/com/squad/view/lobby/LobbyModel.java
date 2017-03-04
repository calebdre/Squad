package com.squad.view.lobby;

import android.support.v4.util.Pair;

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
    private static final String LOBBY_USERS_REFERENCE_STRING = "/lobbies/%s/users";
    private static final String LOBBY_READY_REFERENCE_STRING = "/lobbies/%s/ready";
    private static final String USER_REFERENCE_STRING = "/users/%s";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference lobbyReference;
    private DatabaseReference lobbyReadyReference;
    private DatabaseReference lobbyUsersReference;

    LobbyModel(String id) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        lobbyReference = firebaseDatabase.getReference(String.format(LOBBY_REFERENCE_STRING, id));
        lobbyReadyReference = firebaseDatabase.getReference(String.format(LOBBY_READY_REFERENCE_STRING, id));
        lobbyUsersReference = firebaseDatabase.getReference(String.format(LOBBY_USERS_REFERENCE_STRING, id));
    }

    Observable<Boolean> onReady() {
        return RxFirebaseDatabase.observeValueEvent(lobbyReadyReference)
                .map((snapshot) -> snapshot.getValue() != null && (boolean) snapshot.getValue());
    }

    Observable<Triple<FacebookGraphResponse, RxFirebaseChildEvent.EventType, String>> onUserEvent() {
        return RxFirebaseDatabase.observeChildEvent(lobbyUsersReference)
                .map(dataSnapshot -> Triple.of(FacebookGraphResponse.create(dataSnapshot.getValue()), dataSnapshot.getEventType(), dataSnapshot.getKey()));
    }

    Observable<Pair<Lobby,FacebookGraphResponse>> getLobby() {
        return RxFirebaseDatabase.observeSingleValueEvent(lobbyReference)
                .map(Lobby::create)
                .flatMap((lobby) -> RxFirebaseDatabase.observeValueEvent(userReference(lobby.host()))
                        .map(FacebookGraphResponse::create)
                        .map(user -> new Pair<>(lobby, user)));
    }

    void startSquad() {
        lobbyReadyReference.setValue(true);
    }

    private DatabaseReference userReference(String host){
        return firebaseDatabase.getReference(String.format(USER_REFERENCE_STRING, host));
    }
}
