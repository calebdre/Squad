package com.squad.chat;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class ChatMessage {

    public abstract String name();
    public abstract String message();
    public abstract long time();

    public static TypeAdapter<ChatMessage> typeAdapter(Gson gson) {
        return new AutoValue_ChatMessage.GsonTypeAdapter(gson);
    }

    public Object toFirebaseValue() {
        return new AutoValue_ChatMessage.FirebaseValue(this);
    }

    public static ChatMessage create(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(AutoValue_ChatMessage.FirebaseValue.class).toAutoValue();
    }

    public static Builder builder() {
        return new AutoValue_ChatMessage.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder name(String name);
        public abstract Builder message(String message);
        public abstract Builder time(long key);

        public abstract ChatMessage build();
    }
}
