package com.squad.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.R;
import com.squad.lobby.LobbyActivity;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.model.MeetupLocation;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.atrox.haikunator.HaikunatorBuilder;

import static com.squad.ChooseActivity.EXTRA_FB_USER;
import static com.squad.lobby.LobbyActivity.EXTRA_LOBBY_KEY;

public class CreateSquadActivity extends AppCompatActivity {

    private static final String TAG = "WHOEVER";
    public static final String EXTRA_MEETUP_LOCATION = "extra_meetup_location";
    public static final String EXTRA_LOBBY = "extra_lobby";

    @BindView(R.id.create_squad_activity_input) AutoCompleteTextView activityInput;
    @BindView(R.id.create_squad_toolbar) Toolbar toolbar;
    @BindView(R.id.create_squad_place_input) Button placeInput;
    @BindView(R.id.create_squad_name) TextView squadNameView;
    @BindView(R.id.create_squad_place_name) TextView placeName;
    @BindView(R.id.create_squad_name_prefix) TextView placePrefix;
    @BindView(R.id.create_squad_submit_button) FloatingActionButton submitButton;

    private MeetupLocation location;

    private static final String[] ACTIVITIES = new String[] {
            "Play put put", "Swim", "Go out to eat", "Watch GoT", "Workout", "Taking a long drive", "Movie night", "Dancing", "DnD",
            "Going to a conference", "PantherHackers Workshop", "Winning a hackathon"
    };
    private String squadName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_squad);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Start a Squad");
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener((aVoid) -> {
            onBackPressed();
        });

        squadName = new HaikunatorBuilder().setTokenLength(0).setDelimiter(" ").build().haikunate();
        squadNameView.setText(squadName );
        placeInput.setKeyListener(null);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, ACTIVITIES);
        activityInput.setAdapter(adapter);

        RxView.clicks(placeInput).subscribe((aVoid) -> {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, 1);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
        });

        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getSerializableExtra(EXTRA_FB_USER);

        RxView.clicks(submitButton).subscribe((aVoid) -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("lobbies");
            DatabaseReference push = reference.push();
            long timeStamp = Calendar.getInstance().getTimeInMillis();
            Lobby lobby = Lobby.builder()
                    .name(squadName)
                    .firebaseKey(push.getKey())
                    .createdAt(timeStamp)
                    .location(location)
                    .activity(activityInput.getText().toString())
                    .host(user)
                    .build();

            push.child("name").setValue(squadName);
            push.child("created").setValue(timeStamp);
            push.child("location").setValue(location.toFirebaseValue());
            push.child("activity").setValue(activityInput.getText().toString());
            push.child("users").push().setValue(user.toFirebaseValue());
            push.child("host").setValue(user.fbId());
            push.child("ready").setValue(false);

            Intent intent = new Intent(this, LobbyActivity.class);
            intent.putExtra(EXTRA_MEETUP_LOCATION, location);
            intent.putExtra(EXTRA_FB_USER, user);
            intent.putExtra(EXTRA_LOBBY, lobby);
            intent.putExtra(EXTRA_LOBBY_KEY, push.getKey());
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                location = MeetupLocation.builder()
                        .address(place.getAddress().toString())
                        .name(place.getName().toString())
                        .lat(place.getLatLng().latitude)
                        .lng(place.getLatLng().longitude)
                        .build();

                placeName.setText(location.name());
                placeName.setVisibility(View.VISIBLE);
                placePrefix.setVisibility(View.VISIBLE);
                placeInput.setText("Select a different location to meet");

                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
