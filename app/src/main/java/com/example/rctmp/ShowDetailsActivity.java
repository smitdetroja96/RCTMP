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

public class ShowDetailsActivity extends AppCompatActivity {

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
        getSupportActionBar().setTitle("RC Timings");

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailsActivity.this);
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
                            int ind1 = value.indexOf("\\u003Cp>");
                            ind1 = value.indexOf("\\u003Cp>",ind1+2);
                            int ind2 = value.indexOf("Bottom");

                            String page = value.substring(ind1,ind2+42);
                            page = page.replace("\\u003C","<");
                            page = page.replace("\\n","");
                            page = page.replace("\\t","");
                            page = page.replace("<p>","");
                            page = page.replace("</p>","");
                            page = page.replaceFirst("<ul>","");

                            Log.e("thisissparta",page);

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
        webView.loadUrl("http://resourcecentre.daiict.ac.in/rc/hours.html");

    }
}
