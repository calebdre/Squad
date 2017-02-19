package com.squad.foursquare;


import android.os.Handler;
import android.os.Looper;

import com.google.gson.GsonBuilder;
import com.squad.GsonTypeAdapterFactory;
import com.squad.foursquare.FourSquareVenuesResponse.Response.Venue;
import com.squad.foursquare.FoureSquarePhotosResponse.Response.Photos.Item;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class FourSquare {

    public void getImageForLocation(double lat, double lng, String placeAddress, Action1<String> onGetImageUrl) throws IOException {
        String SEARCH_URL = "https://api.foursquare.com/v2/venues/search";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SEARCH_URL).newBuilder();
        urlBuilder.addQueryParameter("client_id", "XVPZKBDVQJFNVPKICJPVUXUHHWE5PRZC5N2W2ZIF1TXISS42");
        urlBuilder.addQueryParameter("client_secret", "15JDWN1U4EZJTOFQPAQB1SMMTERNYIVYMB2QF33QYFHJDTJK");
        urlBuilder.addQueryParameter("v", "20170101");
        urlBuilder.addQueryParameter("ll", lat + "," + lng);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                FourSquareVenuesResponse jsonResponse = new GsonBuilder()
                        .registerTypeAdapterFactory(GsonTypeAdapterFactory.create())
                        .create().fromJson(json, FourSquareVenuesResponse.class);
                String sourceStringSplit = placeAddress.split(" ")[0];
                for (Venue venue: jsonResponse.response().venues()) {
                    if(venue.location().address() == null) {
                        continue;
                    }
                    if(sourceStringSplit.equals(venue.location().address().split(" ")[0])) {
                        getImageUrl(venue.id(), onGetImageUrl);
                    }
                }
            }
        });
    }

    private void getImageUrl(String id, Action1<String> onGetImageUrl) throws IOException {
        String urlIntermediate = "https://api.foursquare.com/v2/venues/" + id + "/photos";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(urlIntermediate).newBuilder();
        urlBuilder.addQueryParameter("client_id", "XVPZKBDVQJFNVPKICJPVUXUHHWE5PRZC5N2W2ZIF1TXISS42");
        urlBuilder.addQueryParameter("client_secret", "15JDWN1U4EZJTOFQPAQB1SMMTERNYIVYMB2QF33QYFHJDTJK");
        urlBuilder.addQueryParameter("v", "20170101");
        urlBuilder.addQueryParameter("limit", "1");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                FoureSquarePhotosResponse photosResponse = new GsonBuilder()
                        .registerTypeAdapterFactory(GsonTypeAdapterFactory.create())
                        .create().fromJson(json, FoureSquarePhotosResponse.class);
                List<Item> items = photosResponse.response().photos().items();
                if (items.size() > 0) {
                    Item item = items.get(0);

                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    Runnable myRunnable = () -> onGetImageUrl.call(item.prefix() + "600x600" + item.suffix());
                    mainHandler.post(myRunnable);
                }
            }
        });
    }
}
