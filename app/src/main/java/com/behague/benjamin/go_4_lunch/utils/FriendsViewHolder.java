package com.behague.benjamin.go_4_lunch.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Benjamin BEHAGUE on 14/10/2018.
 */
public class FriendsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.friend_name)
    TextView name;
    @BindView(R.id.friend_picture)
    ImageView picture;

    public FriendsViewHolder (View v){
        super(v);
        ButterKnife.bind(this, v);
    }

    public void updateDatas() {

    }

}
