package com.squad.api.foursquare;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.squad.model.Venue;

import java.util.List;

@AutoValue
public abstract class FoureSquareMiniVenuesResponse {

    public abstract Response response();

    @AutoValue
    public static abstract class Response {
        @SerializedName("minivenues")
        public abstract List<Venue> venues();
        public static TypeAdapter<Response> typeAdapter(Gson gson) {
            return new AutoValue_FoureSquareMiniVenuesResponse_Response.GsonTypeAdapter(gson);
        }
    }

    public static TypeAdapter<FoureSquareMiniVenuesResponse> typeAdapter(Gson gson) {
        return new AutoValue_FoureSquareMiniVenuesResponse.GsonTypeAdapter(gson);
    }
}
