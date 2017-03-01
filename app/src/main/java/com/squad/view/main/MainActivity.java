package com.squad.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.ChooseActivity;
import com.squad.R;
import com.squad.facebook.SuccessFacebookCallback;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.profile.ProfileActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_button) LoginButton loginButton;
    @BindView(R.id.real_login_button) Button realLoginButton;

    CallbackManager callbackManager;
    MainModel model;

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
            model.getUser(accessToken).subscribe((user) -> {
                model.getAllUsers()
                        .flatMap(new Func1<List<FacebookGraphResponse>, Observable<FacebookGraphResponse>>() {
                            @Override
                            public Observable<FacebookGraphResponse> call(List<FacebookGraphResponse> facebookGraphResponses) {
                                return Observable.from(facebookGraphResponses);
                            }
                        })
                        .filter(response -> response.id().equals(user.id()))
                        .defaultIfEmpty(null)
                        .subscribe( queriedUser -> {
                           if(queriedUser != null) {
                               goToChoose(queriedUser);
                           }
                        });
            });
        }

        RxView.clicks(realLoginButton).subscribe((aVoid) -> {
            loginButton.performClick();
        });

        loginButton.registerCallback(callbackManager, new SuccessFacebookCallback(result -> {
            model.getUser(result.getAccessToken()).subscribe(
                    user -> model.storeUser(user).subscribe(this::goToProfile));
        }));
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
