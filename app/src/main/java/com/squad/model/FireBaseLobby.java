package com.squad.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class FireBaseLobby {

    @Nullable
    public abstract String activity();
    public abstract long created();
    public abstract String name();
    public abstract String host();
    public abstract MeetupLocation location();

    public static FireBaseLobby create(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(AutoValue_FireBaseLobby.FirebaseValue.class).toAutoValue();
    }
}
