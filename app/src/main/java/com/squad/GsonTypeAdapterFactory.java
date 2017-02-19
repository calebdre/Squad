package com.squad;

import com.google.gson.TypeAdapterFactory;

@com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
public abstract class GsonTypeAdapterFactory  implements TypeAdapterFactory {

    // Static factory method to access the package
    // private generated implementation
    public static TypeAdapterFactory create() {
        return new AutoValueGson_GsonTypeAdapterFactory ();
    }

}