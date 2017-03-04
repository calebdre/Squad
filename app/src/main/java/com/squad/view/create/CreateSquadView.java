package com.squad.view.create;

import android.location.Location;

import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.VenueUiItem;

import java.util.List;

import rx.Observable;

interface CreateSquadView {

    Observable<LobbyData> onSquadSubmit();
    Observable<String> onEnterLocationText();
    Observable<VenueUiItem> onSelectedVenue();

    Location getLocation();

    void goToLobby(LobbyUiItem lobby);
    void updateAutocomplete(List<VenueUiItem> venues);
    void showError(String error);
    void setSelectedVenue(VenueUiItem venue);
}
