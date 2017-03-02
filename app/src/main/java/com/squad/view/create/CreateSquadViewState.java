package com.squad.view.create;

import com.squad.model.Lobby;
import com.squad.model.Venue;

import java.util.List;

public class CreateSquadViewState {

    enum State {
        VENUES_RECIEVED,
        SELECTED_VENUE,
        CREATED_SQUAD,
        VENUE_RETRIEVAL_ERROR,
    }

    private static CreateSquadViewState instance;
    private List<Venue> venues;
    private Lobby lobby;
    private Venue selectedVenue;

    public static CreateSquadViewState getInstance() {
        if (instance == null) {
            instance = new CreateSquadViewState();
        }

        return instance;
    }

    private CreateSquadViewState() { }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Venue getSelectedVenue() {
        return selectedVenue;
    }

    public void setSelectedVenue(Venue selectedVenue) {
        this.selectedVenue = selectedVenue;
    }

    public String getNetworkError() {
        return "Could not connect to the network. Please try again later.";
    }
}
