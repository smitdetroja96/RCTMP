package com.example.rctmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ScholarlyResourcesActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    boolean try_once = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarly_resources);

        webView = findViewById(R.id.wv_drsr);

        try_once = true;
        webView.setWebViewClient(new WebViewClient() {
            boolean page_load_error = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (page_load_error)
                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                else {
                    if (!try_once) return;
                    try_once = false;
                    webView.evaluateJavascript("(function(){var x = document.getElementsByName('query'); x[1].focus(); return x.length;}());", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //Toast.makeText(ScholarlyResourcesActivity.this, value, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        webView.setInitialScale(1);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webView.loadUrl("http://drsr.daiict.ac.in/handle/123456789/9");

    }
}
