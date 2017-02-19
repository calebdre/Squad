package com.squad.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue public abstract class Data implements Serializable {

    @SerializedName("is_silhouette")
    public abstract boolean isSilhouette();

    public abstract String url();

    public static TypeAdapter<Data> typeAdapter(Gson gson) {
        return new AutoValue_Data.GsonTypeAdapter(gson);
    }

    public Object toFirebaseValue() {
        return new AutoValue_Data.FirebaseValue(this);
    }
}
