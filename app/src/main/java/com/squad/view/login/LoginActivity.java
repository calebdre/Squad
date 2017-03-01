package com.squad.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;
import com.jakewharton.rxbinding.view.RxView;
import com.squad.ChooseActivity;
import com.squad.R;
import com.squad.facebook.SuccessFacebookCallback;
import com.squad.model.FacebookGraphResponse;
import com.squad.view.profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.login_button) LoginButton loginButton;
    @BindView(R.id.real_login_button) Button realLoginButton;

    private CallbackManager callbackManager;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(LoginActivity.this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("public_profile");

        RxView.clicks(realLoginButton).subscribe((aVoid) -> loginButton.performClick());

        presenter = new LoginPresenter(this);
        presenter.bindActions();
    }

    @Override
    public Observable<LoginResult> onFacebookLoginResult() {
        SuccessFacebookCallback callback = new SuccessFacebookCallback();
        loginButton.registerCallback(callbackManager, callback);
        return callback.getCallbackSubject();
    }

    @Override
    public void render(LoginViewState state) {
        switch (state) {
            case LOGGED_IN:
                goToChoose(presenter.getUser());
                break;
            case LOGGED_IN_NEW_USER:
                goToProfile(presenter.getUser());
                break;
            default:
                throw new IllegalArgumentException("Illegal state for LoginView");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goToProfile(FacebookGraphResponse modifiedUser) {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.putExtra(ChooseActivity.EXTRA_FB_USER, modifiedUser);
        startActivity(intent);
    }

    private void goToChoose(FacebookGraphResponse user) {
        Intent intent = new Intent(LoginActivity.this, ChooseActivity.class);
        intent.putExtra(ChooseActivity.EXTRA_FB_USER, user);
        startActivity(intent);
    }
}
