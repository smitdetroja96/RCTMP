package com.example.rctmp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ScholarlyResourcesActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    boolean try_once = false;

    String refresh_url;

    ProgressBar progressBar;

    Toolbar scholarly_resources_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarly_resources);
//-------------------------------------------------------------------------------------------------------------------------------------
        scholarly_resources_toolbar = findViewById(R.id.toolbar_scholarly_resources);
        setSupportActionBar(scholarly_resources_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RC");
//-------------------------------------------------------------------------------------------------------------------------------------
        progressBar = findViewById(R.id.pb_progressBar1);

        webView = findViewById(R.id.wv_drsr);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress == 100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
        });

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

    public void onBackPressed(){
        if(webView.canGoBack())
        {
            webView.goBack();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();
        if(menu_id == R.id.toolbar_refresh)
        {
            refresh_url = webView.getUrl();
            webView.loadUrl(refresh_url);
//            Toast.makeText(this, "Toolbar Menu 1", Toast.LENGTH_SHORT).show();
        }
        else if(menu_id == R.id.toolbar_open_browser)
        {
            String temp_url = webView.getUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(temp_url));
            startActivity(i);

//            Toast.makeText(this, "Toolbar Menu 2", Toast.LENGTH_SHORT).show();
        }
        else if(menu_id == R.id.toolbar_share)
        {
            try
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "RC APP");

                String share_string = webView.getUrl();

                String sAux = "RC APP\n";
                sAux = sAux + share_string;

                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Read..."));

            }
            catch (Exception e) {}
//            Toast.makeText(this, "Toolbar Menu 3", Toast.LENGTH_SHORT).show();
        }
        else if(menu_id == R.id.home)
        {
            Log.e("Hiii","........");
//            Toast.makeText(this, "Hiii....", Toast.LENGTH_SHORT).show();
//            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
