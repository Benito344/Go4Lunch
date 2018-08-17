package com.behague.benjamin.go_4_lunch.controllers.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.controllers.activitys.MainActivity;
import com.behague.benjamin.go_4_lunch.models.objects.OpeningHours;
import com.behague.benjamin.go_4_lunch.models.objects.Photo;
import com.behague.benjamin.go_4_lunch.models.objects.Result;
import com.behague.benjamin.go_4_lunch.utils.ListAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListView extends Fragment {

    private List<Result> listResult;
    private ListAdapter listAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView rcView;

    public ListView() {
        // Required empty public constructor
    }

    public static ListView newInstance() {
        return (new ListView());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        this.initRecyclerView();

        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.restaurant);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();

        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        this.executeParseJSON(builder.toString());


        rcView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    ((MainActivity)getActivity()).setNavigationVisibility(false);
                } else if (dy < 0 ) {
                    ((MainActivity)getActivity()).setNavigationVisibility(true);
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

    private void executeParseJSON(String s) {

        try {
            JSONObject root = new JSONObject(s);

            JSONArray result = root.getJSONArray("results");

            for(int i = 0 ; i < result.length() ; i++){

                Result resultObject = new Result();
                Photo photoObject = new Photo();
                OpeningHours openHours = new OpeningHours();

                List<Photo> photo = new ArrayList<>();

                JSONObject resultat = result.getJSONObject(i);
                JSONObject geometry = resultat.getJSONObject("geometry");
/*                if(resultat.getJSONArray("photos") != null) {
                    JSONArray photosArray = resultat.getJSONArray("photos");
                    JSONObject photosObj = photosArray.getJSONObject(0);
                    photoObject.setPhotoReference(photosObj.getString("photo_reference"));
                    photo.add(photoObject);
                    resultObject.setPhotos(photo);
                }
                if(resultat.getJSONObject("openning_hours") != null) {
                    JSONObject openNow = resultat.getJSONObject("opening_hours");
                    openHours.setOpenNow(openNow.getBoolean("open_now"));
                    resultObject.setOpeningHours(openHours);
                }*/

                resultObject.setName(resultat.getString("name"));
                resultObject.setVicinity(resultat.getString("vicinity"));

                listResult.add(resultObject);
            }
            //listAdapter.setData(listResult);
            //listAdapter.notifyDataSetChanged();
            listAdapter.notifyItemRangeChanged(0,listResult.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
