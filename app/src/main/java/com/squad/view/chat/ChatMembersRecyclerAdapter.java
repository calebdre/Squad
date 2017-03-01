package com.squad.view.chat;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.profile.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatMembersRecyclerAdapter  extends RecyclerView.Adapter<ChatMembersRecyclerAdapter.ChatMembersRecyclerViewHolder>{

    List<FacebookGraphResponse> users = new ArrayList<>();
    List<String> userIds = new ArrayList<>();

    public ChatMembersRecyclerAdapter(String lobbyId) {
        init(lobbyId);
    }

    private void init(String lobbyId) {
        FirebaseDatabase.getInstance()
                .getReference("lobbies/" + lobbyId + "/users")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FacebookGraphResponse user = FacebookGraphResponse.create(dataSnapshot);
                        userIds.add(dataSnapshot.getKey());
                        users.add(user);
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
    public ChatMembersRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_chat_member_item, parent, false);
        return new ChatMembersRecyclerViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ChatMembersRecyclerViewHolder holder, int position) {
        FacebookGraphResponse user = users.get(position);
        Picasso.with(holder.imageView.getContext())
                .load(user.picture().data().url())
                .transform(new CircleTransform())
                .into(holder.imageView);

        RxView.clicks(holder.imageView).subscribe((aVoid)-> {

           FirebaseDatabase.getInstance()
                   .getReference("/users/" + user.fbId() + "/points")
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           long num;
                           if(dataSnapshot.getValue() == null) {
                               num = 0;
                           } else {
                               num = (long) dataSnapshot.getValue() + 1;
                           }
                           FirebaseDatabase.getInstance()
                                   .getReference("/users/" + user.fbId() + "/points")
                                   .setValue(num);
                           Snackbar.make(holder.imageView, holder.imageView.getContext().getString(R.string.points, user.name()), Snackbar.LENGTH_LONG).show();
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ChatMembersRecyclerViewHolder extends ViewHolder{

        ImageView imageView;

        public ChatMembersRecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
