package com.example.rctmp;

import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RaxterSearchActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    ProgressBar progressBar;
    boolean page_load_error = true;
    boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raxter_search);

        progressBar = findViewById(R.id.pb_raxter);
        webView = findViewById(R.id.wv_raxter);


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress==100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
        });


        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!loaded) {
                    super.onPageFinished(view, url);
                    if (!page_load_error) {
                        loaded = true;
                    }
                }
                else {
                    //Toast.makeText(getApplicationContext(), "Error: Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                    page_load_error = false;
                }
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl("https://assistant.raxter.io/discover/library");
    }

}
