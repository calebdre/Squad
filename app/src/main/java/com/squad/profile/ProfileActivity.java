package com.squad.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squad.ChooseActivity;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squad.ChooseActivity.EXTRA_FB_USER;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_toolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView image;
    @BindView(R.id.profile_name) TextView name;
    @BindView(R.id.profile_blurb_text) TextInputEditText blurb;
    @BindView(R.id.profile_submit_button) FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Create your profile");

        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getExtras().getSerializable(EXTRA_FB_USER);
        Picasso.with(this).load(user.picture().data().url()).transform(new CircleTransform()).into(image);
        name.setText(user.name());
        if(user.about() != null) {
            blurb.setText(user.about());
        }

        RxView.clicks(fab).subscribe((aVoid) -> {
            Intent intent = new Intent(this, ChooseActivity.class);
            FacebookGraphResponse updatedUser = FacebookGraphResponse.builder()
                    .about(blurb.getText().toString())
                    .fbId(user.fbId())
                    .name(user.name())
                    .id(user.id())
                    .picture(user.picture())
                    .build();

            intent.putExtra(EXTRA_FB_USER, updatedUser);
            startActivity(intent);
        });
    }
}
