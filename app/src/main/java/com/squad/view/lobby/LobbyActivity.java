package com.squad.view.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.R;
import com.squad.foursquare.FourSquare;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.view.chat.ChatActivity;
import com.squad.view.profile.CircleTransform;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squad.ChooseActivity.EXTRA_FB_USER;

public class LobbyActivity extends AppCompatActivity {

    public static final java.lang.String EXTRA_LOBBY_KEY = "extra_lobby_key";

    @BindView(R.id.lobby_toolbar) Toolbar toolbar;
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

    private LobbyModel model;
    private List<FacebookGraphResponse> users = new ArrayList<>();
    private LobbyRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        setupToolbar();

        Bundle extras = getIntent().getExtras();

        String key = extras.getString(EXTRA_LOBBY_KEY);
        FacebookGraphResponse user = (FacebookGraphResponse) extras.getSerializable(EXTRA_FB_USER);
        model = new LobbyModel(key);

        RxView.clicks(startButton).subscribe((aVoid) -> {
            FirebaseDatabase.getInstance().getReference("lobbies/" + key + "/ready").setValue(true);
        });

        model.onReady().subscribe((isReady) -> {
            if (isReady) {
                Intent intent = new Intent(LobbyActivity.this, ChatActivity.class);
                intent.putExtra(EXTRA_LOBBY_KEY, key);
                intent.putExtra(EXTRA_FB_USER, user);
                startActivity(intent);
            }
        });

        model.getLobby().subscribe((lobbyUserPair) -> setupView(lobbyUserPair.first, lobbyUserPair.second));
    }

    private void setupView(Lobby lobby, FacebookGraphResponse host) {
        try {
            new FourSquare().getImageForLocation(lobby.location().lat(), lobby.location().lng(), lobby.location().address(), (url) -> {
                Picasso.with(LobbyActivity.this).load(url).into(lobbyImage);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrettyTime p = new PrettyTime();
        String ago = p.format(new Date(lobby.createdAt()));

        activity.setText(lobby.activity());
        createdAt.setText(ago);
        placeAddress.setText(lobby.location().address());
        placeName.setText(lobby.location().name());
        hostName.setText("Organized by " + host.name() + " (" + host.points() + " points)");

        Picasso.with(LobbyActivity.this).load(host.picture().data().url()).transform(new CircleTransform()).into(hostImage);

        adapter = new LobbyRecyclerAdapter(this, users);
        usersList.setAdapter(adapter);
        usersList.setLayoutManager(new LinearLayoutManager(LobbyActivity.this, LinearLayoutManager.HORIZONTAL, false));

        model.onUserEvent().subscribe(newUserEventTriple -> {
            adapter.updateDataset(newUserEventTriple.getLeft(), newUserEventTriple.getMiddle(), newUserEventTriple.getRight());
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Squad Details");
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener((f) -> {
            onBackPressed();
        });
    }
}
