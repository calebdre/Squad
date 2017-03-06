package com.squad.view.helpers.ui_items;

import android.location.Location;
import android.support.annotation.Nullable;

import com.squad.model.Category;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Group;
import com.squad.model.Item;
import com.squad.model.Lobby;
import com.squad.model.Venue;
import com.squad.util.GPSDistanceCalculator;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public String placeAddress() {
        return lobby.location().location().address();
    }

    public String placeName() {
        return lobby.location().name();
    }

    public String id() {
        return lobby.id();
    }

    public int numberOfMembers() {
        Map<String, FacebookGraphResponse> users = lobby.users();
        if (users == null) {
            return 0;
        } else {
            return users.size();
        }
    }

    public String imageUrl() {
        Venue location = lobby.location();

        if (location.photos() == null) {
            return "";
        }
        List<Group> groups = location.photos().groups();
        for (Group group : groups) {
            if (group.type().equals("venue")) {
                Item item = group.items().get(0);
                return item.prefix() + "1300x500" + item.suffix();
            }
        }

        return "";
    }

    @Nullable
    public List<CategoryUiItem> categories() {
        List<Category> categories = lobby.location().categories();
        if (categories == null) {
            return null;
        }

        List<CategoryUiItem> uiItems = new ArrayList<>();
        for (Category category: categories) {
            uiItems.add(new CategoryUiItem(category));
        }

        return uiItems;
    }

    public String distanceFrom(Location userCoords) {
        com.squad.model.Location location = lobby.location().location();
        return GPSDistanceCalculator.distance(location.lat(), location.lng(), userCoords.getLatitude(), userCoords.getLongitude());
    }
}
