package com.squad.view.create;

import android.location.Location;

import com.squad.model.FacebookGraphResponse;
import com.squad.view.helpers.ui_items.VenueUiItem;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

class CreateSquadPresenter {

    private CreateSquadModel model;
    private CreateSquadView view;
    private CreateSquadViewStateMapper stateMapper;

    CreateSquadPresenter(CreateSquadView view, FacebookGraphResponse user) {
        this.view = view;
        model = new CreateSquadModel(user);
        stateMapper = new CreateSquadViewStateMapper(view);
    }

    void bindEvents(){
        view.onEnterLocationText().subscribe(fetchLocationResults);
        view.onSquadSubmit().subscribe(createSquad);
        view.onSelectedVenue().subscribe(cacheSelectedVenue); // in memory. not real cache
    }

    private Action1<String> fetchLocationResults = query -> {
            Location location = view.getLocation();
            model.getVenues(location.getLatitude(), location.getLongitude(), query)
                    .map(response -> response.response().venues())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            venues -> stateMapper.renderVenuesReceivedState(venues),
                            exception -> stateMapper.renderVenueRetrievalErrorState()
                    );
    };

    private Action1<LobbyData> createSquad = lobbyInfo -> model.createSquad(lobbyInfo.getActivity(), lobbyInfo.getSquadName(), lobbyInfo.getVenueId())
                                                                .subscribe(lobby -> stateMapper.renderSquadCreatedState(lobby));

    private Action1<VenueUiItem> cacheSelectedVenue = venue -> stateMapper.renderVenueSelectedState(venue);
}
