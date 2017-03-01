package com.squad.view.login;

import com.facebook.login.LoginResult;

import rx.Observable;

interface LoginView {
    Observable<LoginResult> onFacebookLoginResult();
    void render(LoginViewState state);
}
