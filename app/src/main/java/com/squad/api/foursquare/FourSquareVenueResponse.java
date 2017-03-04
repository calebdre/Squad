package com.squad.api.foursquare;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squad.model.Venue;

@AutoValue
public abstract class FourSquareVenueResponse {

    public abstract Response response();

    @AutoValue
    public static abstract class Response {
        public abstract Venue venue();

        public static TypeAdapter<Response> typeAdapter(Gson gson) {
            return new AutoValue_FourSquareVenueResponse_Response.GsonTypeAdapter(gson);
        }
    }

    public static TypeAdapter<FourSquareVenueResponse> typeAdapter(Gson gson) {
        return new AutoValue_FourSquareVenueResponse.GsonTypeAdapter(gson);
    }
}
