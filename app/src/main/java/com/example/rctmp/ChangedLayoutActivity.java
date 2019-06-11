package com.example.rctmp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangedLayoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout search,issuedBooks,history,wishList,suggestions,signOut;
    private DrawerLayout drawer;

    static boolean loginState;
    private WebView wv_check;
    private WebSettings webSettings;
    private boolean try_once=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changed_layout);
        search = findViewById(R.id.ll_search);
        issuedBooks = findViewById(R.id.ll_issuedBooks);
        history = findViewById(R.id.ll_history);
        wishList = findViewById(R.id.ll_wishList);
        suggestions = findViewById(R.id.ll_suggestions);
        signOut = findViewById(R.id.ll_signOut);

        wv_check = findViewById(R.id.wv_check_cl);
        wv_check.addJavascriptInterface(new CustomJavaScriptInterface(ChangedLayoutActivity.this),"app");

        wv_check.setWebViewClient(new WebViewClient(){
            boolean page_load_error = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(page_load_error){
                    Toast.makeText(ChangedLayoutActivity.this, "Failed to Connect!", Toast.LENGTH_SHORT).show(); }
                else{
                    if(!try_once) return;
                    try_once = false;

                    wv_check.evaluateJavascript("var check2 = !(!(document.getElementById('userdetails')));", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            wv_check.evaluateJavascript("app.userCheck(check2);", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    if(!loginState){
                                        startActivity(new Intent(ChangedLayoutActivity.this,MainActivity.class));
                                        finish();
                                    }
                                    else
                                        Log.d("CHANGED_TEST","Yes");
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








//------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("RC");

        drawer = findViewById(R.id.drawer_layout_1);

        NavigationView navigationView = findViewById(R.id.nav_view_1);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

//--------------------------------------------------------------------------------------------------------------------------------------------
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangedLayoutActivity.this,IntermediateActivity.class);
                startActivity(i);
            }
        });

        issuedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChangedLayoutActivity.this, "Issued Books", Toast.LENGTH_SHORT).show();

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"History",Toast.LENGTH_SHORT).show();
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Wishlist",Toast.LENGTH_SHORT).show();
            }
        });

        suggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Suggestions",Toast.LENGTH_SHORT).show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData1();
                CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean aBoolean) {
                        Intent i = new Intent(ChangedLayoutActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.change_password:
                Intent i1 = new Intent(ChangedLayoutActivity.this,ChangePasswordActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_user_profile:
                displayMessage("Smit");
                break;
            case R.id.change_app_layout:
                saveData();
                Intent i = new Intent(ChangedLayoutActivity.this,DrawerActivityLayout.class);
                startActivity(i);
                finish();
                break;

        }
        return true;
    }

    public void displayMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveData()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("isDefaultView", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isDefaultView",true);
        editor.commit();
    }

    private void saveData1()
    {
        SharedPreferences sharedpreferences1 = getSharedPreferences("isSignIn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
        editor1.putBoolean("isSignIn",false);
        editor1.commit();

    }

}
