package com.squad.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Venue {
    public abstract String id();
    @Nullable
    public abstract String description();
    @Nullable
    public abstract String url();
    public abstract String name();
    public abstract Location location();
    @Nullable
    public abstract Photos photos();
    @Nullable
    public abstract List<Category> categories();

    public static TypeAdapter<Venue> typeAdapter(Gson gson) {
        return new AutoValue_Venue.GsonTypeAdapter(gson);
    }
}
