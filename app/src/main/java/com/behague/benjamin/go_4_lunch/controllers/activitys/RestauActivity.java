package com.behague.benjamin.go_4_lunch.controllers.activitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.api.UserHelper;
import com.behague.benjamin.go_4_lunch.models.objects.Details.Details;
import com.behague.benjamin.go_4_lunch.models.objects.Details.Result;
import com.behague.benjamin.go_4_lunch.models.objects.User;
import com.behague.benjamin.go_4_lunch.utils.FriendsAdapter;
import com.behague.benjamin.go_4_lunch.utils.GooglePlaceStream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    @BindView(R.id.restau_list_friend)
    RecyclerView friendList;

    /*@BindView(R.id.restau_style_vicinity)
    TextView styleVicinityRestau;

    @BindView(R.id.restau_call)
    Button  callRestau;

    @BindView(R.id.restau_like)
    Button likeRestau;

    @BindView(R.id.restau_web)
    Button webRestau;

    /*@BindView(R.id.restau_rating0)
    ImageView rating0;

    @BindView(R.id.restau_rating1)
    ImageView rating1;

    @BindView(R.id.restau_rating2)
    ImageView rating2;

    @BindView(R.id.restau_rating3)
    ImageView rating3;

    @BindView(R.id.restau_rating4)
    ImageView rating4;*/

    List<Result> listDetails;
    List<User> listUser;
    Result result;
    Disposable disposable;
    private FriendsAdapter friendAdapt;

    RequestManager glide;

    private String id;
    private String picture;
    private final String TAG = "GLIDE";
    private String COLLECTION_NAME = "users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restau);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        picture = intent.getStringExtra("PICTURE");

        listDetails = new ArrayList<>();

        this.initRecyclerView();
        this.executeHttpRequestDetails();
        this.executeFirebaseRequest();
    }

    public void initRecyclerView(){
        this.listUser = new ArrayList<>();
        this.friendAdapt = new FriendsAdapter(listUser, Glide.with(this));
        this.friendList.setAdapter(this.friendAdapt);
        this.friendList.setLayoutManager(new LinearLayoutManager(this));
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
                                        listUser.add(user);
                                        friendAdapt.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void updateUI(){
        nameRestau.setText(result.getName());
        //String styleVicy = result.getTypes().get(0) + " - " + result.getVicinity();
       /* styleVicinityRestau.setText(styleVicy);
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

        int rat = (int) Math.round(result.getRating());

        switch(rat){
            case 1 :
                rating0.setVisibility(View.VISIBLE);
                break;
            case 2 :
                rating0.setVisibility(View.VISIBLE);
                rating1.setVisibility(View.VISIBLE);
                break;
            case 3 :
                rating0.setVisibility(View.VISIBLE);
                rating1.setVisibility(View.VISIBLE);
                rating2.setVisibility(View.VISIBLE);
                break;
            case 4 :
                rating0.setVisibility(View.VISIBLE);
                rating1.setVisibility(View.VISIBLE);
                rating2.setVisibility(View.VISIBLE);
                rating3.setVisibility(View.VISIBLE);
                break;
            case 5 :
                rating0.setVisibility(View.VISIBLE);
                rating1.setVisibility(View.VISIBLE);
                rating2.setVisibility(View.VISIBLE);
                rating3.setVisibility(View.VISIBLE);
                rating4.setVisibility(View.VISIBLE);
                break;
            default :
                break;
        }
*/
        if(picture != null) {
            Glide.with(this)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&maxheight=150&key=AIzaSyApi61iqZP6ZR-mGkYvZkTSLH7OskLQJj0&photoreference=" + picture)
                    .into(imgRestau);
        }
            imgRestau.setContentDescription("Photos du restau");
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
