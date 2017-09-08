package com.cashkarodemoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.cashkarodemoapp.interfaces.OnDialogListner;
import com.cashkarodemoapp.utillities.AlertDialogBuilder;
import com.cashkarodemoapp.utillities.NetworkCheck;
import com.cashkarodemoapp.utillities.StorePreference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDialogListner {

    private CarouselView carouselView;
    private int MY_CAMERA_REQUEST_CODE = 1000;
    private int ACTIVITY_RESULT_LOGIN = 1002;
    private int[] sampleImages = {R.drawable.banner_flipkart1, R.drawable.banner_amazon2,
            R.drawable.banner_snapdeal, R.drawable.banner_makemytrip, R.drawable.banner_fashionu};
    private int mPosition = 0;
    private StorePreference mStorePreference;
    private NetworkCheck mNetworkCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mStorePreference = new StorePreference(this);
        mNetworkCheck=new NetworkCheck(this);
        setupNavigationAndToolbar();
        setupCarouselView();
        setTitle("");
    }

    private void setupCarouselView() {
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                mPosition = position;
                if(mNetworkCheck.isConnectingToInternet()) {
                    if (mStorePreference.getEmailId().trim().equals("")) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("activity_result", true);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, ACTIVITY_RESULT_LOGIN, bundle);
                    } else {
                        launchStoreActivity();
                    }
                }else{
                    Toast.makeText(HomeActivity.this, "Please check your Network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    private void launchStoreActivity() {
        Intent intent = new Intent(HomeActivity.this, StoreSelectorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", mPosition);
        intent.putExtras(bundle);
        startActivity(intent);
    }
//    ViewListener viewListener = new ViewListener() {
//
//        @Override
//        public View setViewForPosition(int position) {
//            View customView = getLayoutInflater().inflate(R.layout.layout_custom_carousel, null);
//            ImageView imageView=(ImageView)customView.findViewById(R.id.layout_custom_img);
//            imageView.setImageResource(sampleImages[position]);
//
//            //set view attributes here
//
//            return customView;
//        }
//    };

    private void setTitle(String name) {
        getSupportActionBar().setTitle("Welcome "+name);
    }

    private void setupNavigationAndToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            openCamera();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_LOGIN) {
            if (!mStorePreference.getEmailId().trim().equals("")) {
                setTitle("User");
                launchStoreActivity();
            }
        }
    }

    private void openCamera() {
        if (!checkPermissionForCamera()) {
            AlertDialogBuilder.getInstance().showErrorDialog("Camera Permission", "Press Ok to grant permission cancel to ignore", "CANCEL", "OK", "Camera", this, this);
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted for camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog, String whichDialog) {
        dialog.dismiss();
        if (whichDialog.equals("Camera")) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog, String whichDialog) {
        dialog.dismiss();
    }
}
