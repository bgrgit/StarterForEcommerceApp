package com.cashkarodemoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashkarodemoapp.interfaces.OnDialogListner;
import com.cashkarodemoapp.utillities.AlertDialogBuilder;
import com.cashkarodemoapp.utillities.NetworkCheck;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StoreSelectorActivity extends AppCompatActivity implements View.OnClickListener, OnDialogListner {

    private int[] sampleImages = {R.drawable.banner_flipkart1, R.drawable.banner_amazon2,
            R.drawable.banner_snapdeal, R.drawable.banner_makemytrip, R.drawable.banner_fashionu};
    private String[] mRetailerName = {"Flipkart", "Amazon",
            "Snapdeal", "Makemytrip", "Fashion and you"};
    private String[] mRetailerNameUrl = {"https://www.flipkart.com/", "https://www.amazon.in/",
            "https://www.snapdeal.com/", "https://www.makemytrip.com/", "http://www.fashionandyou.com/"};
    private ImageView mRetailerImageImgVw;
    private ImageView mShareImageImgVw;
    private Button mGetCodeBtn;
    private TextView mRetailerTitleTxt;
    private int mPosition;
    private NetworkCheck mNetworkCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selector);
        mNetworkCheck=new NetworkCheck(this);
        mRetailerImageImgVw = (ImageView) findViewById(R.id.storeselector_img);
        mRetailerTitleTxt = (TextView) findViewById(R.id.storeselector_txt_cashbacktitle);
        mShareImageImgVw = (ImageView) findViewById(R.id.layout_cashback_img_share);
        mGetCodeBtn = (Button) findViewById(R.id.layout_cashback_btn_getcode);
        mShareImageImgVw.setOnClickListener(this);
        mGetCodeBtn.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        mPosition = bundle.getInt("position");
        setupToolbar();
        //Get the bundle
        setScreenRetailerDetails();

    }

    private void setScreenRetailerDetails() {
        mRetailerImageImgVw.setImageResource(sampleImages[mPosition]);
        mRetailerTitleTxt.setText("Top " + mRetailerName[mPosition] + " Offers");
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mRetailerName[mPosition]);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_cashback_btn_getcode:
                AlertDialogBuilder.getInstance().showErrorDialog("Great!!",
                        "Congratulations you have clicked on " + mRetailerName[mPosition], "OK", "congratz", this, this);

                break;
            case R.id.layout_cashback_img_share:
                share();
                break;
        }
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mRetailerNameUrl[mPosition]);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void loadRetailerLink() {
        if(mNetworkCheck.isConnectingToInternet()) {
            Intent intent = new Intent(StoreSelectorActivity.this, WebviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("url", mRetailerNameUrl[mPosition]);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Please check your Network connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog, String whichDialog) {

    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog, String whichDialog) {
        if (whichDialog.equals("congratz")) {
            loadRetailerLink();
        }
    }
}
