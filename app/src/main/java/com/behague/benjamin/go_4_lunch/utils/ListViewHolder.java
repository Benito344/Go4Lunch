package com.behague.benjamin.go_4_lunch.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.Metrix.Metrix;
import com.behague.benjamin.go_4_lunch.models.objects.Places.Result;
import com.behague.benjamin.go_4_lunch.models.objects.Metrix.Row;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

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

    List<Row> listMetrix;
    private Disposable disposableMetrix;

    private final String TAG = "GLIDE";

    public ListViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
        listMetrix = new ArrayList();
    }

    public void updateDatas(Result result, RequestManager glide, Context context){
        this.name.setText(result.getName());
        this.vicinity.setText(result.getVicinity());
        String destinations = result.getGeometry().getLocation().getLat() + "," + result.getGeometry().getLocation().getLng();
        executeHttpRequestMetrix(destinations);
        if(result.getOpeningHours() != null){
            if(result.getOpeningHours().getOpenNow() != null){
                if(result.getOpeningHours().getOpenNow()){
                    open.setText("Open");
                    open.setTextColor(ContextCompat.getColor(context,R.color.green));
                } else {
                    open.setText("Closed");
                    open.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }
        }
//        if(result.getOpeningHours().getOpenNow() != null){ //Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//            if(result.getOpeningHours().getOpenNow()){
//                open.setText("Open");
//            } else {
//                open.setText("Closed");
//            }
//        }
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

    private void executeHttpRequestMetrix(String destinations){
            //String destination = listResult.get(0).getGeometry().getLocation().getLat() + "," + listResult.get(0).getGeometry().getLocation().getLng();
            //String destination = "43.679421,4.112593899999999";
            //String origins = "43.6780742,4.0978829";

            listMetrix = new ArrayList<>();
            this.disposableMetrix = GooglePlaceStream.streamMetrix(AppConfig.getPosition() ,destinations).subscribeWith(new DisposableObserver<Metrix>(){
                @Override
                public void onNext(Metrix metrix){
                    listMetrix.addAll(metrix.getRows());
                    Log.e("TAG", "LIST METRIX " + listMetrix.size());
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("TAG", e.getMessage());
                }

                @Override
                public void onComplete() {
                    Log.e("TAG", "Completed");
                    dist.setText(listMetrix.get(0).getElements().get(0).getDistance().getValue() + "m");
                    if (disposableMetrix != null && !disposableMetrix.isDisposed()) disposableMetrix.dispose();
                }
            });
        }
}
