package com.squad.view.lobby;

import android.support.v4.util.Pair;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.UserUIItem;

import org.apache.commons.lang3.tuple.Triple;

class LobbyViewStateMapper {

    private LobbyView view;

    LobbyViewStateMapper(LobbyView view) {
        this.view = view;
    }

    void renderUserEventState(Triple<FacebookGraphResponse, RxFirebaseChildEvent.EventType, String> userEvent) {
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

    void renderSquadJoinedState(FacebookGraphResponse user) {
        UserUIItem userUIItem = new UserUIItem(user);

        view.addUserToLobby(userUIItem);
        view.transformToJoinedLobby();
    }

    void renderSquadStartedState() {
        view.goToDashboard();
    }

    void renderLobbyReceivedState(Pair<Lobby, FacebookGraphResponse> lobbyUserPair) {
        UserUIItem hostUIItem = new UserUIItem(lobbyUserPair.second);
        LobbyUiItem lobbyUiItem = new LobbyUiItem(lobbyUserPair.first);
        view.setupView(hostUIItem, lobbyUiItem);
    }
}
