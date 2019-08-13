package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import junit.framework.Test;

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

        Intent intent = getIntent();
        Log.e("errrrrr:","hello" );
        if (intent.hasExtra("ShowNotificationActivity")) {
            Log.e("pppppppppppp","comone");
            String url = intent.getStringExtra("url");
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            Log.e("thisOpens:",url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(browserIntent);
            finish();
//            handleFirebaseNotificationIntent(intent);
//            String url = intent.getStringExtra("url");
//            Intent i1 = new Intent(MainActivity.this, TestActivity.class);
////            i1.putExtra("url",url);
//            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            Log.e("rttttttttt","work!please");
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "YES";
                        if (!task.isSuccessful()) {
                            msg = "NO";
                        }
                        Log.d("TAG", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("general1")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "YES";
                        if (!task.isSuccessful()) {
                            msg = "NO";
                        }
                        Log.d("TAG", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

//        CookieManager.getInstance().removeAllCookies(null);
        readData();

        if(!isInternet())
        {
            Snackbar.make(findViewById(R.id.root_layout_main_activity),"No Internet !!!!",Snackbar.LENGTH_SHORT).show();
        }

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
        username_field.setText("201601112");
        password_field = findViewById(R.id.ti_et_password);
        password_field.setText("abc");

//------------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    public void onClickSignUp(View view) {

        if(!isInternet())
        {
            Snackbar.make(findViewById(R.id.root_layout_main_activity),"No Internet !!!!",Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(username_field.getText()) || TextUtils.isEmpty(password_field.getText()))
        {
            Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        String ans = "123";
        first_if = false;
        second_if = false;
        sign_in_attempted = false;

        //***********************************************************************************************

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_dialogue_layout);

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

        webView.addJavascriptInterface(new CustomJavaScriptInterface(MainActivity.this),"app");

        webView.setWebViewClient(new WebViewClient() {
            boolean page_load_error = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (page_load_error) {

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();

                }
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
                                            dialog.dismiss();
                                            saveData();
                                            Intent intent = new Intent(MainActivity.this,DrawerActivityLayout.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            dialog.dismiss();
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

        if(!isInternet())
        {
            Snackbar.make(findViewById(R.id.root_layout_main_activity),"No Internet !!!!",Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(username_field.getText()))
        {
            Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Sending Mail to Email-ID", Toast.LENGTH_SHORT).show();
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
                                    webView.evaluateJavascript("(function (){ return !(!(document.getElementsByClassName('alert alert-warning')[0]))}()) ;", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            if(value.compareTo("true") == 0){
                                                forgot_password_attempted = false;
                                                Toast.makeText(MainActivity.this, "Mail has already been sent. Please Check Again. ", Toast.LENGTH_SHORT).show();
                                                sign_in_button.setEnabled(true);
                                            }
                                            else{
                                                forgot_password_attempted = false;
                                                Toast.makeText(MainActivity.this, "INCORRECT USERNAME. PLEASE CHANGE ", Toast.LENGTH_SHORT).show();
                                                sign_in_button.setEnabled(true);
                                            }
                                        }
                                    });
                                }
                                else{
                                    forgot_password_attempted = false;
                                    Toast.makeText(MainActivity.this, "MAIL SENT. PLEASE FOLLOW LINK IN MAIL TO RESET PASSWORD", Toast.LENGTH_SHORT).show();
                                    sign_in_button.setEnabled(true);
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
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-password-recovery.pl");
    }

//------------------------------------------------------------------------------------------------------------------------------------------------------------

    public boolean isInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;

        return false;
    }


//------------------------------------------------------------------------------------------------------------------------------------------------------------
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

        SharedPreferences preferences = getSharedPreferences("ID",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();
        editor1.putString("ID",username);
        editor1.commit();

    }

    private void handleFirebaseNotificationIntent(Intent intent){
        String className = intent.getStringExtra("ShowNotificationActivity");
        Log.e("herewereached","yO");
        startSelectedActivity(className, intent.getStringExtra("url"));
    }

    private void startSelectedActivity(String className, String extras){
        Class cls;
        try {
            cls = Class.forName(className);
        }catch(ClassNotFoundException e){
            return;
        }
        Intent i = new Intent(MainActivity.this, cls);
        Log.e("pleaseworkbro",extras);
        if (i != null && extras.length()>0) {
            i.putExtra("url",extras);
            this.startActivity(i);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
}

