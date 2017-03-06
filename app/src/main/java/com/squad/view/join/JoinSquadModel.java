package com.squad.view.join;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.squad.model.Lobby;
import com.squad.util.FirebaseToListMapper;

import java.util.List;

import rx.Observable;

class JoinSquadModel {
    private static final String LOBBY_REFERENCE_STRING = "/lobbies";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference lobbyReference;

    JoinSquadModel() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        lobbyReference = firebaseDatabase.getReference(LOBBY_REFERENCE_STRING);
    }

    Observable<List<Lobby>> getLobbies() {
        return RxFirebaseDatabase.observeSingleValueEvent(lobbyReference, new FirebaseToListMapper<>(Lobby::create).map());
    }
}
