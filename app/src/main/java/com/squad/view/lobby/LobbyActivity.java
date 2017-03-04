package com.squad.view.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.dashboard.DashboardActivity;
import com.squad.view.profile.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.squad.view.ChooseActivity.EXTRA_FB_USER;
import static com.squad.view.create.CreateSquadActivity.EXTRA_USER_IS_HOST;

public class LobbyActivity extends AppCompatActivity implements LobbyView {

    public static final java.lang.String EXTRA_LOBBY_KEY = "extra_lobby_key";

    @BindView(R.id.lobby_toolbar) Toolbar toolbar;
    @BindView(R.id.lobby_preview_text_area) LinearLayout previewTextArea;
    @BindView(R.id.lobby_preview_button_area) LinearLayout previewButtonArea;
    @BindView(R.id.lobby_members_count) TextView memberCount;
    @BindView(R.id.lobby_item_image) ImageView lobbyImage;
    @BindView(R.id.lobby_host_image) ImageView hostImage;
    @BindView(R.id.lobby_activity) TextView activity;
    @BindView(R.id.lobby_created_at) TextView createdAt;
    @BindView(R.id.lobby_host_name) TextView hostName;
    @BindView(R.id.lobby_place_address) TextView placeAddress;
    @BindView(R.id.lobby_place_name) TextView placeName;
    @BindView(R.id.lobby_users) RecyclerView usersList;
    @BindView(R.id.lobby_join_squad_button) Button joinSquadButton;

    private List<UserUIItem> users = new ArrayList<>();
    private LobbyRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ButterKnife.bind(this);

        setupToolbar();

        Bundle extras = getIntent().getExtras();

        String key = extras.getString(EXTRA_LOBBY_KEY);
        boolean isHost = extras.getBoolean(EXTRA_USER_IS_HOST);

        LobbyPresenter presenter = new LobbyPresenter(key, this);
        presenter.bindActions();

        if (isHost) {
            previewButtonArea.setVisibility(View.GONE);
            previewTextArea.setVisibility(View.GONE);
        }
    }

    @Override
    public void addUserToLobby(UserUIItem userUIItem) {
        users.add(userUIItem);
        adapter.notifyItemInserted(users.size() - 2);
    }

    @Override
    public void removeUserFromLobby(UserUIItem userUIItem) {
        for (int i = 0; i < users.size(); i++) {
            UserUIItem userItem = users.get(i);
            if (userItem.pictureUrl().equals(userUIItem.pictureUrl())) {
                users.remove(i);
                adapter.notifyItemRemoved(i);
            }
        }
    }

    @Override
    public void setupView(UserUIItem hostUIItem, LobbyUiItem lobbyUiItem) {
        activity.setText(lobbyUiItem.activity());
        createdAt.setText(lobbyUiItem.timeSinceCreate());
        placeAddress.setText(lobbyUiItem.address());
        placeName.setText(lobbyUiItem.placeName());
        hostName.setText("Organized by " + hostUIItem.name());

        Picasso.with(LobbyActivity.this).load(hostUIItem.pictureUrl()).transform(new CircleTransform()).into(hostImage);

        adapter = new LobbyRecyclerAdapter(this, users);
        usersList.setAdapter(adapter);
        usersList.setLayoutManager(new LinearLayoutManager(LobbyActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void goToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(EXTRA_FB_USER, getIntent().getExtras().getSerializable(EXTRA_FB_USER));
        startActivity(intent);
    }

    @Override
    public void transformToJoinedLobby() {

    }

    @Override
    public void renderVenueImage(String url) {
        Picasso.with(this).load(url).into(lobbyImage);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Squad Details");
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener((f) -> onBackPressed());
    }

    @Override
    public Observable<FacebookGraphResponse> joinSquadClicks() {
        return RxView.clicks(joinSquadButton)
                .map(aVoid -> (FacebookGraphResponse) getIntent().getExtras().getSerializable(EXTRA_FB_USER));
    }

    @Override
    public Observable<Void> startSquadClicks() {
        return Observable.empty();
    }
}