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
import android.widget.Toast;

public class RulesActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    boolean try_once = false;
    boolean second_if = false;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        webView = findViewById(R.id.wv_details);

        toolbar = findViewById(R.id.toolbar_details_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RC Rules");

        AlertDialog.Builder builder = new AlertDialog.Builder(RulesActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_dialogue_layout_1);

        final AlertDialog dialog1 = builder.create();
        dialog1.show();
        Window window1 = dialog1.getWindow();
        if (window1 != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog1.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog1.getWindow().setAttributes(layoutParams);
        }

        dialog1.show();

        try_once = true;
        webView.setWebViewClient(new WebViewClient() {
            boolean page_load_error = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (page_load_error) {
                    dialog1.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(second_if){
                        dialog1.dismiss();
                        webView.setVisibility(View.VISIBLE );
                        Log.d("CALLED FOR CUSTOM URL","YES");
                        return;
                    }
                    if(!try_once)
                        return;
                    try_once = false;
                    webView.evaluateJavascript("(function(){return document.documentElement.innerHTML;})();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e("please",value);
                            String page = value;
                            page = page.replace("\\u003C","<");
                            page = page.replace("\\n","");
                            page = page.replace("\\t","");

                            int ind = page.indexOf("<div class=\\\"heading_two\\\">Borrowing Privileges");
                            page = page.substring(ind);
                            ind  = page.indexOf("action");
                            page = page.substring(0,ind+20);
                            ind  = page.indexOf("<p align=\\\"right");
                            int ind2 = page.indexOf("</p>",ind+1);
                            page = page.replace(page.substring(ind,ind2),"");
                            Log.e("heyy",ind+"  "+ind2);
                            Log.e("HEU",page);

                            webView.loadData(page,"text/html","UTF-8");
                            second_if = true;
                        }
                    });
                }
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl("http://resourcecentre.daiict.ac.in/services/index.html");

    }
}
