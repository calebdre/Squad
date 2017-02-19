package com.squad.join;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.Pair;
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
import com.google.firebase.database.ValueEventListener;
import com.squad.R;
import com.squad.foursquare.FourSquare;
import com.squad.model.FireBaseLobby;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.subjects.PublishSubject;

public class JoinSquadRecyclerAdapter extends RecyclerView.Adapter<JoinSquadRecyclerAdapter.JoinSquadRecyclerViewHolder>{

    List<FireBaseLobby> fireBaseLobbyList = new ArrayList<>();
    List<String> fireBaseLobbyListIds = new ArrayList<>();
    HashMap<String, Integer> lobbyCountMap = new HashMap<>();
    Context context;
    private double currentLat = 0;
    private double currentLnng = 0;
    private PublishSubject<Pair<String, View>> lobbySubject = PublishSubject.create();

    public JoinSquadRecyclerAdapter(Context context) {
        this.context = context;
        init();
    }

    public void updateLocation(double userLat, double userLng) {
        currentLat = userLat;
        currentLnng = userLng;
        notifyDataSetChanged();
    }

    public PublishSubject<Pair<String, View>> onLobbySelect() {
        return lobbySubject;
    }

    private void init() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("lobbies/");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fireBaseLobbyListIds.add(dataSnapshot.getKey());
                try{
                    fireBaseLobbyList.add(FireBaseLobby.create(dataSnapshot));
                    notifyItemInserted(fireBaseLobbyList.size() - 1);
                } catch (NullPointerException e) {
                    FirebaseDatabase.getInstance()
                            .getReference("lobbies/" + dataSnapshot.getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    fireBaseLobbyList.add(FireBaseLobby.create(dataSnapshot));
                                    notifyItemInserted(fireBaseLobbyList.size() - 1);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int userIndex = fireBaseLobbyListIds.indexOf(userId);
                if (userIndex > -1) {
                    // Remove data from the list
                    fireBaseLobbyListIds.remove(userIndex);
                    fireBaseLobbyList.remove(userIndex);

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
    public JoinSquadRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_lobby_item, parent, false);
        return new JoinSquadRecyclerViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(JoinSquadRecyclerViewHolder holder, int position) {
        FireBaseLobby lobby = fireBaseLobbyList.get(position);
        holder.itemView.setOnClickListener(v -> lobbySubject.onNext(new Pair<>(fireBaseLobbyListIds.get(position), holder.image)));
        PrettyTime p = new PrettyTime();
        String ago = p.format(new Date(lobby.created()));
        String currentLobbyId = fireBaseLobbyListIds.get(position);

        try {
            new FourSquare().getImageForLocation(lobby.location().lat(), lobby.location().lng(), lobby.location().address(), (url) -> {
                ((Activity) context).runOnUiThread(() -> {
                    Picasso.with(context).load(url).into(holder.image);
                });

            });
        } catch (IOException e) {
        }

        holder.title.setText(lobby.activity() + " at " + lobby.location().name());

        lobbyCountMap.put(currentLobbyId, 0);
        FirebaseDatabase.getInstance().getReference("lobbies/" + currentLobbyId + "/users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                lobbyCountMap.put(currentLobbyId, lobbyCountMap.get(currentLobbyId) + 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                lobbyCountMap.put(currentLobbyId, lobbyCountMap.get(currentLobbyId) - 1);
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
