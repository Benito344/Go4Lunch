package com.behague.benjamin.go_4_lunch.controllers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.behague.benjamin.go_4_lunch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapView extends Fragment {


    public MapView() {
        // Required empty public constructor
    }

    public static MapView newInstance() {
        return (new MapView());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

}
