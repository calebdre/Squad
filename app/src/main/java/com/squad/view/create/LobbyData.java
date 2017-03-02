package com.squad.view.create;


import com.squad.model.Venue;

public class LobbyData {

    private String activity;
    private String squadName;
    private Venue location;

    public LobbyData(String activity, String squadName, Venue location) {
        this.activity = activity;
        this.squadName = squadName;
        this.location = location;
    }

    public String getActivity() {
        return activity;
    }

    public String getSquadName() {
        return squadName;
    }

    public Venue getLocation() {
        return location;
    }
}
