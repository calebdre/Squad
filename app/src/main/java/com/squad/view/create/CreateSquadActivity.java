package com.squad.view.create;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.helpers.ui_items.LobbyUiItem;
import com.squad.view.helpers.ui_items.VenueUiItem;
import com.squad.view.lobby.LobbyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.atrox.haikunator.HaikunatorBuilder;
import rx.Observable;

import static com.squad.view.ChooseActivity.EXTRA_FB_USER;
import static com.squad.view.lobby.LobbyActivity.EXTRA_LOBBY_KEY;

public class CreateSquadActivity extends AppCompatActivity implements CreateSquadView {

    public static final String EXTRA_USER_IS_HOST = "extra_user_is_host";

    private static final String[] ACTIVITIES = new String[]{"Play put put", "Swim", "Go out to eat", "Watch GoT", "Workout", "Taking a long drive", "Movie night", "Dancing", "DnD", "Going to a conference", "PantherHackers Workshop", "Winning a hackathon"};

    @BindView(R.id.create_squad_activity_input) AutoCompleteTextView activityInput;
    @BindView(R.id.create_squad_toolbar) Toolbar toolbar;
    @BindView(R.id.create_squad_place_input) AutoCompleteTextView placeInput;
    @BindView(R.id.create_squad_name) TextView squadNameView;
    @BindView(R.id.create_squad_place_meta_container) LinearLayout placeMetaContainer;
    @BindView(R.id.create_squad_place_name) TextView placeName;
    @BindView(R.id.create_squad_place_distance) TextView placeDistance;
    @BindView(R.id.create_squad_name_prefix) TextView placePrefix;
    @BindView(R.id.create_squad_submit_button) FloatingActionButton submitButton;

    private List<VenueUiItem> venues = new ArrayList<>();
    private Location userGPSCoords;
    private VenueUiItem selectedVenue;
    private String squadName;
    private PlaceAutocompleteAdapter placeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_squad);
        ButterKnife.bind(this);

        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getSerializableExtra(EXTRA_FB_USER);
        CreateSquadPresenter presenter = new CreateSquadPresenter(this, user);
        presenter.bindEvents();

        squadName = new HaikunatorBuilder().setTokenLength(0).setDelimiter(" ").build().haikunate();
        squadNameView.setText(squadName);

        setupToolbar();
        setupAdapters();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public Location getLocation() {
        if (userGPSCoords == null) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //noinspection MissingPermission
            userGPSCoords = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        return userGPSCoords;
    }

    @Override
    public void setSelectedVenue(VenueUiItem selectedVenue) {
        if (selectedVenue.address() != null) {
            placeInput.setText(selectedVenue.address());
        } else {
            placeInput.setText(selectedVenue.name());
        }

        placeName.setText(selectedVenue.name());
        placeDistance.setText(selectedVenue.distanceFrom(userGPSCoords) + "km");

        placeMetaContainer.setVisibility(View.VISIBLE);

        this.selectedVenue = selectedVenue;
    }

    @Override
    public void showError(String networkError) {
        Snackbar.make(toolbar, networkError, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void goToLobby(LobbyUiItem lobby) {
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra(EXTRA_USER_IS_HOST, true);
        intent.putExtra(EXTRA_FB_USER, getIntent().getSerializableExtra(EXTRA_FB_USER));
        intent.putExtra(EXTRA_LOBBY_KEY, lobby.id());
        startActivity(intent);
    }

    @Override
    public void updateAutocomplete(List<VenueUiItem> newVenues) {
        venues.clear();
        venues.addAll(newVenues);
        placeAdapter.notifyDataSetChanged();
    }

    @Override
    public Observable<LobbyData> onSquadSubmit() {
        return RxView.clicks(submitButton).doOnNext(aVoid -> {
            if (selectedVenue == null) {
                placeInput.setError("Please select a venue from the dropdown.");
                return;
            } else {
                placeInput.setError(null);
            }

            if (activityInput.getText().length() == 0) {
                activityInput.setError("Please type an activity.");
            } else {
                activityInput.setError(null);
            }
        }).filter(aVoid -> selectedVenue != null && activityInput.getText().length() != 0)
        .map(aVoid -> new LobbyData(activityInput.getText().toString(), squadName, selectedVenue.id()));
    }

    @Override
    public Observable<String> onEnterLocationText() {
        return RxTextView.afterTextChangeEvents(placeInput)
                .debounce(100, TimeUnit.MILLISECONDS)
                .map(event -> placeInput.getText().toString())
                .filter(text -> text.length() > 2);
    }

    @Override
    public Observable<VenueUiItem> onSelectedVenue() {
        return RxAutoCompleteTextView.itemClickEvents(placeInput)
                .map(event -> venues.get(event.position()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //noinspection MissingPermission
                    userGPSCoords = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else {
                    // TODO: probably should do something here
                }
                return;
            }
        }
    }

    private void setupAdapters() {
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, ACTIVITIES);
        placeAdapter = new PlaceAutocompleteAdapter(this, venues);

        placeInput.setAdapter(placeAdapter);
        activityInput.setAdapter(activityAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Start a Squad");
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener((aVoid) -> {
            onBackPressed();
        });
    }
}
