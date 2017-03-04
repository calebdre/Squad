package com.squad.view.lobby;

import com.squad.model.FacebookGraphResponse;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.UserUIItem;

import rx.Observable;

interface LobbyView {
    void addUserToLobby(UserUIItem userUIItem);
    void removeUserFromLobby(UserUIItem userUIItem);
    void setupView(UserUIItem hostUIItem, LobbyUiItem lobbyUiItem);
    void goToDashboard();
    void transformToJoinedLobby();

    Observable<FacebookGraphResponse> joinSquadClicks();
    Observable<Void> startSquadClicks();
}
