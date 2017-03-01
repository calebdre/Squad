package com.squad.view.main;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.GsonBuilder;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.squad.GsonTypeAdapterFactory;
import com.squad.model.FacebookGraphResponse;
import com.squad.util.FirebaseToListMapper;

import java.util.List;

import rx.Observable;

public class MainModel {

    private static final String USER_REFERENCE_STRING = "/users";
    private DatabaseReference userReference;
    private FirebaseDatabase firebaseDatabase;

    public MainModel() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference(USER_REFERENCE_STRING);
    }

    public Observable<FacebookGraphResponse> getUser(AccessToken accessToken) {
        return Observable.create(subscriber -> {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, (object, response) -> {
                        FacebookGraphResponse user = new GsonBuilder()
                                .registerTypeAdapterFactory(GsonTypeAdapterFactory.create())
                                .create().fromJson(response.getRawResponse(), FacebookGraphResponse.class);
                        subscriber.onNext(user);
                        subscriber.unsubscribe();
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,about,picture.width(200).height(200)");
            request.setParameters(parameters);
            request.executeAsync();
        });
    }

    public Observable<List<FacebookGraphResponse>> getAllUsers() {
        return RxFirebaseDatabase.observeSingleValueEvent(userReference, new FirebaseToListMapper<>(FacebookGraphResponse::create).map());
    }

    public Observable<FacebookGraphResponse> storeUser(FacebookGraphResponse user) {
        return Observable.create(subscriber -> {
            DatabaseReference push = userReference.push();
            FacebookGraphResponse modifiedUser = FacebookGraphResponse.addFBId(user, push.getKey());
            push.setValue(modifiedUser.toFirebaseValue());
            userReference.push().setValue(user);

            subscriber.onNext(modifiedUser);
            subscriber.unsubscribe();
        });
    }
}
