package com.behague.benjamin.go_4_lunch.utils;

import com.behague.benjamin.go_4_lunch.models.objects.PlaceObject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Benjamin BEHAGUE on 02/04/2018.
 */

public interface GooglePlacesService {

    @GET("maps/api/place/nearbysearch/json?key=AIzaSyDB5L8Lln_r8PvxKp43aqBoAAVPfYzSLV4")
    Observable<PlaceObject> getPlaces(@Query("location") String location, @Query("radius") int radius,
                                      @Query("type") String type);

    // ex : location = 44.448151,1.441430

    // OK : https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyApi61iqZP6ZR-mGkYvZkTSLH7OskLQJj0&location=43.6780742,4.0978829&radius=1500&type=restaurant
    //!OK : https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyApi61iqZP6ZR-mGkYvZkTSLH7OskLQJj0&location=43.6780742,4.0978829&radius=2000&type=restaurant
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
