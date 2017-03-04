package com.squad.view.create;


class LobbyData {

    private String activity;
    private String squadName;
    private String venueId;

    LobbyData(String activity, String squadName, String venueId) {
        this.activity = activity;
        this.squadName = squadName;
        this.venueId = venueId;
    }

    public String getActivity() {
        return activity;
    }

    String getSquadName() {
        return squadName;
    }

    String getVenueId() {
        return venueId;
    }
}
