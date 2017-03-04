package com.squad.view.create;

import com.squad.model.Lobby;
import com.squad.model.Venue;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.VenueUiItem;

import java.util.ArrayList;
import java.util.List;

class CreateSquadViewStateMapper {

    private CreateSquadView view;

    CreateSquadViewStateMapper(CreateSquadView view) {
        this.view = view;
    }

    void renderVenuesReceivedState(List<Venue> venues) {
        List<VenueUiItem> items = new ArrayList<>();
        for (Venue venue : venues) {
            items.add(new VenueUiItem(venue));
        }

        view.updateAutocomplete(items);
    }

    void renderVenueSelectedState(VenueUiItem venue) {
        view.setSelectedVenue(venue);
    }

    void renderSquadCreatedState(Lobby lobby) {
        LobbyUiItem lobbyUiItem = new LobbyUiItem(lobby);
        view.goToLobby(lobbyUiItem);
    }

    void renderVenueRetrievalErrorState() {
        view.showError("Could not connect to the network. Please try again later.");
    }
}
