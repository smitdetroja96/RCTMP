package com.example.rctmp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static boolean loginState;
    TextInputEditText username_field,password_field;
    WebView webView;
    WebSettings webSettings;
    String username="",password="";
    boolean try_once = false,sign_in_tried = false;
    Button sign_in_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sign_in_button = findViewById(R.id.bt_sign_in);
        webView = findViewById(R.id.wv_sign_in);
        username_field = findViewById(R.id.ti_et_student_id);
        password_field = findViewById(R.id.ti_et_password);

        webView.addJavascriptInterface(new CustomJavaScriptInterface(MainActivity.this),"app");

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
                    //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();

                    webView.evaluateJavascript("var check = !(!(document.getElementById('userdetails')));", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            webView.evaluateJavascript("app.userCheck(check);", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    if (loginState)
                                    {
                                        startActivity(new Intent(MainActivity.this,DrawerActivityLayout.class));
                                        finish();

                                    } else {
                                        if (sign_in_tried)
                                            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                        sign_in_tried = false;
                                        sign_in_button.setEnabled(true);

                                    }
                                }
                            });
                        }
                    });

                    if (loginState)
                        Toast.makeText(MainActivity.this, "LOGGED IN SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
                    else {
                        sign_in_button.setEnabled(false);
                    }
                }


                //startActivity(new Intent(MainActivity.this, ChangedLayoutActivity.class));
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-user.pl");

    }

    public void onClickSignUp(View view) {

        if(TextUtils.isEmpty(username_field.getText()) || TextUtils.isEmpty(password_field.getText()))
        {
            Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        sign_in_tried = true;
        webView.evaluateJavascript("document.getElementById('userid').value = '" + username + "';", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //webView.evaluateJavascript("document.getElementById('password').value = '" + s_password + "';",);
                webView.evaluateJavascript("document.getElementById('password').value = '" + password + "';", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        try_once = true;
                        webView.evaluateJavascript("document.getElementById('auth').submit();",null);

                    }
                });

            }
        });

    }
}

