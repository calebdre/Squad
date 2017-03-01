package com.squad.view.login;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.squad.model.FacebookGraphResponse;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

class LoginPresenter {

    private LoginModel model;
    private LoginView view;
    private FacebookGraphResponse user;

    LoginPresenter(LoginView loginView) {
        this.view = loginView;
        model = new LoginModel();
    }

    void bindActions() {
        checkIfUserIsLoggedIn();
        view.onFacebookLoginResult().subscribe(onFacebookLoginResult);
    }

    public FacebookGraphResponse getUser() {
        return user;
    }

    private Action1<LoginResult> onFacebookLoginResult = result -> {
        model.getUser(result.getAccessToken())
                .subscribe(user -> model.storeUser(user)
                        .subscribe((storedUser) -> {
                            this.user = storedUser;
                            view.render(LoginViewState.LOGGED_IN_NEW_USER);
                        })
                );
    };

    private void checkIfUserIsLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            model.getUser(accessToken).subscribe((user) -> {
                model.getAllUsers().flatMap(new Func1<List<FacebookGraphResponse>, Observable<FacebookGraphResponse>>() {
                    @Override
                    public Observable<FacebookGraphResponse> call(List<FacebookGraphResponse> facebookGraphResponses) {
                        return Observable.from(facebookGraphResponses);
                    }
                }).filter(response -> response.id().equals(user.id())).defaultIfEmpty(null).subscribe(queriedUser -> {
                    if (queriedUser != null) {
                        this.user = queriedUser;
                        view.render(LoginViewState.LOGGED_IN);
                    }
                });
            });
        }
    }
}
