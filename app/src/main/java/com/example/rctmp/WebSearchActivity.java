package com.example.rctmp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
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

    String refresh_url;
    String downloadURL;


    Toolbar web_search_toolbar;

    boolean page_load_error = false;
    boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);
//-------------------------------------------------------------------------------------------------------------------------------------------
        web_search_toolbar = findViewById(R.id.toolbar_web_search);
        setSupportActionBar(web_search_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RC");
//-------------------------------------------------------------------------------------------------------------------------------------------
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


//-----------------------------------------------------------------------------------------------------------------------------------------------------

        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                downloadURL = url;
                if(ContextCompat.checkSelfPermission(WebSearchActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(WebSearchActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else {
                    String download_file_name = URLUtil.guessFileName(url,null, MimeTypeMap.getFileExtensionFromUrl(url));

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setMimeType(mimetype);
                    request.setDescription("Downloading File");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, download_file_name);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                }
            }
        });

//-----------------------------------------------------------------------------------------------------------------------------------------------------


        switch (site_to_be_searched)
        {
            case "aps":
//                refresh_url = "https://journals.aps.org/search/results?sort=relevance&clauses=[%7B%22operator%22:%22AND%22,%22field%22:%22all%22,%22value%22:%22"+search_query+"%22%7D]";
                wv.loadUrl("https://journals.aps.org/search/results?sort=relevance&clauses=[%7B%22operator%22:%22AND%22,%22field%22:%22all%22,%22value%22:%22"+search_query+"%22%7D]");
                break;
            case "bloomsbury":
//                refresh_url = "https://www.bloomsburydesignlibrary.com/search-results?any="+search_query;
                wv.loadUrl("https://www.bloomsburydesignlibrary.com/search-results?any="+search_query);
                break;
            case "jstor" :
//                refresh_url = "https://www.jstor.org/action/doBasicSearch?Query="+search_query+"&acc=on&wc=on&fc=off&group=none";
                wv.loadUrl("https://www.jstor.org/action/doBasicSearch?Query="+search_query+"&acc=on&wc=on&fc=off&group=none");
                loaded = false;
                break;
            case "ieee":
//                refresh_url = "https://ieeexplore.ieee.org/search/searchresult.jsp?newsearch=true&queryText="+search_query+"&subscribed=true";
                wv.loadUrl("https://ieeexplore.ieee.org/search/searchresult.jsp?newsearch=true&queryText="+search_query+"&subscribed=true");
                loaded = false;
                break;
            case "science_direct":
//                refresh_url = "https://www.sciencedirect.com/browse/journals-and-books?contentType=JL&subject=computer-science&searchPhrase="+search_query;
                wv.loadUrl("https://www.sciencedirect.com/browse/journals-and-books?contentType=JL&subject=computer-science&searchPhrase="+search_query);
                loaded = false;
                break;
            case "springer":
//                refresh_url = "https://link.springer.com/search?&query="+search_query+"&showAll=false";
                wv.loadUrl("https://link.springer.com/search?&query="+search_query+"&showAll=false");
                break;
            case "india_stat":
//                refresh_url = "https://www.indiastat.com/";
                wv.loadUrl("https://www.indiastat.com/");
                break;
            case "usenix":
//                refresh_url = "https://www.usenix.org/search/site/"+search_query;
                wv.loadUrl("https://www.usenix.org/search/site/"+search_query);
                break;
        }
    }


//-----------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    String download_file_name = URLUtil.guessFileName(downloadURL,null, MimeTypeMap.getFileExtensionFromUrl(downloadURL));

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadURL));
                    request.allowScanningByMediaScanner();
                    request.setDescription("Downloading File");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, download_file_name);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                } else {
                    Toast.makeText(this, "Please provide Storage Permission to download content!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
//----------------------------------------------------------------------------------------------------------------------------------------------------


    public void onBackPressed(){
        if(wv.canGoBack())
        {
            wv.goBack();
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
            refresh_url = wv.getUrl();
            wv.loadUrl(refresh_url);
//            Toast.makeText(this, "Toolbar Menu 1", Toast.LENGTH_SHORT).show();
        }
        else if(menu_id == R.id.toolbar_open_browser)
        {
            String temp_url = wv.getUrl();
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

                String share_string = wv.getUrl();

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

