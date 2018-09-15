package com.behague.benjamin.go_4_lunch.controllers.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.objects.Details.Details;
import com.behague.benjamin.go_4_lunch.models.objects.Details.Result;
import com.behague.benjamin.go_4_lunch.utils.GooglePlaceStream;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class RestauActivity extends AppCompatActivity {

    @BindView(R.id.restau_img)
    ImageView imgRestau;

    @BindView(R.id.restau_name)
    TextView nameRestau;

    @BindView(R.id.restau_style_vicinity)
    TextView styleVicinityRestau;

    @BindView(R.id.restau_call)
    ImageButton  callRestau;

    @BindView(R.id.restau_like)
    ImageButton likeRestau;

    @BindView(R.id.restau_web)
    ImageButton webRestau;

    List<Result> listDetails;
    Result result;
    Disposable disposable;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restau);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");

        listDetails = new ArrayList<>();

        this.executeHttpRequestDetails();
    }

    private void executeHttpRequestDetails(){
        this.disposable = GooglePlaceStream.streamDetails(id).subscribeWith(new DisposableObserver<Details>(){
            @Override
            public void onNext(Details details){
                result = details.getResult();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "Completed");
                updateUI();
            }
        });
    }

    private void updateUI(){
        nameRestau.setText(result.getName());
        String styleVicy = result.getTypes().get(0) + " - " + result.getVicinity();
        styleVicinityRestau.setText(styleVicy);
        if(result.getFormattedPhoneNumber() != null){
            callRestau.setEnabled(true);
        } else {
            webRestau.setEnabled(false);
        }

        if(result.getWebsite() != null){
            webRestau.setEnabled(true);
        } else {
            webRestau.setEnabled(false);
        }
    }

    @OnClick(R.id.restau_call)
    public void onClickCall(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", result.getInternationalPhoneNumber(), null));
        startActivity(callIntent);
    }

    @OnClick(R.id.restau_web)
    public void onClick(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getWebsite()));
        startActivity(browserIntent);
    }
}
