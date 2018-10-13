package com.behague.benjamin.go_4_lunch.utils;

import com.behague.benjamin.go_4_lunch.models.objects.Details.Details;
import com.behague.benjamin.go_4_lunch.models.objects.Metrix.Metrix;
import com.behague.benjamin.go_4_lunch.models.objects.Places.PlaceObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Benjamin BEHAGUE on 02/04/2018.
 */

public class GooglePlaceStream {

    public static Observable<PlaceObject> streamPlaces(String location, int radius, String type){
        GooglePlacesService googlePlacesService = GooglePlacesService.retrofit.create(GooglePlacesService.class);

        return googlePlacesService.getPlaces(location, radius, type)
                .subscribeOn(Schedulers.io()) //Execute stream in thread Scheduler.io
                .observeOn(AndroidSchedulers.mainThread()) //Listen stream observable on main thread
                .timeout(500, TimeUnit.SECONDS); //Time for send data
    }

    public static Observable<Metrix> streamMetrix(String origins, String destinations){
        GooglePlacesService googleMetrixService = GooglePlacesService.retrofit.create(GooglePlacesService.class);

        return googleMetrixService.getMetrix(origins, destinations)
                .subscribeOn(Schedulers.io()) //Execute stream in thread Scheduler.io
                .observeOn(AndroidSchedulers.mainThread()) //Listen stream observable on main thread
                .timeout(500, TimeUnit.SECONDS); //Time for send data
    }

    public static Observable<Details> streamDetails(String placeId){
        GooglePlacesService googleDetailsService = GooglePlacesService.retrofit.create(GooglePlacesService.class);

        return googleDetailsService.getDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(500, TimeUnit.SECONDS);
    }

}
