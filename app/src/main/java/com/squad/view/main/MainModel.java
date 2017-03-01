package com.squad.view.main;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.gson.GsonBuilder;
import com.squad.GsonTypeAdapterFactory;
import com.squad.model.FacebookGraphResponse;

import rx.functions.Action1;

public class MainModel {

    public void getUser(AccessToken accessToken, Action1<FacebookGraphResponse> onUser) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, (object, response) -> {
                    FacebookGraphResponse user = new GsonBuilder()
                            .registerTypeAdapterFactory(GsonTypeAdapterFactory.create())
                            .create().fromJson(response.getRawResponse(), FacebookGraphResponse.class);
                    onUser.call(user);
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,about,picture.width(200).height(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
