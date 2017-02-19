package com.squad.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.R;
import com.squad.chat.ChatActivity;
import com.squad.foursquare.FourSquare;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.FireBaseLobby;
import com.squad.model.Lobby;
import com.squad.model.MeetupLocation;
import com.squad.profile.CircleTransform;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squad.ChooseActivity.EXTRA_FB_USER;
import static com.squad.create.CreateSquadActivity.EXTRA_LOBBY;
import static com.squad.create.CreateSquadActivity.EXTRA_MEETUP_LOCATION;

public class LobbyActivity extends AppCompatActivity {

    public static final java.lang.String EXTRA_LOBBY_KEY = "extra_lobby_key";

    @BindView(R.id.lobby_members_count) TextView memberCount;
    @BindView(R.id.lobby_item_image) ImageView lobbyImage;
    @BindView(R.id.lobby_host_image) ImageView hostImage;
    @BindView(R.id.lobby_activity) TextView activity;
    @BindView(R.id.lobby_created_at) TextView createdAt;
    @BindView(R.id.lobby_host_name) TextView hostName;
    @BindView(R.id.lobby_place_address) TextView placeAddress;
    @BindView(R.id.lobby_place_name) TextView placeName;
    @BindView(R.id.lobby_users) RecyclerView usersList;
    @BindView(R.id.lobby_start_squad) Button startButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        Lobby lobby = (Lobby) extras.getSerializable(EXTRA_LOBBY);

        PrettyTime p = new PrettyTime();
        String key = extras.getString(EXTRA_LOBBY_KEY);
        FacebookGraphResponse user = (FacebookGraphResponse) extras.getSerializable(EXTRA_FB_USER);
        FirebaseDatabase.getInstance()
                .getReference("lobbies/" + key + "/ready")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isReady = (boolean) dataSnapshot.getValue();
                        if (isReady) {
                           Intent intent = new Intent(LobbyActivity.this, ChatActivity.class);
                            intent.putExtra(EXTRA_LOBBY_KEY, key);
                            intent.putExtra(EXTRA_FB_USER, user);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        if(lobby == null) {
            startButton.setVisibility(View.VISIBLE);
            RxView.clicks(startButton).subscribe((aVoid) ->{
                FirebaseDatabase.getInstance()
                        .getReference("lobbies/" + key + "/ready").setValue(true);
            });
            FirebaseDatabase.getInstance()
                    .getReference("lobbies/" + key)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FireBaseLobby lobby = FireBaseLobby.create(dataSnapshot);

                    try {
                        new FourSquare().getImageForLocation(lobby.location().lat(), lobby.location().lng(), lobby.location().address(), (url) -> {
                            Picasso.with(LobbyActivity.this).load(url).into(lobbyImage);
                            supportPostponeEnterTransition();
                            lobbyImage.getViewTreeObserver().addOnPreDrawListener(
                                    new ViewTreeObserver.OnPreDrawListener() {
                                        @Override
                                        public boolean onPreDraw() {
                                            lobbyImage.getViewTreeObserver().removeOnPreDrawListener(this);
                                            supportStartPostponedEnterTransition();
                                            return true;
                                        }
                                    }
                            );
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String ago = p.format(new Date(lobby.created()));

                    activity.setText(lobby.activity());
                    createdAt.setText(ago);
                    placeAddress.setText(lobby.location().address());
                    placeName.setText(lobby.location().name());

                    LobbyRecyclerAdapter adapter = new LobbyRecyclerAdapter(LobbyActivity.this, key);
                    usersList.setAdapter(adapter);
                    usersList.setLayoutManager(new LinearLayoutManager(LobbyActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    FirebaseDatabase.getInstance()
                            .getReference("/users/" + lobby.host())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FacebookGraphResponse host = FacebookGraphResponse.create(dataSnapshot);
                                    hostName.setText("Organized by " + host.name());
                                    Picasso.with(LobbyActivity.this).load(host.picture().data().url()).transform(new CircleTransform()).into(hostImage);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            MeetupLocation location = (MeetupLocation) extras.getSerializable(EXTRA_MEETUP_LOCATION);

            String ago = p.format(new Date(lobby.createdAt()));

            activity.setText(lobby.name() + " @ " +lobby.activity());
            createdAt.setText(ago);
            hostName.setText(lobby.host().name());
            placeAddress.setText(location.address());
            placeName.setText(location.name());

            LobbyRecyclerAdapter adapter = new LobbyRecyclerAdapter(this, lobby.firebaseKey());
            usersList.setAdapter(adapter);
            usersList.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
