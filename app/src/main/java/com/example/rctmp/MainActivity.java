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
    String username="",password="",username_emailid="";
    boolean first_if=false,second_if=false;
    Button sign_in_button;
    boolean sign_in_attempted = false,forgot_password_attempted=false;

    boolean isSignIn = false;
    boolean isDefaultView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readData();

        if(isSignIn)
        {
            //saveData();

            if(isDefaultView)
            {
                startActivity(new Intent(MainActivity.this, DrawerActivityLayout.class));
                finish();
            }
            else
            {
                startActivity(new Intent(MainActivity.this, ChangedLayoutActivity.class));
                finish();
            }

            return;

        }

//-------------------------------------------------------------------------------------------------------------------------------------------------
        sign_in_button = findViewById(R.id.bt_sign_in);
        webView = findViewById(R.id.wv_sign_in);
        username_field = findViewById(R.id.ti_et_student_id);
        password_field = findViewById(R.id.ti_et_password);

//------------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    public void onClickSignUp(View view) {

        if(TextUtils.isEmpty(username_field.getText()) || TextUtils.isEmpty(password_field.getText()))
        {
            Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        first_if = false;
        second_if = false;
        sign_in_attempted = false;

        webView.addJavascriptInterface(new CustomJavaScriptInterface(MainActivity.this),"app");


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



                    if(!first_if && !sign_in_attempted) {
                        first_if=true;
                        webView.evaluateJavascript("document.getElementById('userid').value = '" + username + "';", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //webView.evaluateJavascript("document.getElementById('password').value = '" + s_password + "';",);
                                webView.evaluateJavascript("document.getElementById('password').value = '" + password + "';", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        sign_in_attempted = true;
                                        webView.evaluateJavascript("document.getElementById('auth').submit();", null);
                                        sign_in_button.setEnabled(false);
                                    }
                                });

                            }
                        });
                    }
                    else if(!second_if && sign_in_attempted){
                        second_if=true;
                        webView.evaluateJavascript("var check = !(!(document.getElementById('userdetails')));", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                webView.evaluateJavascript("app.userCheck(check);", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        if (loginState) {
                                            saveData();
                                            Intent intent = new Intent(MainActivity.this,DrawerActivityLayout.class);
                                            intent.putExtra("ID",username);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(MainActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                                            sign_in_button.setEnabled(true);
                                        }
                                    }
                                });
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
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-user.pl");


    }

//-----------------------------------------------------------------------------------------------------------------------------------------------

    public void onClickForgetPassword(View view) {
        if(TextUtils.isEmpty(username_field.getText()))
        {
            Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "CLICK KIA Me", Toast.LENGTH_SHORT).show();
        username = username_field.getText().toString();
        username_emailid = username + "@daiict.ac.in";
        first_if = false;
        second_if = false;
        forgot_password_attempted = false;

        webView.addJavascriptInterface(new CustomJavaScriptInterface(MainActivity.this),"app");

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



                    if(!first_if && !forgot_password_attempted) {
                        first_if=true;
                        Log.d("first if here","--");
                        webView.evaluateJavascript("document.getElementById('username').value = '" + username + "';", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                webView.evaluateJavascript("document.getElementById('email').value = '" + username_emailid + "';", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {

                                        forgot_password_attempted = true;
                                        webView.evaluateJavascript("document.getElementsByName('sendEmail')[0].click();", null);
                                        sign_in_button.setEnabled(false);
                                    }
                                });

                            }
                        });
                    }
                    else if(!second_if && forgot_password_attempted){
                        second_if=true;
                        Log.d("second if here","--");
                        webView.evaluateJavascript(" (function (){return !(!(document.getElementsByClassName('alert alert-info')[0]))}()) ;", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.d("on recev val","--");
                                if(value.compareTo("false") == 0){
                                    Log.d("does this work?","hope so");
                                    forgot_password_attempted = false;
                                    Toast.makeText(MainActivity.this, "INCORRECT USERNAME. PLEASE CHANGE ", Toast.LENGTH_SHORT).show();
                                    sign_in_button.setEnabled(true);
                                }
                                else{
                                    forgot_password_attempted = false;
                                    Toast.makeText(MainActivity.this, "MAIL SENT. PLEASE FOLLOW LINK IN MAIL TO RESET PASSWORD", Toast.LENGTH_SHORT).show();
                                    sign_in_button.setEnabled(true);
                                }
//                                webView.evaluateJavascript("app.userCheck(check);", new ValueCallback<String>() {
//                                    @Override
//                                    public void onReceiveValue(String value) {
//                                        if (loginState) {
//                                            saveData();
//                                            Intent intent = new Intent(MainActivity.this,DrawerActivityLayout.class);
//                                            intent.putExtra("ID",username);
//                                            startActivity(intent);
//                                            finish();
//
//                                        } else {
//                                            Toast.makeText(MainActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
//                                            sign_in_button.setEnabled(true);
//                                        }
//                                    }
//                                });
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
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-password-recovery.pl");
    }

    private void readData()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("isSignIn", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        isSignIn = sharedpreferences.getBoolean("isSignIn",false);
        //editor.clear().commit();

        SharedPreferences preferences = getSharedPreferences("isDefaultView", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor1 = preferences.edit();
        isDefaultView = preferences.getBoolean("isDefaultView",true);
        //editor1.clear().commit();

    }

    private void saveData() {
        SharedPreferences sharedpreferences2 = getSharedPreferences("isSignIn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences2.edit();
        editor.putBoolean("isSignIn",true);
        editor.commit();

    }

//------------------------------------------------------------------------------------------------------------------------------------------------
}

