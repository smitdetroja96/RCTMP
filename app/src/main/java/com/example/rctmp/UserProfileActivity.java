package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UserProfileActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    boolean try_once = true;

    String name = "";

    TextView card_number,full_name;

    ImageView user_image;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        webView = findViewById(R.id.wv_personal_details);

        card_number = findViewById(R.id.tv_libraryCardNumber);
        full_name = findViewById(R.id.tv_userName);
        user_image = findViewById(R.id.iv_user_image);

        //***********************************************************************************************

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setCancelable(false);
//        TextView textView = findViewById(R.id.loader);
//        textView.setText("Fetching Books...");
        builder.setView(R.layout.progress_dialogue_layout_1);

        final AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.show();

        //***********************************************************************************************

        readData();

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
                    dialog.dismiss();
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

                            card_number.setText(user_id);
                            full_name.setText(name);

                            dialog.dismiss();

//                            Toast.makeText(UserProfileActivity.this, name, Toast.LENGTH_SHORT).show();
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
//--------------------------------------------------------------------------------------------------------------------------------------
    private void readData()
    {
        SharedPreferences preferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        user_id = preferences.getString("ID","No user");
    }
//--------------------------------------------------------------------------------------------------------------------------------------

}
