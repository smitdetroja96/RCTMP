package com.example.rctmp;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetailsActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        webView = findViewById(R.id.wv_details);

        toolbar = findViewById(R.id.toolbar_details_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RC Details");


    }
}
