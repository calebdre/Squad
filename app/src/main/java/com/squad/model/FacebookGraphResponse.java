package com.squad.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue
@FirebaseValue
public abstract class FacebookGraphResponse implements Serializable {

    public abstract String name();
    public abstract String id();

    @Nullable
    public abstract String about();
    public abstract Picture picture();

    @Nullable
    public abstract String fbId();

    public static FacebookGraphResponse addFBId(FacebookGraphResponse response, String fbId) {
        return FacebookGraphResponse.builder()
                .about(response.about())
                .id(response.id())
                .name(response.name())
                .picture(response.picture())
                .fbId(fbId)
                .build();
    }

    public static FacebookGraphResponse create(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(AutoValue_FacebookGraphResponse.FirebaseValue.class).toAutoValue();
    }

    public static TypeAdapter<FacebookGraphResponse> typeAdapter(Gson gson) {
        return new AutoValue_FacebookGraphResponse.GsonTypeAdapter(gson);
    }

    public Object toFirebaseValue() {
        return new AutoValue_FacebookGraphResponse.FirebaseValue(this);
    }


    public static Builder builder() {
        return new AutoValue_FacebookGraphResponse.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder name(String name);
        public abstract Builder id(String createdAt);
        public abstract Builder about(String key);
        public abstract Builder picture(Picture activity);
        public abstract Builder fbId(String location);

        public abstract FacebookGraphResponse build();
    }

}
