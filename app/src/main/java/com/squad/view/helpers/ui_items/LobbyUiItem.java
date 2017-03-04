package com.squad.view.helpers.ui_items;

import com.squad.model.Group;
import com.squad.model.Item;
import com.squad.model.Lobby;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

public class LobbyUiItem {

    private Lobby lobby;

    public LobbyUiItem(Lobby lobby) {
        this.lobby = lobby;
    }

    public String timeSinceCreate() {
        return new PrettyTime().format(new Date(lobby.createdAt()));
    }

    public String activity() {
        return lobby.activity();
    }

    public String address() {
        return lobby.location().location().address();
    }

    public String placeName() {
        return lobby.location().name();
    }

    public String id() {
        return lobby.id();
    }

    public String imageUrl() {
        List<Group> groups = lobby.location().photos().groups();
        for (Group group : groups) {
            if (group.type().equals("venue")) {
                Item item = group.items().get(0);
                return item.prefix() + "1300x500" + item.suffix();
            }
        }

        return "";
    }
}
