package com.example.rctmp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DrawerActivityLayout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    static boolean loginState;
    private WebView wv_check,webview_alarm;
    private WebSettings webSettings,webSettings2;
    private boolean try_once=true, try_once_alarm = true;
    String user_id;
    ArrayList<HistoryBooksClass> MyIssues = new ArrayList<>();
    TextView userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        readData();

        //***********************************************************************************************

        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivityLayout.this);
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
                        dialog.dismiss();
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
                                            dialog.dismiss();
                                            saveData1();
                                            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                                                @Override
                                                public void onReceiveValue(Boolean aBoolean) {
                                                    Intent i = new Intent(DrawerActivityLayout.this,MainActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                        } else {
                                            Log.d("DRAWER_TEST", "Yes");
                                        }
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
            dialog.dismiss();
            Snackbar.make(findViewById(R.id.drawer_layout),"No Internet !!!",Snackbar.LENGTH_SHORT).show();
        }

        //------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("RC");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        userId = view.findViewById(R.id.nav_header_textView);

        userId.setText(user_id);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //------------------------------------------------------------------------------------------------------------------------------------

        if(isInternet())
        {
            webview_alarm = findViewById(R.id.wv_alarm);

            webview_alarm.setWebViewClient(new WebViewClient() {
                boolean page_load_error = false;

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    page_load_error = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (page_load_error) {
                        dialog.dismiss();
                        Toast.makeText(DrawerActivityLayout.this, "Failed to Connect!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!try_once_alarm) return;
                        try_once_alarm = false;

                        webview_alarm.evaluateJavascript("(function(){return window.document.body.outerHTML})();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {

                                //Substrings to be searched in the HTML page for retrieving biblionumber,title,author,check-in date
                                String search_bib = "l.pl?biblionumber=";
                                String search_date = "span title=";
                                String search_author = "item-details";

                                //ind = index where substring search_bib is present in HTML PAGE(string value)
                                int index_bib = value.indexOf(search_bib);
                                int index_date = value.indexOf(search_date);
                                int index_author = value.indexOf(search_author);

                                while (index_bib >= 0 && index_date >= 0 && index_author >= 0) {
                                    StringBuilder biblio_string = new StringBuilder();
                                    StringBuilder date_string = new StringBuilder();
                                    StringBuilder title_string = new StringBuilder();
                                    StringBuilder author_string = new StringBuilder();

                                    int j = index_bib + search_bib.length();
                                    int k = index_date + search_date.length() + 2;
                                    int l = index_author + search_author.length() + 3;

                                    char p;
                                    while (true) {
                                        p = value.charAt(j);
                                        if (Character.isDigit(p))
                                            biblio_string.append(p);
                                        else break;
                                        j++;
                                    }
                                    j = j + 3;
                                    p = value.charAt(j);
                                    while (p != '\\' && p != '\u003C') {
                                        title_string.append(p);
                                        j++;
                                        p = value.charAt(j);
                                    }
                                    while (true) {
                                        char q = value.charAt(k);
                                        if (q == 'T') break;
                                        date_string.append(q);
                                        k++;
                                    }
                                    while (true) {
                                        char r = value.charAt(l);
                                        if (r == '\\' || r == '\u003C') break;
                                        author_string.append(r);
                                        l++;
                                    }
                                    String date_str = date_string.toString();
                                    if (date_str.compareTo("Checked") != 0) {
                                        StringBuilder year = new StringBuilder();
                                        StringBuilder month = new StringBuilder();
                                        StringBuilder day = new StringBuilder();

                                        int change = 0;
                                        for (int i = 0; i < date_str.length(); i++) {
                                            if (date_str.charAt(i) == '-') {
                                                change++;
                                            } else {
                                                char x = date_str.charAt(i);
                                                if (change == 0) year.append(x);
                                                else if (change == 1) month.append(x);
                                                else day.append(x);
                                            }
                                        }

                                        date_str = day.toString() + "/" + month.toString() + "/" + year.toString();
                                        BooksClass bk = new BooksClass();
                                        bk.setBiblionumber(Integer.parseInt(biblio_string.toString()));
                                        bk.setName(title_string.toString());
                                        bk.setAuthors(author_string.toString());
//                                        MyIssues.add(new HistoryBooksClass(bk, date_str));
                                        MyIssues.add(new HistoryBooksClass(bk, "06/07/2019"));
                                    }
                                    //MyDateList.add(temp2.toString());
                                    //MyBibList.add(temp.toString());
                                    index_bib = value.indexOf(search_bib, index_bib + search_bib.length());
                                    index_date = value.indexOf(search_date, index_date + search_date.length());
                                    index_author = value.indexOf(search_author, index_author + search_author.length());
                                }

//                                MyIssues.add(new HistoryBooksClass(boo,"21/06/2019"));
//                                MyIssues.add(new HistoryBooksClass("125", "Busy Bastards On the Street", "Smit Detroja", "21/06/2019"));
//                                MyIssues.add(new HistoryBooksClass("128", "One More approach to Indian Society", "Shreyansh Surana", "21/06/2019"));

                                for(HistoryBooksClass i : MyIssues)
                                {
                                    try {
                                        Date d = new SimpleDateFormat("dd/MM/yyyy").parse(i.checkInDate);

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(d);
                                        calendar.set(Calendar.HOUR_OF_DAY,21);
                                        calendar.set(Calendar.MINUTE,21);

                                        AlarmManager alarmManager;
                                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                                        Intent intent;

                                        intent = new Intent(DrawerActivityLayout.this,AlarmReceiver.class);
                                        intent.putExtra("title",i.getMybook().getName());
                                        intent.putExtra("check_in_date",i.getCheckInDate());

                                        int requestCode = (i.getMybook().biblionumber);
                                        intent.putExtra("requestCode",requestCode);


                                        PendingIntent pendingIntent;
                                        pendingIntent = PendingIntent.getBroadcast(DrawerActivityLayout.this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);





                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }

                                dialog.dismiss();

                                Log.d("count=", Integer.toString(MyIssues.size()));


                            }
                        });
                    }
                }
            });

            webSettings2 = webview_alarm.getSettings();
            webSettings2.setJavaScriptEnabled(true);
            webSettings2.setAllowContentAccess(true);
            webSettings2.setDomStorageEnabled(true);
            webSettings2.setDatabaseEnabled(true);
            webview_alarm.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-user.pl");

        }
        else
        {
            dialog.dismiss();
            Snackbar.make(findViewById(R.id.drawer_layout),"No Internet !!!",Snackbar.LENGTH_SHORT).show();
        }

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
        Intent intent = new Intent(DrawerActivityLayout.this,SuggestionsActivity.class);
        startActivity(intent);
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

    public void onClickMyFavourites(View view) {
        Intent intent = new Intent(DrawerActivityLayout.this,ShowFavouritesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DrawerActivityLayout.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void onClickRCDetails(View view) {
        Intent newIntent = new Intent(DrawerActivityLayout.this,ShowDetailsActivity.class);
        startActivity(newIntent);
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

    private void readData()
    {
        SharedPreferences preferences = getSharedPreferences("ID",Context.MODE_PRIVATE);
        user_id = preferences.getString("ID","No user");
    }

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

        SharedPreferences sharedPreferences = getSharedPreferences("ID",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID","No user");
        editor.commit();

    }


//----------------------------------------------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------------------------------------------
}
