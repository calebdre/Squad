package com.squad.view.join;

import com.squad.model.Lobby;

import rx.Observable;


public interface JoinSquadView {

    Observable<Lobby> lobbySelectClicks();
}
