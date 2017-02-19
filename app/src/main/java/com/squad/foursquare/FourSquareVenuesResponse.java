package com.squad.foursquare;


import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class FourSquareVenuesResponse {

    public abstract Response response();

    @AutoValue
    public static abstract class Response {
        public abstract List<Venue> venues();

        @AutoValue
        public static abstract class Venue {
            public abstract String id();
            public abstract String name();
            public abstract Location location();

            @AutoValue
            public static abstract class Location {

                @Nullable
                public abstract String address();
                public abstract double lat();
                public abstract double lng();

                public static TypeAdapter<Location> typeAdapter(Gson gson) {
                    return new AutoValue_FourSquareVenuesResponse_Response_Venue_Location.GsonTypeAdapter(gson);
                }
            }

            public static TypeAdapter<Venue> typeAdapter(Gson gson) {
                return new AutoValue_FourSquareVenuesResponse_Response_Venue.GsonTypeAdapter(gson);
            }
        }

        public static TypeAdapter<Response> typeAdapter(Gson gson) {
            return new AutoValue_FourSquareVenuesResponse_Response.GsonTypeAdapter(gson);
        }
    }

    public static TypeAdapter<FourSquareVenuesResponse> typeAdapter(Gson gson) {
        return new AutoValue_FourSquareVenuesResponse.GsonTypeAdapter(gson);
    }
}
