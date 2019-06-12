package com.example.rctmp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText current_password,new_password,confirm_new_password;
    Button change_password_button;
    String current="",new_pwd="",conf_new_pwd="";
    WebView webView;
    boolean first_if=false,second_if=false,change_password_attempted = false;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        change_password_button = findViewById(R.id.bt_change_password);
        webView = findViewById(R.id.wv_change_pwd);
        current_password = findViewById(R.id.ti_current_password);
        new_password = findViewById(R.id.ti_et_new_pwd);
        confirm_new_password = findViewById(R.id.ti_et_new2_password);
    }


    public void onClickChangePassword(View view) {
        if(TextUtils.isEmpty(current_password.getText()) || TextUtils.isEmpty(new_password.getText()) ||
            TextUtils.isEmpty(confirm_new_password.getText()))
        {
            Toast.makeText(ChangePasswordActivity.this, "Field Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        current = current_password.getText().toString();
        new_pwd = new_password.getText().toString();
        conf_new_pwd = confirm_new_password.getText().toString();

        if(new_pwd.compareTo(conf_new_pwd) != 0){
            Toast.makeText(ChangePasswordActivity.this, "Both passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if(new_pwd.length() < 3){
            Toast.makeText(ChangePasswordActivity.this, "Password should have minimum 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(ChangePasswordActivity.this, "Succesfully here", Toast.LENGTH_SHORT).show();


        first_if = false;
        second_if = false;
        change_password_attempted = false;

        webView.addJavascriptInterface(new CustomJavaScriptInterface(ChangePasswordActivity.this),"app");

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

                    //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();



                    if(!first_if && !change_password_attempted) {
                        first_if = true;
                        Log.d("first if here", "--");
                        webView.evaluateJavascript("document.getElementById('Oldkey').value = '" + current + "';", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                webView.evaluateJavascript("document.getElementById('Newkey').value = '" + new_pwd + "';", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        webView.evaluateJavascript("document.getElementById('Confirm').value = '" + conf_new_pwd + "';", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                change_password_attempted = true;
                                                webView.evaluateJavascript("document.getElementById('mainform').submit();", null);
                                                change_password_button.setEnabled(false);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else if(!second_if && change_password_attempted){
                        second_if=true;
                        Log.d("second if here","--");
                        webView.evaluateJavascript(" (function (){return !(!document.getElementsByClassName('alert alert-success')[0]);}()) ;", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.d("on recev val","--");
                                if(value.compareTo("false") == 0){
                                    Log.d("does this work?","hope so");
                                    change_password_attempted = false;
                                    Toast.makeText(ChangePasswordActivity.this, "Current Password is INCORRECT", Toast.LENGTH_SHORT).show();
                                    change_password_button.setEnabled(true);
                                }
                                else{
                                    change_password_attempted = false;
                                    Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    saveData1();
                                    CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                                        @Override
                                        public void onReceiveValue(Boolean aBoolean) {
                                            Intent i = new Intent(ChangePasswordActivity.this,MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    change_password_button.setEnabled(true);
                                }
                            }
                        });
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
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-passwd.pl");
    }

    private void saveData1()
    {
        SharedPreferences sharedpreferences1 = getSharedPreferences("isSignIn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
        editor1.putBoolean("isSignIn",false);
        editor1.commit();

    }
}
