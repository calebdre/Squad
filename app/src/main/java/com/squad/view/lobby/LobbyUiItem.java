package com.squad.view.lobby;

import com.squad.model.Lobby;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class LobbyUiItem {

    private Lobby lobby;

    public LobbyUiItem(Lobby lobby) {
        this.lobby = lobby;
    }

    public String timeSinceCreate() {
        return new PrettyTime().format(new Date(lobby.createdAt()));
    }

    public String activity() {
        return lobby.activity();
    }

    public String address() {
        return lobby.location().location().address();
    }

    public String placeName() {
        return lobby.location().name();
    }
}
