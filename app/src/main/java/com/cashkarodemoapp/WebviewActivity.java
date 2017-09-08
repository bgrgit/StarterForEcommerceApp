package com.cashkarodemoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class WebviewActivity extends AppCompatActivity {
    private WebView mWebView;
    private CircularProgressView mCircularProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mCircularProgressView = (CircularProgressView) findViewById(R.id.progress_view);
        mWebView = (WebView) findViewById(R.id.webvw_webview);
        setupToolbar();
        isWebviewMakeVisible(false);
        loadUrlTowebview();
    }

    private void loadUrlTowebview() {
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                isWebviewMakeVisible(true);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Retailer Page");

    }

    private void isWebviewMakeVisible(boolean status) {
        if (status) {
            mCircularProgressView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        } else {
            mCircularProgressView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }
    }
}
