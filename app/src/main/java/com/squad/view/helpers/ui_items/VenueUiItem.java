package com.squad.view.helpers.ui_items;

import android.location.Location;

import com.squad.model.Venue;
import com.squad.view.join.Calculator;

public class VenueUiItem {

    private Venue venue;

    public VenueUiItem(Venue venue) {
        this.venue = venue;
    }

    public String address () {
        return venue.location().address();
    }

    public String name() {
        return venue.name();
    }

    public String distanceFrom(Location userCoords) {
        return Calculator.distance(venue.location().lat(), venue.location().lng(), userCoords.getLatitude(), userCoords.getLongitude());
    }

    public String id() {
        return venue.id();
    }
}
