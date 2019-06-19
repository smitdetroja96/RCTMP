package com.example.rctmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    boolean try_once = true;

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        webView = findViewById(R.id.wv_personal_details);

        webView.setWebViewClient(new WebViewClient() {
            boolean page_load_error = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (page_load_error) {

                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!try_once)
                        return;

                    try_once = false;
                    webView.evaluateJavascript("(function(){var x = document.getElementById('borrower_title'); return x.options[x.selectedIndex].value;})();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
//                            Toast.makeText(UserProfileActivity.this, s, Toast.LENGTH_SHORT).show();
//                            return x.innerHTML;

                            name = name + s.substring(1,s.length() - 1) + " ";

                        }
                    });

                    webView.evaluateJavascript("(function(){var x = document.getElementById('borrower_firstname'); return x.value;})()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                            name = name + s.substring(1,s.length() - 1) + " ";

                        }
                    });

                    webView.evaluateJavascript("(function() {var x = document.getElementById('borrower_surname'); return x.value;} )()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            name = name  + s.substring(1,s.length() - 1);
                            Toast.makeText(UserProfileActivity.this, name, Toast.LENGTH_SHORT).show();
                        }
                    });

                }


                //startActivity(new Intent(MainActivity.this, ChangedLayoutActivity.class));
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-memberentry.pl");

    }

    public void onClickChangePassword(View view) {
        Intent i = new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
        startActivity(i);
    }
}
