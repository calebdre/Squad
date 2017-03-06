package com.squad.view.join;

import android.location.Location;

import com.squad.view.helpers.ui_items.LobbyUiItem;

import java.util.List;


interface JoinSquadView {

    void setupView(List<LobbyUiItem> lobbyUiItems);
    Location getLocation();
}
