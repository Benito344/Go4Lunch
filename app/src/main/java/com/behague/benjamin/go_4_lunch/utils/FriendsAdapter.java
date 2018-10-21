package com.behague.benjamin.go_4_lunch.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.User;
import com.bumptech.glide.RequestManager;

import java.util.List;

/**
 * Created by Benjamin BEHAGUE on 14/10/2018.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private RequestManager glide;
    private List<User> user;
    private Context context;

    public FriendsAdapter (List<User> mUser, RequestManager glide){
        this.user = mUser;
        this.glide = glide;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recycler_view_friends, parent, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder viewHolder, int position){
        viewHolder.updateDatas(user.get(position), glide);
    }

    @Override
    public int getItemCount(){
        return user.size();
    }
}
