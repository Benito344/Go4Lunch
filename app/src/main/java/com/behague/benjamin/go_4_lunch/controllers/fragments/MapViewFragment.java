package com.behague.benjamin.go_4_lunch.controllers.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.PlaceObject;
import com.behague.benjamin.go_4_lunch.models.objects.Result;
import com.behague.benjamin.go_4_lunch.utils.GPSTracker;
import com.behague.benjamin.go_4_lunch.utils.GooglePlaceStream;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Disposable disposable;
    public List<Result> mapResult;
    private GPSTracker mGPSTracker;
    private String latLong ;

    public MapViewFragment() {
        // Required empty public constructor
    }

    public static MapViewFragment newInstance() {
        return (new MapViewFragment());
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert inflater != null;
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_map_view, null, false);

        mGPSTracker = new GPSTracker(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latLong = "43.6780742,4.0978829";
        mapResult = new ArrayList<>();
        return view;
    }

    private void executeHttpRequest(){
        this.disposable = GooglePlaceStream.streamPlaces(latLong, 1500, "restaurant").subscribeWith(new DisposableObserver<PlaceObject>(){
            @Override
            public void onNext(PlaceObject placeObject){
                mapResult.addAll(placeObject.getResults());
                updateUI();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "Completed");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        googleMap = mMap;

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        String loc = mGPSTracker.getLatitude() + "/" + mGPSTracker.getLongitude();
        Toast.makeText(getContext(), loc, Toast.LENGTH_SHORT).show();
        LatLng Test = new LatLng(mGPSTracker.getLatitude(), mGPSTracker.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(Test).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(Test).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        this.executeHttpRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    public void updateUI(){
        for(Result result : mapResult){
            LatLng restau = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
            googleMap.addMarker(new MarkerOptions().position(restau).title(result.getName()).snippet(result.getVicinity()));
        }
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }
}

