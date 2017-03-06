package com.squad.model;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue
@FirebaseValue
public abstract class Category {

    public abstract String name();
    public abstract String pluralName();
    public abstract String shortName();

    public static TypeAdapter<Category> typeAdapter(Gson gson) {
        return new AutoValue_Category.GsonTypeAdapter(gson);
    }
}
