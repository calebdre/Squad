package com.squad.model;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Lobby implements Serializable {
    public abstract String name();
    public abstract String id();
    @SerializedName("created_at")
    public abstract long createdAt();
    public abstract String activity();
    public abstract String firebaseKey();
    public abstract MeetupLocation location();
    public abstract boolean ready();
    public abstract String host();
    public abstract Map<String, FacebookGraphResponse> users();

    public static Lobby create(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(AutoValue_Lobby.FirebaseValue.class).toAutoValue();
    }

    public static TypeAdapter<Lobby> typeAdapter(Gson gson) {
        return new AutoValue_Lobby.GsonTypeAdapter(gson);
    }

    public Object toFirebaseValue() {
        return new AutoValue_Lobby.FirebaseValue(this);
    }

    public static Builder builder() {
        return new AutoValue_Lobby.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder name(String name);
        public abstract Builder id(String id);
        public abstract Builder ready(boolean ready);
        public abstract Builder createdAt(long createdAt);
        public abstract Builder firebaseKey(String key);
        public abstract Builder activity(String activity);
        public abstract Builder location(MeetupLocation location);
        public abstract Builder host(String host);
        public abstract Builder users(Map<String, FacebookGraphResponse> users);

        public abstract Lobby build();
    }
}
