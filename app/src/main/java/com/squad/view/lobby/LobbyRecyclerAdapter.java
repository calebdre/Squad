package com.squad.view.lobby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent.EventType;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.profile.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LobbyRecyclerAdapter extends RecyclerView.Adapter<LobbyRecyclerAdapter.LobbyRecyclerViewHolder> {

    private List<FacebookGraphResponse> users;
    private List<String> userIds = new ArrayList<>();
    private Context context;

    public LobbyRecyclerAdapter(Context context, List<FacebookGraphResponse> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public LobbyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.view_lobby_user, parent, false);

        // Return a new holder instance
        return new LobbyRecyclerViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(LobbyRecyclerViewHolder holder, int position) {
// Get the data model based on position
        FacebookGraphResponse user = users.get(position);

        holder.nameTextView.setText(user.name());
        Picasso.with(context).load(user.picture().data().url()).transform(new CircleTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateDataset(FacebookGraphResponse user, EventType event, String userKey) {
        switch (event) {
            case ADDED:
                addUser(user, userKey);
                break;
            case REMOVED:
                removeUser(userKey);
                break;
            default:
                throw new IllegalArgumentException("uh oh. Something happened to the user that we didn't account for");
        }
    }

    private void removeUser(String userKey) {
        // [START_EXCLUDE]
        int userIndex = userIds.indexOf(userKey);
        if (userIndex > -1) {
            // Remove data from the list
            userIds.remove(userIndex);
            users.remove(userIndex);

            // Update the RecyclerView
            notifyItemRemoved(userIndex);
        }
    }

    private void addUser(FacebookGraphResponse user, String userKey) {
        userIds.add(userKey);
        users.add(user);
        notifyItemInserted(users.size() - 1);
    }

    public static class LobbyRecyclerViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public LobbyRecyclerViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any LobbyRecyclerViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.lobby_user_name);
            image = (ImageView) itemView.findViewById(R.id.lobby_user_image);
        }
    }
}
