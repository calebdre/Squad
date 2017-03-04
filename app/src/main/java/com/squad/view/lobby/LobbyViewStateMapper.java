package com.squad.view.lobby;

import android.support.v4.util.Pair;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;

import org.apache.commons.lang3.tuple.Triple;

public class LobbyViewStateMapper {

    private LobbyView view;

    public LobbyViewStateMapper(LobbyView view) {
        this.view = view;
    }

    public void renderUserEventState(Triple<FacebookGraphResponse, RxFirebaseChildEvent.EventType, String> userEvent) {
        UserUIItem userUIItem = new UserUIItem(userEvent.getLeft());

        switch (userEvent.getMiddle()) {
            case ADDED:
                view.addUserToLobby(userUIItem);
                break;
            case REMOVED:
                view.removeUserFromLobby(userUIItem);
                break;
            default:
                throw new IllegalArgumentException("An option was just passed that isn't possible");
        }
    }

    public void renderSquadJoinedState(FacebookGraphResponse user) {
        UserUIItem userUIItem = new UserUIItem(user);

        view.addUserToLobby(userUIItem);
        view.transformToJoinedLobby();
    }

    public void renderSquadStartedState() {
        view.goToDashboard();
    }

    public void renderLobbyReceivedState(Pair<Lobby, FacebookGraphResponse> lobbyUserPair) {
        UserUIItem hostUIItem = new UserUIItem(lobbyUserPair.second);
        LobbyUiItem lobbyUiItem = new LobbyUiItem(lobbyUserPair.first);
        view.setupView(hostUIItem, lobbyUiItem);
    }

    public void renderVenueImageReceived(String url) {
        view.renderVenueImage(url);
    }
}
