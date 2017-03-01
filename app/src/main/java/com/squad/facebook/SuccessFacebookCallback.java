package com.squad.facebook;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

public class SuccessFacebookCallback implements FacebookCallback<LoginResult>{

    private Callback callback;

    public SuccessFacebookCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        callback.onSuccess(loginResult);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    public interface Callback {
        void onSuccess(LoginResult t);
    }
}
