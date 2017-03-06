package com.squad.view.join;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squad.R;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.PublishSubject;

class JoinSquadRecyclerAdapter extends RecyclerView.Adapter<JoinSquadRecyclerAdapter.JoinSquadRecyclerViewHolder>{

    private List<LobbyUiItem> fireBaseLobbyList = new ArrayList<>();
    private Location location;
    private Context context;
    private PublishSubject<String> lobbySubject = PublishSubject.create();

    JoinSquadRecyclerAdapter(Context context, List<LobbyUiItem> lobbies, Location location) {
        this.context = context;
        fireBaseLobbyList = lobbies;
        this.location = location;
    }

    PublishSubject<String> onLobbySelect() {
        return lobbySubject;
    }

    @Override
    public JoinSquadRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_lobby_item, parent, false);
        return new JoinSquadRecyclerViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(JoinSquadRecyclerViewHolder holder, int position) {
        LobbyUiItem lobby = fireBaseLobbyList.get(position);
        holder.itemView.setOnClickListener(v -> lobbySubject.onNext(lobby.id()));
        holder.activity.setText(lobby.activity());
        holder.distance.setText(context.getString(R.string.distance_km, lobby.distanceFrom(location)));
        holder.placeName.setText(lobby.placeName());

        if (lobby.placeAddress() != null) {
            holder.placeAddress.setText("(" + lobby.placeAddress() + ")");
        }

        if (!lobby.imageUrl().isEmpty()) {
            Picasso.with(context).load(lobby.imageUrl()).into(holder.placeImage);
        }
    }

    @Override
    public int getItemCount() {
        return fireBaseLobbyList.size();
    }

    class JoinSquadRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView placeImage;
        TextView activity;
        TextView distance;
        TextView placeAddress;
        TextView placeName;
        View itemView;

        JoinSquadRecyclerViewHolder(View itemView) {
            super(itemView);

            activity = (TextView) itemView.findViewById(R.id.lobby_item_activity);
            distance = (TextView) itemView.findViewById(R.id.lobby_item_distance);
            placeAddress = (TextView) itemView.findViewById(R.id.lobby_item_place_address);
            placeName = (TextView) itemView.findViewById(R.id.lobby_item_place_name);
            placeImage = (ImageView) itemView.findViewById(R.id.lobby_item_image);
            this.itemView = itemView;
        }
    }
}
