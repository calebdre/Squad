package com.squad.model;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class MeetupLocation implements Serializable {

    public abstract String address();
    public abstract String name();
    public abstract double lat();
    public abstract double lng();

    public static Builder builder() {
        return new AutoValue_MeetupLocation.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder address(String address);
        public abstract Builder name(String name);
        public abstract Builder lat(double lat);
        public abstract Builder lng(double lng);

        public abstract MeetupLocation build();
    }

    public Object toFirebaseValue() {
        return new AutoValue_MeetupLocation.FirebaseValue(this);
    }
}
