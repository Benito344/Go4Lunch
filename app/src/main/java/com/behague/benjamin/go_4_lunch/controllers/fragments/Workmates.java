package com.behague.benjamin.go_4_lunch.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.behague.benjamin.go_4_lunch.models.api.UserHelper;
import com.behague.benjamin.go_4_lunch.models.objects.Details.Result;
import com.behague.benjamin.go_4_lunch.models.objects.User;
import com.behague.benjamin.go_4_lunch.utils.FriendsAdapter;
import com.behague.benjamin.go_4_lunch.utils.WorkmatesAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Workmates extends Fragment {

    @BindView(R.id.recycler_view_friend)
    RecyclerView friendList;

    List<User> listUser;

    private WorkmatesAdapter mWorkmatesAdapter;

    Result result;

    private String COLLECTION_NAME = "users";

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
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);

        this.initRecyclerView();
        this.executeFirebaseRequest();

        return view;
    }

    public void initRecyclerView(){
        this.listUser = new ArrayList<>();
        this.mWorkmatesAdapter = new WorkmatesAdapter(MainActivity.listUser, Glide.with(this));
        this.friendList.setAdapter(this.mWorkmatesAdapter);
        this.friendList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void executeFirebaseRequest(){
        FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            for(DocumentSnapshot documentSnapshot : myListOfDocuments){
                                UserHelper.getUser(documentSnapshot.getId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        if(!user.getUid().equals(getCurrentUser().getUid())){
                                            listUser.add(user);}
                                        mWorkmatesAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

}
