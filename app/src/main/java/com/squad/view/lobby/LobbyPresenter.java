package com.squad.view.lobby;


import android.support.v4.util.Pair;

import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;

import rx.Subscription;
import rx.functions.Action1;

class LobbyPresenter {

    private LobbyModel model;
    private LobbyViewStateMapper stateTransfer;
    private FacebookGraphResponse user;
    private LobbyView view;

    private Subscription joinSquadSubscription;
    private Subscription startSquadSubscription;
    private Subscription removeSquadSubscription;

    LobbyPresenter(String lobbyId, LobbyView view, FacebookGraphResponse user) {
        this.model = new LobbyModel(lobbyId);
        this.view = view;
        this.user = user;

        stateTransfer = new LobbyViewStateMapper(view);

        init();
    }

    private void init() {
        model.getLobby().subscribe(onReceiveLobby);
        model.onReady().subscribe(onSquadReady);
    }

    void bindActions() {
        if(joinSquadSubscription != null) {
            joinSquadSubscription.unsubscribe();
        }

        if(startSquadSubscription != null) {
            startSquadSubscription.unsubscribe();
        }

        if (removeSquadSubscription != null) {
           removeSquadSubscription.unsubscribe();
        }

        startSquadSubscription = view.startSquadClicks().subscribe(aVoid -> model.startSquad());
        removeSquadSubscription = view.leaveSquadClicks().subscribe(user -> {
            model.removeUserFromSquad(user);
            stateTransfer.renderUserLeftSquadState(user);
        });
        joinSquadSubscription = view.joinSquadClicks().subscribe(user -> {
            model.addUserToSquad(user);
            stateTransfer.renderUserJoinedSquadState(user);
        });
    }

    private Action1<Pair<Lobby, FacebookGraphResponse>> onReceiveLobby = lobbyHostPair -> {
        stateTransfer.renderLobbyReceivedState(lobbyHostPair, user);
        model.onUserEvent().subscribe(eventTriple -> {
            stateTransfer.renderUserEventState(eventTriple);
        });
    };

    private Action1<Boolean> onSquadReady = isReady -> {
        if (isReady) {
            stateTransfer.renderSquadStartedState();
        }
    };
}
