package com.squad.view.create;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squad.api.foursquare.FourSquareClient;
import com.squad.api.foursquare.FoureSquareMiniVenuesResponse;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.model.Venue;

import java.util.Calendar;
import java.util.HashMap;

import rx.Observable;

public class CreateSquadModel {

    public static final String LOBBY_REFERENCE_STRING = "/lobbies";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference lobbyReference;
    private FacebookGraphResponse user;
    private FourSquareClient fourSquareClient;

    public CreateSquadModel(FacebookGraphResponse user) {
        this.user = user;
        firebaseDatabase = FirebaseDatabase.getInstance();
        lobbyReference = firebaseDatabase.getReference(LOBBY_REFERENCE_STRING);
        fourSquareClient = new FourSquareClient();
    }

    public Lobby createSquad(String activity, String squadName, Venue location){
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

    public Observable<FoureSquareMiniVenuesResponse> getVenues(double lat, double lon, String query) {
        return fourSquareClient.getVenues(lat, lon, query);
    }

    public Observable<String> getImageUrl(String id) {
        return fourSquareClient.getImageUrl(id);
    }
}
