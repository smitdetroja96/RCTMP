package com.example.rctmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebSearchActivity extends AppCompatActivity {

    WebView wv;
    ProgressBar progressBar;
    Intent intent;
    String site_to_be_searched;
    String search_query;

    Toolbar web_search_toolbar;

    boolean page_load_error = false;
    boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        web_search_toolbar = findViewById(R.id.toolbar_web_search);
        setSupportActionBar(web_search_toolbar);

        progressBar = findViewById(R.id.pb_progressBar);
        wv = findViewById(R.id.wv_search_result);
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        //ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //wv.addJavascriptInterface(new CustomJavaScriptInterface(getApplicationContext()),"app");
        //wv.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        intent = getIntent();
        site_to_be_searched = intent.getStringExtra("site_to_be_searched");
        search_query = intent.getStringExtra("search_query");

        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress==100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
        });


        wv.setWebViewClient(new WebViewClient(){

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
        switch (site_to_be_searched)
        {
            case "aps":
                wv.loadUrl("https://journals.aps.org/search/results?sort=relevance&clauses=[%7B%22operator%22:%22AND%22,%22field%22:%22all%22,%22value%22:%22"+search_query+"%22%7D]");
            case "bloomsbury":
                wv.loadUrl("https://www.bloomsburydesignlibrary.com/search-results?any="+search_query);
                break;
            case "jstor" :
                wv.loadUrl("https://www.jstor.org/action/doBasicSearch?Query="+search_query+"&acc=on&wc=on&fc=off&group=none");
                loaded = false;
                break;
            case "ieee":
                wv.loadUrl("https://ieeexplore.ieee.org/search/searchresult.jsp?newsearch=true&queryText="+search_query+"&subscribed=true");
                loaded = false;
                break;
            case "science_direct":
                wv.loadUrl("https://www.sciencedirect.com/browse/journals-and-books?contentType=JL&subject=computer-science&searchPhrase="+search_query);
                loaded = false;
                break;
            case "springer":
                wv.loadUrl("https://link.springer.com/search?&query="+search_query+"&showAll=false");
                break;
            case "india_stat":
                wv.loadUrl("https://www.indiastat.com/");
                break;
            case "usenix":
                wv.loadUrl("https://www.usenix.org/search/site/"+search_query);
                break;
        }
    }

    public void onBackPressed(){
        if(wv.canGoBack()){
            wv.goBack();
        }
        else finish();
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
        if(menu_id == R.id.toolbar_menu1)
        {
            Toast.makeText(this, "Toolbar Menu 1", Toast.LENGTH_SHORT).show();
        }
        else if(menu_id == R.id.toolbar_menu2)
        {
            Toast.makeText(this, "Toolbar Menu 2", Toast.LENGTH_SHORT).show();
        }
        else if(menu_id == R.id.toolbar_menu3)
        {
            Toast.makeText(this, "Toolbar Menu 3", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}

