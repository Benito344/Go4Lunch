package com.behague.benjamin.go_4_lunch.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.Result;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Benjamin BEHAGUE on 14/08/2018.
 */
public class ListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_restaurant)
    TextView name;
    @BindView(R.id.vicinity_restaurant)
    TextView vicinity;
    @BindView(R.id.open_restaurant)
    TextView open;
    @BindView(R.id.distance_restaurant)
    TextView dist;
    @BindView(R.id.image_restaurant)
    ImageView image;

    public ListViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }

    public void updateDatas(Result result, RequestManager glide){
        this.name.setText(result.getName());
        this.vicinity.setText(result.getVicinity());
        if(result.getPhotos() != null){
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=75&photoreference=" + result.getPhotos().get(0).getPhotoReference())
                    .apply(RequestOptions.centerCropTransform()).into(image);
            image.setContentDescription("Photos du restau");
        }
    }
}
