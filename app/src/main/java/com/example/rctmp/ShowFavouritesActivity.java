package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class ShowFavouritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    FavouritesAdapter adapter;
    ArrayList<BooksClass> books = new ArrayList<>();
    ArrayList<BooksClass> tempbooks = new ArrayList<>();
    WebView webView;
    WebSettings webSettings;
    Boolean loadNextPage = false;
    TextView textView;
    RadioGroup radioGroup;
    DatabaseReference databaseReference;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favourites);

        webView = findViewById(R.id.wv_fav);
        relativeLayout = findViewById(R.id.rl_sort_fav);
        recyclerView = findViewById(R.id.rv_favourites);
        textView = findViewById(R.id.tv_empty_list);

//-------------------------------------------------------------------------------------------------------------------------------------
        toolbar = findViewById(R.id.toolbar_favourites_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Favourites");
//-------------------------------------------------------------------------------------------------------------------------------------


        readBookData();

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowFavouritesActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_dialogue_layout_1);

        final AlertDialog dialog = builder.create();

        Window window1 = dialog.getWindow();
        if (window1 != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.show();

        if(books.size() == 0){
            databaseReference = FirebaseDatabase.getInstance().getReference("Books").child("inside");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BooksClass temp = snapshot.getValue(BooksClass.class);
                        books.add(temp);
                    }

                    saveData();
                    webView.setWebViewClient(new WebViewClient(){
                        boolean page_load_error = false;
                        boolean done = false;
                        boolean secondif = false;

                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                            page_load_error = true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if(page_load_error){
                                Toast.makeText(ShowFavouritesActivity.this, "Failed to Connect !", Toast.LENGTH_SHORT).show();
                            }
                            else if(!done && !loadNextPage){
                                done = true;
                                webView.evaluateJavascript("(function(){return (document.getElementsByClassName('table')).length;})();", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
                                        Log.d("hello1:",s);
                                        if(s.equals("0") || s==null){
                                            textView.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                            relativeLayout.setVisibility(View.GONE);
                                            dialog.dismiss();
                                        }
                                        else{
                                            String script= "(function(){\n" +
                                                    "\tvar x = document.getElementsByClassName('table')[0].rows.length;\n" +
                                                    "  var temp = document.getElementsByClassName('table')[0];\n" +
                                                    "  var search = 'rc_app_list';\n" +
                                                    "  for(var i=1 ; i<x ; i++){\n" +
                                                    "  \tif(temp.rows[i].cells[0].innerText == search){\n" +
                                                    "    \treturn i;\n" +
                                                    "    }\n" +
                                                    "  }\n" +
                                                    "  return 0;\n" +
                                                    "})();";
                                            webView.evaluateJavascript(script, new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String s) {
                                                    Log.d("hello3:",s);
                                                    if(s.equals("0")){

                                                        textView.setVisibility(View.VISIBLE);
                                                        recyclerView.setVisibility(View.GONE);
                                                        relativeLayout.setVisibility(View.GONE);
                                                        dialog.dismiss();
                                                    }
                                                    else{
                                                        String newscript = "(document.getElementsByClassName('table')[0].rows["+s+"].cells[0]).firstChild.click();";
                                                        webView.evaluateJavascript(newscript, new ValueCallback<String>() {
                                                            @Override
                                                            public void onReceiveValue(String s) {
                                                                Toast.makeText(ShowFavouritesActivity.this, "Opening lists...", Toast.LENGTH_SHORT).show();
                                                                loadNextPage = true;
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            else if(loadNextPage && !secondif){
                                secondif = true;
                                webView.evaluateJavascript("document.querySelector('div.maincontent > h2').textContent;", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
//                            Toast.makeText(ShowActivity.this, s, Toast.LENGTH_SHORT).show();
                                        Log.d("fffff1",s);
                                        webView.evaluateJavascript("(document.getElementsByClassName('table')).length;", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String s) {
//                                    if(s==null)
                                                if(s.equals("0")){

                                                    textView.setVisibility(View.VISIBLE);
                                                    recyclerView.setVisibility(View.GONE);
                                                    relativeLayout.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                }
                                                else{
                                                    webView.evaluateJavascript("", new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String s) {
                                                            String script= "(function(){\n" +
                                                                    "\tvar x = document.getElementsByClassName('table')[0].rows.length;\n" +
                                                                    "  var temp = document.getElementsByClassName('table')[0];\n" +
                                                                    "  var toret='';\n" +
                                                                    "  for(var i=0 ; i<x ; i++){\n" +
                                                                    "  \tvar xx = temp.rows[i].cells[0].firstChild.value;" +
                                                                    "toret += xx; toret += 'xxxxx';\n" +
                                                                    "  }\n" +
                                                                    "  return toret;\n" +
                                                                    "})();";
                                                            webView.evaluateJavascript(script, new ValueCallback<String>() {
                                                                @Override
                                                                public void onReceiveValue(String s) {
                                                                    s = s.substring(1,s.length()-1);
                                                                    Log.d("finalllll",s);
                                                                    String[] myarray = s.split("xxxxx");
                                                                    Log.d("finalllll/",s.length()+" "+myarray.length);
                                                                    int i=0;
                                                                    tempbooks.clear();
                                                                    for(i=0 ; i<myarray.length ; i++){
                                                                        int val = Integer.parseInt(myarray[i]);
                                                                        Log.d("finalllol",""+val);
                                                                        for(int j=0 ; j<books.size() ; j++){
                                                                            if(books.get(j).getBiblionumber() == val){
                                                                                tempbooks.add(books.get(j));
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    Collections.sort(tempbooks,BooksClass.TitleComparator);
                                                                    recyclerView.setLayoutManager(new LinearLayoutManager(ShowFavouritesActivity.this));
                                                                    adapter = new FavouritesAdapter(ShowFavouritesActivity.this, tempbooks);
                                                                    recyclerView.setAdapter(adapter);
                                                                    textView.setVisibility(View.GONE);

                                                                    radioGroup = findViewById(R.id.rg_fav_sort_choices);
                                                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                        @Override
                                                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                                            if(checkedId==R.id.rb_title_favorite){
                                                                                Collections.sort(tempbooks,BooksClass.TitleComparator);
                                                                                adapter.filterList(tempbooks);
                                                                            }
                                                                            else if(checkedId==R.id.rb_author_favorite){
                                                                                Collections.sort(tempbooks,BooksClass.AuthorComparator);
                                                                                adapter.filterList(tempbooks);
                                                                            }
                                                                        }
                                                                    });
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });

                    webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setAllowContentAccess(true);
                    webSettings.setDomStorageEnabled(true);
                    webSettings.setDatabaseEnabled(true);
                    webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-shelves.pl?op=list&category=1");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    dialog.dismiss();
                    Toast.makeText(ShowFavouritesActivity.this, "Failed : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{
            webView.setWebViewClient(new WebViewClient(){
                boolean page_load_error = false;
                boolean done = false;
                boolean secondif = false;

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    page_load_error = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if(page_load_error){
                        Toast.makeText(ShowFavouritesActivity.this, "Failed to Connect !", Toast.LENGTH_SHORT).show();
                    }
                    else if(!done && !loadNextPage){
                        done = true;
                        webView.evaluateJavascript("(function(){return (document.getElementsByClassName('table')).length;})();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.d("hello1:",s);
                                if(s.equals("0") || s==null){
                                    textView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }
                                else{
                                    String script= "(function(){\n" +
                                            "\tvar x = document.getElementsByClassName('table')[0].rows.length;\n" +
                                            "  var temp = document.getElementsByClassName('table')[0];\n" +
                                            "  var search = 'rc_app_list';\n" +
                                            "  for(var i=1 ; i<x ; i++){\n" +
                                            "  \tif(temp.rows[i].cells[0].innerText == search){\n" +
                                            "    \treturn i;\n" +
                                            "    }\n" +
                                            "  }\n" +
                                            "  return 0;\n" +
                                            "})();";
                                    webView.evaluateJavascript(script, new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String s) {
                                            Log.d("hello3:",s);
                                            if(s.equals("0")){
                                                textView.setVisibility(View.VISIBLE);
                                                recyclerView.setVisibility(View.GONE);
                                                relativeLayout.setVisibility(View.GONE);
                                                dialog.dismiss();
                                            }
                                            else{
                                                String newscript = "(document.getElementsByClassName('table')[0].rows["+s+"].cells[0]).firstChild.click();";
                                                webView.evaluateJavascript(newscript, new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String s) {
                                                        Toast.makeText(ShowFavouritesActivity.this, "Opening lists...", Toast.LENGTH_SHORT).show();
                                                        loadNextPage = true;
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else if(loadNextPage && !secondif){
                        secondif = true;
                        webView.evaluateJavascript("document.querySelector('div.maincontent > h2').textContent;", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
//                            Toast.makeText(ShowActivity.this, s, Toast.LENGTH_SHORT).show();
                                Log.d("fffff1",s);
                                webView.evaluateJavascript("(document.getElementsByClassName('table')).length;", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
//                                    if(s==null)
                                        if(s.equals("0")){
                                            textView.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                            relativeLayout.setVisibility(View.GONE);
                                            dialog.dismiss();
                                        }
                                        else{
                                            webView.evaluateJavascript("", new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String s) {
                                                    String script= "(function(){\n" +
                                                            "\tvar x = document.getElementsByClassName('table')[0].rows.length;\n" +
                                                            "  var temp = document.getElementsByClassName('table')[0];\n" +
                                                            "  var toret='';\n" +
                                                            "  for(var i=0 ; i<x ; i++){\n" +
                                                            "  \tvar xx = temp.rows[i].cells[0].firstChild.value;" +
                                                            "toret += xx; toret += 'xxxxx';\n" +
                                                            "  }\n" +
                                                            "  return toret;\n" +
                                                            "})();";
                                                    webView.evaluateJavascript(script, new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String s) {
                                                            s = s.substring(1,s.length()-1);
                                                            Log.d("finalllll",s);
                                                            String[] myarray = s.split("xxxxx");
                                                            Log.d("finalllll/",s.length()+" "+myarray.length);
                                                            int i=0;
                                                            tempbooks.clear();
                                                            for(i=0 ; i<myarray.length ; i++){
                                                                int val = Integer.parseInt(myarray[i]);
                                                                Log.d("finalllol",""+val);
                                                                for(int j=0 ; j<books.size() ; j++){
                                                                    if(books.get(j).getBiblionumber() == val){
                                                                        tempbooks.add(books.get(j));
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            Collections.sort(tempbooks,BooksClass.TitleComparator);
                                                            recyclerView.setLayoutManager(new LinearLayoutManager(ShowFavouritesActivity.this));
                                                            adapter = new FavouritesAdapter(ShowFavouritesActivity.this, tempbooks);
                                                            recyclerView.setAdapter(adapter);
                                                            textView.setVisibility(View.GONE);

                                                            radioGroup = findViewById(R.id.rg_fav_sort_choices);
                                                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                @Override
                                                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                                    if(checkedId==R.id.rb_title_favorite){
                                                                        Collections.sort(tempbooks,BooksClass.TitleComparator);
                                                                        adapter.filterList(tempbooks);
                                                                    }
                                                                    else if(checkedId==R.id.rb_author_favorite){
                                                                        Collections.sort(tempbooks,BooksClass.AuthorComparator);
                                                                        adapter.filterList(tempbooks);
                                                                    }
                                                                }
                                                            });
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });

            webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-shelves.pl?op=list&category=1");

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

    private void saveData()
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

}
