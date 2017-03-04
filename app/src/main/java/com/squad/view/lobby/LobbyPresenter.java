package com.squad.view.lobby;


import android.support.v4.util.Pair;

import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;

import rx.functions.Action1;

public class LobbyPresenter {

    private LobbyModel model;
    private LobbyViewStateMapper stateTransfer;
    private final LobbyView view;

    public LobbyPresenter(String lobbyId, LobbyView view) {
        this.model = new LobbyModel(lobbyId);
        this.view = view;

        stateTransfer = new LobbyViewStateMapper(view);

        init();
    }

    private void init() {
        model.getLobby().subscribe(onReceiveLobby);
        model.onReady().subscribe(onSquadReady);
    }

    public void bindActions() {
        view.joinSquadClicks().subscribe(user -> stateTransfer.renderSquadJoinedState(user));
        view.startSquadClicks().subscribe(startSquad);
    }

    private Action1<Pair<Lobby, FacebookGraphResponse>> onReceiveLobby = lobbyUserPair -> {
        stateTransfer.renderLobbyReceivedState(lobbyUserPair);
        model.getLobbyImage(lobbyUserPair.first.location().id())
                .subscribe((url) -> stateTransfer.renderVenueImageReceived(url));
        model.onUserEvent().subscribe(eventTriple -> stateTransfer.renderUserEventState(eventTriple));
    };

    private Action1<Boolean> onSquadReady = isReady -> {
        if (isReady) {
            stateTransfer.renderSquadStartedState();
        }
    };

    private Action1<Void> startSquad = aVoid -> {
        model.startSquad();
    };
}
