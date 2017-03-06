package com.squad.view.lobby;

import com.squad.model.FacebookGraphResponse;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.UserUIItem;

import rx.Observable;

interface LobbyView {
    void addUserToLobby(UserUIItem userUIItem);
    void addCurrentUserToLobby(UserUIItem userUIItem);
    void removeUserFromLobby(UserUIItem userUIItem);
    void setupView(UserUIItem hostUIItem, LobbyUiItem lobbyUiItem);
    void goToDashboard();
    void setUserIsInSquad();
    void setUserIsHost();

    // boolean for whether or not the user is the host
    Observable<FacebookGraphResponse> joinSquadClicks();
    Observable<Void> startSquadClicks();
    Observable<FacebookGraphResponse> leaveSquadClicks();
}
