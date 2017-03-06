package com.squad.view.lobby;

import android.support.v4.util.Pair;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.UserUIItem;

import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;

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

    void renderUserJoinedSquadState(FacebookGraphResponse user) {
        UserUIItem userUIItem = new UserUIItem(user);
        view.addCurrentUserToLobby(userUIItem);
    }

    void renderSquadStartedState() {
        view.goToDashboard();
    }

    void renderLobbyReceivedState(Pair<Lobby, FacebookGraphResponse> lobbyUserPair, FacebookGraphResponse user) {
        UserUIItem hostUIItem = new UserUIItem(lobbyUserPair.second);
        LobbyUiItem lobbyUiItem = new LobbyUiItem(lobbyUserPair.first);
        view.setupView(hostUIItem, lobbyUiItem);

        if (user.id().equals(lobbyUserPair.second.id())) {
            view.setUserIsHost();
            return;
        }

        Map<String, FacebookGraphResponse> users = lobbyUserPair.first.users();
        if(users == null) return;

        for (Map.Entry<String, FacebookGraphResponse> entry: users.entrySet()) {
            if(entry.getValue().id().equals(user.id())) {
                view.setUserIsInSquad();
                return;
            }
        }
    }

    void renderUserLeftSquadState(FacebookGraphResponse user) {
        UserUIItem userUIItem = new UserUIItem(user);
        view.removeUserFromLobby(userUIItem);
    }
}
