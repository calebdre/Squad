package com.squad.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.ChooseActivity;
import com.squad.R;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_button) LoginButton loginButton;
    @BindView(R.id.real_login_button) Button realLoginButton;

    CallbackManager callbackManager;
    MainModel model;
    private ChildEventListener searchUserListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        model = new MainModel();
        FirebaseApp.initializeApp(MainActivity.this);
        loginButton.setReadPermissions("public_profile");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null) {
            model.getUser(accessToken, (user) -> {
                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
                searchUserListener = userReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FacebookGraphResponse queriedUser = FacebookGraphResponse.create(dataSnapshot);
                        if (user.id().equals(queriedUser.id())) {
                            userReference.removeEventListener(searchUserListener);
                            goToChoose(queriedUser);
                        }
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
            });
        }

        RxView.clicks(realLoginButton).subscribe((aVoid) -> {
            loginButton.performClick();
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                model.getUser(loginResult.getAccessToken(), (user) -> {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");
                    DatabaseReference push = myRef.push();
                    FacebookGraphResponse modifiedUser = FacebookGraphResponse.addFBId(user, push.getKey());
                    database.getReference("users/" + push.getKey() + "/points").setValue(0);
                    push.setValue(modifiedUser.toFirebaseValue());
                    goToProfile(modifiedUser);
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void goToProfile(FacebookGraphResponse modifiedUser) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra(ChooseActivity.EXTRA_FB_USER, modifiedUser);
        startActivity(intent);
    }

    private void goToChoose(FacebookGraphResponse user) {
        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
        intent.putExtra(ChooseActivity.EXTRA_FB_USER, user);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
