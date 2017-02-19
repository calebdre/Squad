package com.squad.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.ChooseActivity;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squad.lobby.LobbyActivity.EXTRA_LOBBY_KEY;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_messages) RecyclerView messagesList;
    @BindView(R.id.chat_message_entry) EditText messageEntry;
    @BindView(R.id.chat_send_message_button) Button submitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        String key = getIntent().getExtras().getString(EXTRA_LOBBY_KEY);
        FacebookGraphResponse user = (FacebookGraphResponse) getIntent().getExtras().getSerializable(ChooseActivity.EXTRA_FB_USER);

        ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(key, user.fbId());
        messagesList.setLayoutManager(new LinearLayoutManager(this));
        messagesList.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("lobbies/" + key + "/messages");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messagesList.scrollToPosition(messagesList.getAdapter().getItemCount() - 1);
                messageEntry.setText("");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RxView.clicks(submitButton).subscribe((aVoid) -> {
            ChatMessage chatMessage = ChatMessage.builder()
                    .message(messageEntry.getText().toString())
                    .name(user.name())
                    .userId(user.fbId())
                    .time(Calendar.getInstance().getTimeInMillis())
                    .build();

            reference.push()
                    .setValue(chatMessage.toFirebaseValue());
        });
    }
}
