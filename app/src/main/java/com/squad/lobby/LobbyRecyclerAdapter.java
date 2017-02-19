package com.squad.lobby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LobbyRecyclerAdapter extends RecyclerView.Adapter<LobbyRecyclerAdapter.LobbyRecyclerViewHolder> {

    private List<FacebookGraphResponse> users = new ArrayList<>();
    private List<String> userIds = new ArrayList<>();
    private Context context;

    public LobbyRecyclerAdapter(Context context, String lobbyKey) {
        this.context = context;
        init(lobbyKey);
    }

    private void init(String lobbyKey) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("lobbies/" + lobbyKey + "/users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userIds.add(dataSnapshot.getKey());
                users.add(FacebookGraphResponse.create(dataSnapshot));
                notifyItemInserted(users.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int userIndex = userIds.indexOf(userId);
                if (userIndex > -1) {
                    // Remove data from the list
                    userIds.remove(userIndex);
                    users.remove(userIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(userIndex);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        // Set item views based on your views and data model
        String about = user.about() == null ? "" : user.about();
        holder.descriptionTextView.setText(about);
        holder.nameTextView.setText(user.name());
        Picasso.with(context).load(user.picture().data().url()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class LobbyRecyclerViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public LobbyRecyclerViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any LobbyRecyclerViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.lobby_user_name);
            descriptionTextView = (TextView) itemView.findViewById(R.id.lobby_user_description);
            image = (ImageView) itemView.findViewById(R.id.lobby_user_image);
        }
    }
}
