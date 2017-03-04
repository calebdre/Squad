package com.squad.view.lobby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squad.R;
import com.squad.view.helpers.ui_items.UserUIItem;
import com.squad.view.profile.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

class LobbyRecyclerAdapter extends RecyclerView.Adapter<LobbyRecyclerAdapter.LobbyRecyclerViewHolder> {

    private List<UserUIItem> users;
    private Context context;

    LobbyRecyclerAdapter(Context context, List<UserUIItem> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public LobbyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.view_lobby_user, parent, false);

        return new LobbyRecyclerViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(LobbyRecyclerViewHolder holder, int position) {
        UserUIItem user = users.get(position);

        holder.nameTextView.setText(user.name());
        Picasso.with(context).load(user.pictureUrl()).transform(new CircleTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class LobbyRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView image;

        LobbyRecyclerViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.lobby_user_name);
            image = (ImageView) itemView.findViewById(R.id.lobby_user_image);
        }
    }
}
