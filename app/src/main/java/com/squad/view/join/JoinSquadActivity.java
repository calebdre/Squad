package com.squad.view.join;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.model.Lobby;
import com.squad.view.lobby.LobbyActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squad.view.ChooseActivity.EXTRA_FB_USER;
import static com.squad.view.lobby.LobbyActivity.EXTRA_LOBBY_KEY;

public class JoinSquadActivity extends AppCompatActivity {

    @BindView(R.id.join_squad_list) RecyclerView recyclerView;
    private JoinSquadModel model;
    public JoinSquadActivity() {
        model = new JoinSquadModel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_squad);
        ButterKnife.bind(this);

        model.getLobbies().subscribe(this::init);
    }

    private void init(List<Lobby> lobbies) {
        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getSerializableExtra(EXTRA_FB_USER);
        JoinSquadRecyclerAdapter adapter = new JoinSquadRecyclerAdapter(this, lobbies);
        recyclerView.setLayoutManager(new LinearLayoutManager(JoinSquadActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.onLobbySelect().subscribe((id) -> {
            FirebaseDatabase.getInstance()
                    .getReference("lobbies/" +id +"/users/")
                    .push()
                    .setValue(user.toFirebaseValue());
            Intent intent = new Intent(this, LobbyActivity.class);
            intent.putExtra(EXTRA_LOBBY_KEY, id);
            intent.putExtra(EXTRA_FB_USER, user);
            startActivity(intent);
        });
    }
}
