package com.squad.view.join;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squad.R;
import com.squad.foursquare.FourSquare;
import com.squad.model.Lobby;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.subjects.PublishSubject;

public class JoinSquadRecyclerAdapter extends RecyclerView.Adapter<JoinSquadRecyclerAdapter.JoinSquadRecyclerViewHolder>{

    List<Lobby> fireBaseLobbyList = new ArrayList<>();
    Context context;
    private PublishSubject<String> lobbySubject = PublishSubject.create();

    public JoinSquadRecyclerAdapter(Context context, List<Lobby> lobbies) {
        this.context = context;
        fireBaseLobbyList = lobbies;
    }

    public PublishSubject<String> onLobbySelect() {
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
        Lobby lobby = fireBaseLobbyList.get(position);
        holder.itemView.setOnClickListener(v -> lobbySubject.onNext(lobby.id()));
        PrettyTime p = new PrettyTime();
        String ago = p.format(new Date(lobby.createdAt()));

        try {
            new FourSquare().getImageForLocation(lobby.location().lat(), lobby.location().lng(), lobby.location().address(), (url) -> {
                ((Activity) context).runOnUiThread(() -> {
                    Picasso.with(context).load(url).into(holder.image);
                });
            });
        } catch (IOException e) {
        }

        holder.title.setText(lobby.activity() + " at " + lobby.location().name());
    }

    @Override
    public int getItemCount() {
        return fireBaseLobbyList.size();
    }

    public class JoinSquadRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        View itemView;

        public JoinSquadRecyclerViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.lobby_item_title);
            image = (ImageView) itemView.findViewById(R.id.lobby_item_image);
            this.itemView = itemView;
        }
    }
}
