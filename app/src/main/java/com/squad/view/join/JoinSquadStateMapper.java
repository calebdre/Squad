package com.squad.view.join;

import com.squad.model.Lobby;
import com.squad.view.helpers.ui_items.LobbyUiItem;

import java.util.ArrayList;
import java.util.List;

public class JoinSquadStateMapper {

    private JoinSquadView view;

    public JoinSquadStateMapper(JoinSquadView view) {
        this.view = view;
    }

    public void renderLobbiesRecievedState(List<Lobby> lobbies){
        List<LobbyUiItem> uiItems = new ArrayList<>();
        for (Lobby lobby: lobbies) {
            uiItems.add(new LobbyUiItem(lobby));
        }

        view.setupView(uiItems);
    }
}
