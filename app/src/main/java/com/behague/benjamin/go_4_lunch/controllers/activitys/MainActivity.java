package com.behague.benjamin.go_4_lunch.controllers.activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apt7.rxpermissions.Permission;
import com.apt7.rxpermissions.PermissionObservable;
import com.behague.benjamin.go_4_lunch.R;
import com.behague.benjamin.go_4_lunch.models.api.UserHelper;
import com.behague.benjamin.go_4_lunch.models.objects.User;
import com.behague.benjamin.go_4_lunch.views.PageAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.behague.benjamin.go_4_lunch.utils.GPSTracker.MY_PERMISSIONS_REQUEST_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR UI
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavView;


    @BindView(R.id.activity_main_nav_view)
    NavigationView navView;

    @BindView(R.id.activity_main_viewpager)
    ViewPager viewPager;

    private TextView userName;
    private TextView userEmail;
    private ImageView userPicture;

    private LocationManager locationManager;

    //FOR DATA
    private static final int SIGN_OUT_TASK = 10;
    private static final int RC_SIGN_IN = 1993;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (!isCurrentUserLogged()) {
            this.startSignInActivity();
        }

        bottomNavView = findViewById(R.id.bottom_navigation);
        View headerView = navView.getHeaderView(0);
        userName = headerView.findViewById(R.id.name_user);
        userEmail = headerView.findViewById(R.id.mail_user);
        userPicture = headerView.findViewById(R.id.picture_user);

        PermissionObservable.getInstance().checkThePermissionStatus(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new DisposableObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        System.out.println("Permission Check : " + permission.getName() + " -- " + permission.getGranted());
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("ERROR");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("DONE");
                        // on complete, dispose method automatically un subscribes the subscriber
                        dispose();
                    }
                });

        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureViewPager();
        this.initUserInfo();

        bottomNavView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        onNavigationBottom(item.getItemId());

                        return true;
                    }
                });
    }

    private void configureToolbar(){
        //Get the toolbar view inside the activity layout
        toolbar = findViewById(R.id.toolbar);
        //Sets the Toolbar
        setSupportActionBar(toolbar);
    }

    private void configureNavigationView(){
        NavigationView navigationView ;

        navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureViewPager(){
        // Get ViewPager from layout
        ViewPager page = findViewById(R.id.activity_main_viewpager);
        // Set Adapter PageAdapter and glue it together
        page.setAdapter(new PageAdapter(getSupportFragmentManager()));
    }

    private void initUserInfo(){
        if (this.getCurrentUser() != null){
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(userPicture);
            }

            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            this.userEmail.setText(email);

            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    String username = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.info_no_username_found) : currentUser.getUsername();
                    userName.setText(username);
                }
            });
        }
    }

    public void setNavigationVisibility(boolean visible) {
        if (bottomNavView.isShown() && !visible) {
            bottomNavView.setVisibility(View.GONE);
        }
        else if (!bottomNavView.isShown() && visible){
            bottomNavView.setVisibility(View.VISIBLE);
        }
    }

    public void onClickSignOutButton() { this.signOutUserFromFirebase(); }

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                switch (origin){

                    case SIGN_OUT_TASK:
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    private boolean onNavigationBottom(int item){
        switch (item) {

            case R.id.map_view :
                viewPager.setCurrentItem(0);
                break;

            case R.id.list_view :
                viewPager.setCurrentItem(1);
                break;

            case R.id.workmates :
                viewPager.setCurrentItem(2);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                // 2 - CREATE USER IN FIRESTORE
                this.createUserInFirestore();
                Toast.makeText(getApplicationContext(), getString(R.string.connection_succeed),Toast.LENGTH_SHORT).show();
                this.initUserInfo();
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_authentication_canceled),Toast.LENGTH_SHORT).show();
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_no_internet),Toast.LENGTH_SHORT).show();
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void createUserInFirestore(){
        if (this.getCurrentUser() != null){
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uemail = this.getCurrentUser().getEmail();
            String uid = this.getCurrentUser().getUid();

            UserHelper.createUser(uid, username, uemail, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.activity_main_drawer_logout :
                onClickSignOutButton();
                break;

            default :
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.auth_screen)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(), // SUPPORT GOOGLE
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())) // FACEBOOK
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_local_cafe_white_24dp)
                        .build(),
                RC_SIGN_IN);
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }
}