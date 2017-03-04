package com.squad.api.foursquare;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squad.model.Item;

import java.util.List;

@AutoValue
public abstract class FoureSquarePhotosResponse {

    public abstract Response response();

    @AutoValue
    public static abstract class Response {
        public abstract Photos photos();

        @AutoValue
        public static abstract class Photos {

            public abstract int count();
            public abstract List<Item> items();

            public static TypeAdapter<Photos> typeAdapter(Gson gson) {
                return new AutoValue_FoureSquarePhotosResponse_Response_Photos.GsonTypeAdapter(gson);
            }
        }

        public static TypeAdapter<Response> typeAdapter(Gson gson) {
            return new AutoValue_FoureSquarePhotosResponse_Response.GsonTypeAdapter(gson);
        }
    }

    public static TypeAdapter<FoureSquarePhotosResponse> typeAdapter(Gson gson) {
        return new AutoValue_FoureSquarePhotosResponse.GsonTypeAdapter(gson);
    }
}
