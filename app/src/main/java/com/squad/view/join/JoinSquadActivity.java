package com.squad.view.join;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.util.LocationHelper;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.lobby.LobbyActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squad.view.ChooseActivity.EXTRA_FB_USER;
import static com.squad.view.create.CreateSquadActivity.EXTRA_USER_IS_HOST;
import static com.squad.view.lobby.LobbyActivity.EXTRA_LOBBY_KEY;

public class JoinSquadActivity extends AppCompatActivity implements JoinSquadView {

    @BindView(R.id.join_squad_list) RecyclerView recyclerView;
    private Location userGPSCoords;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_squad);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting location...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        setLocation();
    }

    @Override
    public void setupView(List<LobbyUiItem> lobbyUiItems) {
        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getSerializableExtra(EXTRA_FB_USER);
        JoinSquadRecyclerAdapter adapter = new JoinSquadRecyclerAdapter(this, lobbyUiItems, getLocation());
        recyclerView.setLayoutManager(new LinearLayoutManager(JoinSquadActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.onLobbySelect().subscribe((id) -> {
            Intent intent = new Intent(this, LobbyActivity.class);
            intent.putExtra(EXTRA_USER_IS_HOST, false);
            intent.putExtra(EXTRA_LOBBY_KEY, id);
            intent.putExtra(EXTRA_FB_USER, user);
            startActivity(intent);
        });
    }

    @Override
    public Location getLocation() {
        return userGPSCoords;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocation();
                } else {
                    // TODO: probably should do something here
                }
                return;
            }
        }
    }

    private void setLocation() {
        LocationHelper.getUserLocation(this)
                .subscribe(location -> {
                    userGPSCoords = location;
                    new JoinSquadPresenter(JoinSquadActivity.this);
                    progressDialog.dismiss();
                });
    }
}
