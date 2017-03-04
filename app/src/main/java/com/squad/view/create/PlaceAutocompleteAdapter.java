package com.squad.view.create;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squad.view.helpers.ui_items.VenueUiItem;

import java.util.List;

class PlaceAutocompleteAdapter extends ArrayAdapter<VenueUiItem> {

    private List<VenueUiItem> places;

    PlaceAutocompleteAdapter(@NonNull Context context, List<VenueUiItem> places) {
        super(context, 0, places);
        this.places = places;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        VenueUiItem venue = places.get(position);
        String text = venue.name();
        if(venue.address() != null) {
            text += " (" + venue.address() + ")";
        }

        ((TextView) convertView.findViewById(android.R.id.text1)).setText(text);
        return convertView;
    }

    @Nullable
    @Override
    public VenueUiItem getItem(int position) {
        return places.get(position);
    }

    @Override
    public int getCount() {
        return places.size();
    }
}
