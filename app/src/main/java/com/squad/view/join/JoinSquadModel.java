package com.squad.view.join;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.squad.api.foursquare.FourSquareClient;
import com.squad.model.Lobby;
import com.squad.util.FirebaseToListMapper;

import java.util.List;

import rx.Observable;

public class JoinSquadModel {
    public static final String LOBBY_REFERENCE_STRING = "/lobbies";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference lobbyReference;

    public JoinSquadModel() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        lobbyReference = firebaseDatabase.getReference(LOBBY_REFERENCE_STRING);
    }

    public Observable<List<Lobby>> getLobbies() {
        return RxFirebaseDatabase.observeSingleValueEvent(lobbyReference, new FirebaseToListMapper<>(Lobby::create).map())
                .doOnNext(lobbies -> {
                    FourSquareClient client = new FourSquareClient();
                    for (Lobby lobby: lobbies) {
                        client.getImageUrl(lobby.location().id())
                                .subscribe();
                    }
                });
    }
}
