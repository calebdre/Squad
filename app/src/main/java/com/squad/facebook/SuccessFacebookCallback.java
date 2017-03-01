package com.squad.facebook;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import rx.subjects.PublishSubject;

public class SuccessFacebookCallback implements FacebookCallback<LoginResult>{

    private PublishSubject<LoginResult> callback = PublishSubject.create();

    public PublishSubject<LoginResult> getCallbackSubject() {
        return callback;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        callback.onNext(loginResult);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
        callback.onError(error);
    }
}
