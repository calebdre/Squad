package com.squad.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import rx.Observable;

public class LocationHelper {

    public static Observable<Location> getUserLocation(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        return Observable.create(subscriber -> {
            //noinspection MissingPermission
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    subscriber.onNext(location);
                    subscriber.onCompleted();
                    subscriber.unsubscribe();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            }, activity.getMainLooper());
        });
    }
}
