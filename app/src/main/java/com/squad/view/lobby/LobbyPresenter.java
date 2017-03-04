package com.squad.view.lobby;


import android.support.v4.util.Pair;

import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;

import rx.functions.Action1;

class LobbyPresenter {

    private LobbyModel model;
    private LobbyViewStateMapper stateTransfer;
    private final LobbyView view;

    LobbyPresenter(String lobbyId, LobbyView view) {
        this.model = new LobbyModel(lobbyId);
        this.view = view;

        stateTransfer = new LobbyViewStateMapper(view);

        init();
    }

    private void init() {
        model.getLobby().subscribe(onReceiveLobby);
        model.onReady().subscribe(onSquadReady);
    }

    void bindActions() {
        view.joinSquadClicks().subscribe(user -> stateTransfer.renderSquadJoinedState(user));
        view.startSquadClicks().subscribe(aVoid -> model.startSquad());
    }

    private Action1<Pair<Lobby, FacebookGraphResponse>> onReceiveLobby = lobbyUserPair -> {
        stateTransfer.renderLobbyReceivedState(lobbyUserPair);
        model.onUserEvent().subscribe(eventTriple -> stateTransfer.renderUserEventState(eventTriple));
    };

    private Action1<Boolean> onSquadReady = isReady -> {
        if (isReady) {
            stateTransfer.renderSquadStartedState();
        }
    };
}
