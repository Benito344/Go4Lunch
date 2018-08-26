package com.behague.benjamin.go_4_lunch.controllers.fragments;


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
import com.behague.benjamin.go_4_lunch.models.objects.PlaceObject;
import com.behague.benjamin.go_4_lunch.models.objects.Result;
import com.behague.benjamin.go_4_lunch.utils.GooglePlaceStream;
import com.behague.benjamin.go_4_lunch.utils.ListAdapter;
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
    private Disposable disposable;
    private String latLong ;

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
        latLong = "43.6780742,4.0978829";

        this.initRecyclerView();
        this.executeHttpRequest();

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

    private void executeHttpRequest(){
        this.disposable = GooglePlaceStream.streamPlaces(latLong, 1500, "restaurant").subscribeWith(new DisposableObserver<PlaceObject>(){
            @Override
            public void onNext(PlaceObject placeObject){
                listResult.addAll(placeObject.getResults());
                listAdapter.notifyItemRangeChanged(0,listResult.size());
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
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

}
