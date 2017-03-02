package com.squad.api.foursquare;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squad.model.Venue;

import java.util.List;

@AutoValue
public abstract class FourSquareVenuesResponse {

    public abstract Response response();

    @AutoValue
    public static abstract class Response {
        public abstract List<Venue> venues();
        public static TypeAdapter<Response> typeAdapter(Gson gson) {
            return new AutoValue_FourSquareVenuesResponse_Response.GsonTypeAdapter(gson);
        }
    }

    public static TypeAdapter<FourSquareVenuesResponse> typeAdapter(Gson gson) {
        return new AutoValue_FourSquareVenuesResponse.GsonTypeAdapter(gson);
    }
}
