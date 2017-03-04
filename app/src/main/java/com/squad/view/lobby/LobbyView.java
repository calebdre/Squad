package com.squad.view.lobby;

import com.squad.model.FacebookGraphResponse;

import rx.Observable;

public interface LobbyView {
    void addUserToLobby(UserUIItem userUIItem);
    void removeUserFromLobby(UserUIItem userUIItem);
    void setupView(UserUIItem hostUIItem, LobbyUiItem lobbyUiItem);
    void goToDashboard();
    void transformToJoinedLobby();
    void renderVenueImage(String url);

    Observable<FacebookGraphResponse> joinSquadClicks();
    Observable<Void> startSquadClicks();
}
