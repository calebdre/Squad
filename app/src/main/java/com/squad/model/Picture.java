package com.squad.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue public abstract class Picture implements Serializable {

    public abstract Data data();

    public static TypeAdapter<Picture> typeAdapter(Gson gson) {
        return new AutoValue_Picture.GsonTypeAdapter(gson);
    }

    public Object toFirebaseValue() {
        return new AutoValue_Picture.FirebaseValue(this);
    }
}
