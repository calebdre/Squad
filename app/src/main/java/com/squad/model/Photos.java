package com.squad.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Photos {

    public abstract List<Group> groups();

    public static TypeAdapter<Photos> typeAdapter(Gson gson) {
        return new AutoValue_Photos.GsonTypeAdapter(gson);
    }
}
