package com.behague.benjamin.go_4_lunch.controllers.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.controllers.activitys.MainActivity;
import com.behague.benjamin.go_4_lunch.controllers.activitys.RestauActivity;
import com.behague.benjamin.go_4_lunch.models.objects.Places.PlaceObject;
import com.behague.benjamin.go_4_lunch.models.objects.Places.Result;
import com.behague.benjamin.go_4_lunch.models.objects.Metrix.Row;
import com.behague.benjamin.go_4_lunch.utils.AppConfig;
import com.behague.benjamin.go_4_lunch.utils.GPSTracker;
import com.behague.benjamin.go_4_lunch.utils.GooglePlaceStream;
import com.behague.benjamin.go_4_lunch.utils.ListAdapter;
import com.behague.benjamin.go_4_lunch.views.ItemClickRecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListView extends Fragment {

    private List<Result> listResult;
    private ListAdapter listAdapter;
    private Disposable disposablePlace;
    public static String latLong ;
    private GPSTracker mGPSTracker;
    @BindView(R.id.recycler_view)
    RecyclerView rcView;

    public ListView() {
        // Required empty public constructor
    }

    public static ListView newInstance() {
        return (new ListView());
    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert inflater != null;
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        mGPSTracker = new GPSTracker(getContext());
        latLong = mGPSTracker.getLatitude() + "," + mGPSTracker.getLongitude();
        AppConfig.setPosition(latLong);


        this.initRecyclerView();
        this.configureOnClickRecyclerView();
        this.executeHttpRequestPlace();

        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).setNavigationVisibility(false);
                } else if (dy < 0 ) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).setNavigationVisibility(true);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    private void initRecyclerView() {
        this.listResult = new ArrayList<>();
        this.listAdapter = new ListAdapter(listResult, Glide.with(this));
        this.rcView.setAdapter(this.listAdapter);
        this.rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureOnClickRecyclerView(){
        ItemClickRecyclerView.addTo(rcView, R.layout.fragment_list_view)
                .setOnItemClickListener(new ItemClickRecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String placeId = listResult.get(position).getPlaceId();
                        Intent restauActivity = new Intent(getContext(), RestauActivity.class);
                        restauActivity.putExtra("ID", placeId);
                        if(listResult.get(position).getPhotos() != null) {
                            restauActivity.putExtra("PICTURE", listResult.get(position).getPhotos().get(0).getPhotoReference());
                        }
                        getContext().startActivity(restauActivity);
                    }
                });
    }

    private void executeHttpRequestPlace(){
        this.disposablePlace = GooglePlaceStream.streamPlaces(latLong, 1500, "restaurant").subscribeWith(new DisposableObserver<PlaceObject>(){
            @Override
            public void onNext(PlaceObject placeObject){
                listResult.addAll(placeObject.getResults());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "Completed");
                listAdapter.notifyItemRangeChanged(0,listResult.size());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if (this.disposablePlace != null && !this.disposablePlace.isDisposed()) this.disposablePlace.dispose();
    }

}
