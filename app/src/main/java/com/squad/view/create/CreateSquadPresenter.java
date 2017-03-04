package com.squad.view.create;

import android.location.Location;

import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.model.Venue;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.squad.view.create.CreateSquadViewState.State.CREATED_SQUAD;
import static com.squad.view.create.CreateSquadViewState.State.SELECTED_VENUE;
import static com.squad.view.create.CreateSquadViewState.State.VENUES_RECIEVED;
import static com.squad.view.create.CreateSquadViewState.State.VENUE_RETRIEVAL_ERROR;

public class CreateSquadPresenter {

    private CreateSquadModel model;
    private CreateSquadView view;
    private CreateSquadViewState viewState;

    public CreateSquadPresenter(CreateSquadView view, FacebookGraphResponse user) {
        this.view = view;
        model = new CreateSquadModel(user);
        viewState = CreateSquadViewState.getInstance();
    }

    public void bindEvents(){
        view.onEnterLocationText().subscribe(fetchLocationResults);
        view.onSquadSubmit().subscribe(createSquad);
        view.onSelectedVenue().subscribe(cacheSelectedVenue); // in memory. not real cache
    }

    private Action1<String> fetchLocationResults = query -> {
            Location location = view.getLocation();
            model.getVenues(location.getLatitude(), location.getLongitude(), query)
                    .map(response -> response.response().venues())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(venues -> {
                        viewState.setVenues(venues);
                        view.render(VENUES_RECIEVED);
                    }, (e) -> view.render(VENUE_RETRIEVAL_ERROR));
    };

    private Action1<LobbyData> createSquad = lobbyInfo -> {
        Lobby lobby = model.createSquad(lobbyInfo.getActivity(), lobbyInfo.getSquadName(), lobbyInfo.getLocation());
        viewState.setLobby(lobby);
        view.render(CREATED_SQUAD);
    };

    private Action1<Venue> cacheSelectedVenue = venue -> {
        viewState.setSelectedVenue(venue);
        view.render(SELECTED_VENUE);
    };
}
