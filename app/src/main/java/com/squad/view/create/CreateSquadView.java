package com.squad.view.create;

import android.location.Location;

import com.squad.model.Venue;

import rx.Observable;

public interface CreateSquadView {

    Observable<LobbyData> onSquadSubmit();
    Observable<String> onEnterLocationText();
    Observable<Venue> onSelectedVenue();

    Location getLocation();
    void render(CreateSquadViewState.State state);
}
