package com.example.rctmp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.SyncStateContract;
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

public class RaxterSearchActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    ProgressBar progressBar;

    Toolbar raxter_search_toolbar;

    String refresh_url;

    boolean page_load_error = true;
    boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raxter_search);

//-------------------------------------------------------------------------------------------------------------------------------------------
        raxter_search_toolbar = findViewById(R.id.toolbar_raxter_search);
        setSupportActionBar(raxter_search_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RC");
//-------------------------------------------------------------------------------------------------------------------------------------------

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


        webSettings = webView.getSettings();
        if(Build.VERSION.SDK_INT >= 28){
            webView.setInitialScale(100);
            webSettings.setBuiltInZoomControls(true);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl("https://assistant.raxter.io/discover/library");
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
