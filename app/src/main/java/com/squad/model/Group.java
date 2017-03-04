package com.squad.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Group {
    public abstract String type();
    public abstract List<Item> items();

    public static TypeAdapter<Group> typeAdapter(Gson gson) {
        return new AutoValue_Group.GsonTypeAdapter(gson);
    }
}
