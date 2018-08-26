package com.behague.benjamin.go_4_lunch.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.Result;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


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

    private final String TAG = "GLIDE";

    public ListViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }

    public void updateDatas(Result result, RequestManager glide, Context context){
        this.name.setText(result.getName());
        this.vicinity.setText(result.getVicinity());
//        if(result.getOpeningHours().getOpenNow()){
//            open.setText("Open");
//           // open.setTextColor(ContextCompat.getColor(context,R.color.green));
//        } else {
//            open.setText("Closed");
//           // open.setTextColor(ContextCompat.getColor(context, R.color.red));
//        }
        if(result.getPhotos() != null) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=75&key=AIzaSyApi61iqZP6ZR-mGkYvZkTSLH7OskLQJj0&photoreference=" + result.getPhotos().get(0).getPhotoReference())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Load failed", e);

                            assert e != null;
                            for (Throwable t : e.getRootCauses()) {
                                Log.e(TAG, "Caused by", t);
                            }

                            e.logRootCauses(TAG);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e(TAG, "Load Success");

                            return false;
                        }
                    }).into(image);
        }
            image.setContentDescription("Photos du restau");
        }
}
