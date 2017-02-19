package com.squad.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Lobby implements Serializable {
    public abstract String name();
    @SerializedName("created_at")
    public abstract long createdAt();
    public abstract String activity();
    public abstract String firebaseKey();
    public abstract MeetupLocation location();
    public abstract FacebookGraphResponse host();

    public static Builder builder() {
        return new AutoValue_Lobby.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder name(String name);
        public abstract Builder createdAt(long createdAt);
        public abstract Builder firebaseKey(String key);
        public abstract Builder activity(String activity);
        public abstract Builder location(MeetupLocation location);
        public abstract Builder host(FacebookGraphResponse host);

        public abstract Lobby build();
    }
}
