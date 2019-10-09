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
import com.google.android.gms.tasks.Tasks;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

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

    int curVer=1,active=0,latest=1;
    DatabaseReference databaseReference;


    ArrayList<BooksClass> books = new ArrayList<>();

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
        readBookData();
        if(books.size() == 0){
            saveDataBooks();
        }
        readData();

        databaseReference = FirebaseDatabase.getInstance().getReference("GoHere");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getKey().compareTo("active")==0)
                        active = child.getValue(Integer.class);
                    else
                        latest = child.getValue(Integer.class);
                }

                Log.e("hhhhhhhhhhhhhh",active+" "+latest);

                if(active == 0 && curVer != latest){
                    databaseReference = FirebaseDatabase.getInstance().getReference("Books");//.child(i+"");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            SharedPreferences pref = getSharedPreferences("curVer",Context.MODE_PRIVATE);
                            curVer = pref.getInt("curVer",0);

                            Log.e("HELL22",curVer+"");

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){


                                String currentChild = snapshot.getKey();
                                if(Integer.parseInt(currentChild) > curVer ){
                                    for(DataSnapshot snap : snapshot.getChildren()){
                                        BooksClass temp = snap.getValue(BooksClass.class);
                                        books.add(temp);
                                    }
                                }
                            }
                            saveDataBooks2();
                            Log.e("HELLO",curVer+"");
                            SharedPreferences preff = getSharedPreferences("curVer",Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = preff.edit();
                            edit.putInt("curVer",latest);
                            edit.commit();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

//        SharedPreferences pref = getSharedPreferences("curVer",Context.MODE_PRIVATE);
//        curVer = pref.getInt("curVer",1);

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

    private void readBookData()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        int numberOfAllBooks = sharedpreferences.getInt("numberOfBooks",0);

//        Toast.makeText(this, "" + numberOfAllBooks, Toast.LENGTH_SHORT).show();

        books.clear();

        for(int i=0;i<numberOfAllBooks;i++)
        {
            books.add(complexPreferences.getObject("Books"+Integer.toString(i),BooksClass.class));
        }

//        Toast.makeText(this, "" + books.size() + " hii", Toast.LENGTH_SHORT).show();
    }

    private void saveDataBooks()
    {

        SharedPreferences preferences = getSharedPreferences("CUR_DATE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String cur_date = df.format(date);

        editor1.putString("CUR_DATE",cur_date);

        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        complexPreferences.clearObject();

        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset(this));
            JSONObject books_obj = obj.getJSONObject("Books");
            JSONObject inside_obj = books_obj.getJSONObject("inside");
            Iterator<String> keys = inside_obj.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONObject current_obj = inside_obj.getJSONObject(key);
//                Log.d("Details-->", jo_inside.getString("formule"));
                BooksClass curr_book = new BooksClass();
                curr_book.setAuthors(current_obj.getString("authors"));
                curr_book.setBiblionumber(current_obj.getInt("biblionumber"));
                curr_book.setCallnumber(current_obj.getString("callnumber"));
                curr_book.setDescription(current_obj.getString("description"));
                curr_book.setIsbn(current_obj.getString("isbn"));
                curr_book.setMaterialType(current_obj.getString("materialType"));
                curr_book.setName(current_obj.getString("name"));
                curr_book.setPublisher(current_obj.getString("publisher"));
                curr_book.setQuantity(current_obj.getInt("quantity"));
                curr_book.setSubjects(current_obj.getString("subjects"));
                curr_book.setUrl(current_obj.getString("url"));

                //Add your values in your `ArrayList` as below:
                books.add(curr_book);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        editor.putInt("numberOfBooks",books.size());

        int i;

        for(i = 0 ; i < books.size() ; i++)
        {
            complexPreferences.putObject("Books" + Integer.toString(i), books.get(i));
        }

//        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();

        editor.commit();
        complexPreferences.commit();
        editor1.commit();
        Log.e("currentCount:",""+books.size());


        SharedPreferences pref = getSharedPreferences("curVer",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("curVer",1);
        edit.commit();
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
//            InputStream is = get
            InputStream is = context.getAssets().open("rcappsri-96aa2-export.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.e("hhhhhhhhhhhh","success");
        return json;
    }

    private void saveDataBooks2()
    {

        SharedPreferences preferences = getSharedPreferences("CUR_DATE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String cur_date = df.format(date);

        editor1.putString("CUR_DATE",cur_date);

        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        complexPreferences.clearObject();

        editor.putInt("numberOfBooks",books.size());

        int i;

        for(i = 0 ; i < books.size() ; i++)
        {
            complexPreferences.putObject("Books" + Integer.toString(i), books.get(i));
        }

//        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();

        editor.commit();
        complexPreferences.commit();
        editor1.commit();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
}