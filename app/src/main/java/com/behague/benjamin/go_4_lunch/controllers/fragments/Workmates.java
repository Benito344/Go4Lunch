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
public class Workmates extends Fragment {


    public Workmates() {
        // Required empty public constructor
    }

    public static Workmates newInstance() {
        return (new Workmates());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workmates, container, false);
    }

}
