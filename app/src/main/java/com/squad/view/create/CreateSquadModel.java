package com.squad.view.create;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.model.MeetupLocation;

import java.util.Calendar;
import java.util.HashMap;

public class CreateSquadModel {

    public static final String LOBBY_REFERENCE_STRING = "/lobbies";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference lobbyReference;
    private FacebookGraphResponse user;

    public CreateSquadModel(FacebookGraphResponse user) {
        this.user = user;
        firebaseDatabase = FirebaseDatabase.getInstance();
        lobbyReference = firebaseDatabase.getReference(LOBBY_REFERENCE_STRING);
    }

    public Lobby createSquad(String activity, String squadName, MeetupLocation location){
        DatabaseReference push = lobbyReference.push();
        long timeStamp = Calendar.getInstance().getTimeInMillis();
        HashMap<String, FacebookGraphResponse> userMap = new HashMap<>();
        userMap.put(user.fbId(), user);

        Lobby lobby = Lobby.builder()
                .id(push.getKey())
                .name(squadName)
                .firebaseKey(push.getKey())
                .createdAt(timeStamp)
                .location(location)
                .activity(activity)
                .host(user.fbId())
                .ready(false)
                .users(userMap)
                .build();

        push.setValue(lobby.toFirebaseValue());

        return lobby;
    }
}
