package com.behague.benjamin.go_4_lunch.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.User;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Benjamin BEHAGUE on 05/12/2018.
 */
public class WorkmatesViewholder extends RecyclerView.ViewHolder {
    @BindView(R.id.friend_name)
    TextView name;
    @BindView(R.id.friend_picture)
    ImageView image;

    private String TAG = "GLIDE";

    public WorkmatesViewholder (View v){
        super(v);
        ButterKnife.bind(this, v);
    }

    public void updateDatas(User user, RequestManager glide) {
        if(!user.getUsername().equals(getCurrentUser().getDisplayName())) {
            name.setText(user.getUsername());

            glide.load(user.getUrlPicture())
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
                    })
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);
        }
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
}
