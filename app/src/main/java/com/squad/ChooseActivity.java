package com.squad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.squad.view.create.CreateSquadActivity;
import com.squad.view.join.JoinSquadActivity;
import com.squad.model.FacebookGraphResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseActivity extends AppCompatActivity {

    public static final String EXTRA_FB_USER = "extra_fb_user";

    @BindView(R.id.choose_create_group) LinearLayout createGroupButton;
    @BindView(R.id.choose_join_group) LinearLayout  joinGroupButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ButterKnife.bind(this);

        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getSerializableExtra(EXTRA_FB_USER);
        RxView.clicks(createGroupButton).subscribe((aVoid) -> {
            Intent intent = new Intent(this, CreateSquadActivity.class);
            intent.putExtra(EXTRA_FB_USER, user);
            startActivity(intent);
        });

        RxView.clicks(joinGroupButton).subscribe((aVoid) -> {
            Intent intent = new Intent(this, JoinSquadActivity.class);
            intent.putExtra(EXTRA_FB_USER, user);
            startActivity(intent);
        });
    }
}
