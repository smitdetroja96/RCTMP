package com.example.rctmp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbRequest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawerActivityLayout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    static boolean loginState;
    private WebView wv_check;
    private WebSettings webSettings;
    private boolean try_once=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        if(isInternet()) {

            wv_check = findViewById(R.id.wv_check);
            wv_check.addJavascriptInterface(new CustomJavaScriptInterface(DrawerActivityLayout.this), "app");

            wv_check.setWebViewClient(new WebViewClient() {
                boolean page_load_error = false;

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    page_load_error = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (page_load_error) {
                        Toast.makeText(DrawerActivityLayout.this, "Failed to Connect!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!try_once) return;
                        try_once = false;

                        wv_check.evaluateJavascript("var check2 = !(!(document.getElementById('userdetails')));", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                wv_check.evaluateJavascript("app.userCheck(check2);", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
                                        if (!loginState) {
                                            startActivity(new Intent(DrawerActivityLayout.this, MainActivity.class));
                                            finish();
                                        } else
                                            Log.d("DRAWER_TEST", "Yes");
                                    }
                                });
                            }
                        });
                    }

                }
            });

            webSettings = wv_check.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            wv_check.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-user.pl");

        }
        else
        {
            Snackbar.make(findViewById(R.id.drawer_layout),"No Internet !!!",Snackbar.LENGTH_SHORT).show();
        }

        //------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("RC");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //------------------------------------------------------------------------------------------------------------------------------------



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.change_password:
                Intent i1 = new Intent(DrawerActivityLayout.this,ChangePasswordActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_user_profile:
                Intent i2 = new Intent(DrawerActivityLayout.this, UserProfileActivity.class);
                startActivity(i2);
                break;
            case R.id.change_app_layout:
                saveData();
                Intent i = new Intent(DrawerActivityLayout.this,ChangedLayoutActivity.class);
                startActivity(i);
                finish();
                break;

        }
        return true;
    }

    public void onClickSearch(View view) {
        Intent i = new Intent(DrawerActivityLayout.this,IntermediateActivity.class);
        startActivity(i);
    }

    public void onClickIssuedBooks(View view) {
        Intent intent = new Intent(DrawerActivityLayout.this,IssuedBooksActivity.class);
        startActivity(intent);
    }

    public void onClickWishList(View view) {
        Toast.makeText(this,"WISH LIST",Toast.LENGTH_SHORT).show();
    }

    public void onClickHistory(View view) {
        Intent intent = new Intent(DrawerActivityLayout.this,ReadingHistoryActivity.class);
        startActivity(intent);
    }

    public void onClickSuggestions(View view) {
        Toast.makeText(this,"Suggestions",Toast.LENGTH_SHORT).show();
    }

    public void onClickSignOut(View view) {
        saveData1();
        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                Intent i = new Intent(DrawerActivityLayout.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void onClickRCDetails(View view) {

    }

    public void displayMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------


    public boolean isInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;

        return false;
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------
    private void saveData()
    {

        SharedPreferences sharedpreferences = getSharedPreferences("isDefaultView", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isDefaultView",false);
        editor.commit();

    }

    private void saveData1()
    {
        SharedPreferences sharedpreferences1 = getSharedPreferences("isSignIn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
        editor1.putBoolean("isSignIn",false);
        editor1.commit();

    }
//----------------------------------------------------------------------------------------------------------------------------------------------------------
}
