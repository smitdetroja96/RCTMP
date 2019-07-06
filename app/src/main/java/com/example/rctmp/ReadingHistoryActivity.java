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

public class ReadingHistoryActivity extends AppCompatActivity {

    ArrayList<HistoryBooksClass> MyHistory = new ArrayList<>();
    WebView wv;
    WebSettings webSettings;
    boolean try_once = false;
    RadioGroup radioGroup;
    ArrayList<BooksClass> books;
    DatabaseReference databaseReference;
    TextView textView;

    RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    HistoryListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_history);

//-------------------------------------------------------------------------------------------------------------------------------------
        toolbar = findViewById(R.id.toolbar_reading_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Reading History");
//-------------------------------------------------------------------------------------------------------------------------------------


        AlertDialog.Builder builder = new AlertDialog.Builder(ReadingHistoryActivity.this);
        builder.setCancelable(false);
//        TextView textView = findViewById(R.id.loader);
//        textView.setText("Fetching Books...");
        builder.setView(R.layout.progress_dialogue_layout_1);
        textView = findViewById(R.id.tv_empty_history);
        relativeLayout = findViewById(R.id.rl_sort_history);
        books = new ArrayList<>();
        readBookData();


        final AlertDialog dialog1 = builder.create();
        dialog1.show();
        Window window1 = dialog1.getWindow();
        if (window1 != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog1.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog1.getWindow().setAttributes(layoutParams);
        }

        dialog1.show();
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

                    recyclerView = findViewById(R.id.rv_history);

                    wv = findViewById(R.id.wv_history);

                    try_once = true;
                    wv.setWebViewClient(new WebViewClient(){
                        boolean page_load_error = false;
                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                            page_load_error=true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if(page_load_error) {
                                dialog1.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(!try_once)return;
                                try_once = false;

                                //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();
                                wv.evaluateJavascript("(function(){return window.document.body.outerHTML})();", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {

                                        //Substrings to be searched in the HTML page for retrieving biblionumber,title,author,check-in date
                                        String search_bib = "l.pl?biblionumber=";
                                        String search_date = "span title=";
                                        String search_author = "item-details";

                                        //index_x = index where substring search_x is present in HTML PAGE(string value)
                                        int index_bib = value.indexOf(search_bib);
                                        int index_date = value.indexOf(search_date);
                                        int index_author = value.indexOf(search_author);

                                        while(index_bib>=0 && index_date>=0 && index_author>=0) {
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
                                            j=j+3;
                                            p=value.charAt(j);
                                            while (p != '\\' && p!='\u003C')
                                            {
                                                title_string.append(p);
                                                j++;
                                                p=value.charAt(j);
                                            }
                                            while(true){
                                                char q = value.charAt(k);
                                                if(q==' ')break;
                                                date_string.append(q);
                                                k++;
                                            }
                                            while (true){
                                                char r = value.charAt(l);
                                                if(r=='\\' || r=='\u003C')break;
                                                author_string.append(r);
                                                l++;
                                            }
                                            String date_str = date_string.toString();
                                            //Check if the check-in date has value "Checked Out" (it is currently issued and not history)
                                            if(date_str.compareTo("Checked")!=0) {
                                                StringBuilder year = new StringBuilder();
                                                StringBuilder month = new StringBuilder();
                                                StringBuilder day = new StringBuilder();

                                                int change = 0;
                                                for (int i = 0; i < date_str.length(); i++) {
                                                    if (date_str.charAt(i) == '-') {
                                                        change++;
                                                    }
                                                    else {
                                                        char x = date_str.charAt(i);
                                                        if (change == 0) year.append(x);
                                                        else if (change == 1) month.append(x);
                                                        else day.append(x);
                                                    }
                                                }

                                                date_str = day.toString() + "/" + month.toString() + "/" + year.toString();
                                                int biblionumber = Integer.parseInt(biblio_string.toString());
                                                BooksClass bk = new BooksClass();
                                                for(int i=0 ; i<books.size() ; i++){
                                                    if(books.get(i).getBiblionumber() == biblionumber){
                                                        bk = books.get(i);
                                                        break;
                                                    }
                                                }
                                                HistoryBooksClass toinsert = new HistoryBooksClass(bk,date_str);
                                                MyHistory.add(toinsert);
                                            }
                                            //MyDateList.add(temp2.toString());
                                            //MyBibList.add(temp.toString());
                                            index_bib = value.indexOf(search_bib,index_bib+search_bib.length());
                                            index_date = value.indexOf(search_date,index_date+search_date.length());
                                            index_author = value.indexOf(search_author,index_author+search_author.length());
                                        }
                                        Log.d("count=",Integer.toString(MyHistory.size()));

//                            MyHistory.add(new HistoryBooksClass("123","An approach to Indian Society","Shreyansh Surana","12/12/2017"));
//                            MyHistory.add(new HistoryBooksClass("125","Busy Bastards On the Street","Smit Detroja","12/01/2017"));
//                            MyHistory.add(new HistoryBooksClass("128","One More approach to Indian Society","Shreyansh Surana","10/01/2019"));


                                        //Log.d("date=",Integer.toString(MyDateList.size()));
                                        dialog1.dismiss();
                                        if(MyHistory.size() == 0){
                                            textView.setVisibility(View.VISIBLE);
                                            relativeLayout.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                        else{
                                            Collections.sort(MyHistory,HistoryBooksClass.TitleComparator);
                                            layoutManager = new LinearLayoutManager(ReadingHistoryActivity.this);
                                            recyclerView.setLayoutManager(layoutManager);
                                            mAdapter = new HistoryListAdapter(MyHistory,ReadingHistoryActivity.this);
                                            recyclerView.setAdapter(mAdapter);
                                            textView.setVisibility(View.GONE);
                                            radioGroup = findViewById(R.id.rg_history_sort_choices);
                                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                    if(checkedId==R.id.rb_title_history){
                                                        Collections.sort(MyHistory,HistoryBooksClass.TitleComparator);
                                                        mAdapter.filterList(MyHistory);
                                                    }
                                                    else if(checkedId==R.id.rb_author_history){
                                                        Collections.sort(MyHistory,HistoryBooksClass.AuthorComparator);
                                                        mAdapter.filterList(MyHistory);
                                                    }
                                                    else{
                                                        Collections.sort(MyHistory,HistoryBooksClass.DateComparator);
                                                        mAdapter.filterList(MyHistory);
                                                    }
                                                }
                                            });
                                        }


                                        // */
                                        //Toast.makeText(ReadingHistory.this,MyDateList.get(0), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }

                    });

                    webSettings = wv.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setAllowContentAccess(true);
                    webSettings.setDomStorageEnabled(true);
                    webSettings.setDatabaseEnabled(true);
                    wv.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-readingrecord.pl?limit=full");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

//                    dialog.dismiss();
                    Toast.makeText(ReadingHistoryActivity.this, "Failed : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            recyclerView = findViewById(R.id.rv_history);

            wv = findViewById(R.id.wv_history);

            try_once = true;
            wv.setWebViewClient(new WebViewClient(){
                boolean page_load_error = false;
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    page_load_error=true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if(page_load_error) {
                        dialog1.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(!try_once)return;
                        try_once = false;

                        //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();
                        wv.evaluateJavascript("(function(){return window.document.body.outerHTML})();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {

                                //Substrings to be searched in the HTML page for retrieving biblionumber,title,author,check-in date
                                String search_bib = "l.pl?biblionumber=";
                                String search_date = "span title=";
                                String search_author = "item-details";

                                //index_x = index where substring search_x is present in HTML PAGE(string value)
                                int index_bib = value.indexOf(search_bib);
                                int index_date = value.indexOf(search_date);
                                int index_author = value.indexOf(search_author);

                                while(index_bib>=0 && index_date>=0 && index_author>=0) {
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
                                    j=j+3;
                                    p=value.charAt(j);
                                    while (p != '\\' && p!='\u003C')
                                    {
                                        title_string.append(p);
                                        j++;
                                        p=value.charAt(j);
                                    }
                                    while(true){
                                        char q = value.charAt(k);
                                        if(q==' ')break;
                                        date_string.append(q);
                                        k++;
                                    }
                                    while (true){
                                        char r = value.charAt(l);
                                        if(r=='\\' || r=='\u003C')break;
                                        author_string.append(r);
                                        l++;
                                    }
                                    String date_str = date_string.toString();
                                    //Check if the check-in date has value "Checked Out" (it is currently issued and not history)
                                    if(date_str.compareTo("Checked")!=0) {
                                        StringBuilder year = new StringBuilder();
                                        StringBuilder month = new StringBuilder();
                                        StringBuilder day = new StringBuilder();

                                        int change = 0;
                                        for (int i = 0; i < date_str.length(); i++) {
                                            if (date_str.charAt(i) == '-') {
                                                change++;
                                            }
                                            else {
                                                char x = date_str.charAt(i);
                                                if (change == 0) year.append(x);
                                                else if (change == 1) month.append(x);
                                                else day.append(x);
                                            }
                                        }

                                        date_str = day.toString() + "/" + month.toString() + "/" + year.toString();
                                        int biblionumber = Integer.parseInt(biblio_string.toString());
                                        BooksClass bk = new BooksClass();
                                        for(int i=0 ; i<books.size() ; i++){
                                            if(books.get(i).getBiblionumber() == biblionumber){
                                                bk = books.get(i);
                                                break;
                                            }
                                        }
                                        HistoryBooksClass toinsert = new HistoryBooksClass(bk,date_str);
                                        MyHistory.add(toinsert);
                                    }
                                    //MyDateList.add(temp2.toString());
                                    //MyBibList.add(temp.toString());
                                    index_bib = value.indexOf(search_bib,index_bib+search_bib.length());
                                    index_date = value.indexOf(search_date,index_date+search_date.length());
                                    index_author = value.indexOf(search_author,index_author+search_author.length());
                                }
                                Log.d("count=",Integer.toString(MyHistory.size()));

//                            MyHistory.add(new HistoryBooksClass("123","An approach to Indian Society","Shreyansh Surana","12/12/2017"));
//                            MyHistory.add(new HistoryBooksClass("125","Busy Bastards On the Street","Smit Detroja","12/01/2017"));
//                            MyHistory.add(new HistoryBooksClass("128","One More approach to Indian Society","Shreyansh Surana","10/01/2019"));


                                //Log.d("date=",Integer.toString(MyDateList.size()));
                                dialog1.dismiss();

                                Collections.sort(MyHistory,HistoryBooksClass.TitleComparator);
                                if(MyHistory.size() == 0){
                                    textView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.GONE);
                                }
                                else{
                                    Collections.sort(MyHistory,HistoryBooksClass.TitleComparator);
                                    layoutManager = new LinearLayoutManager(ReadingHistoryActivity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    mAdapter = new HistoryListAdapter(MyHistory,ReadingHistoryActivity.this);
                                    recyclerView.setAdapter(mAdapter);
                                    textView.setVisibility(View.GONE);
                                    radioGroup = findViewById(R.id.rg_history_sort_choices);
                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            if(checkedId==R.id.rb_title_history){
                                                Collections.sort(MyHistory,HistoryBooksClass.TitleComparator);
                                                mAdapter.filterList(MyHistory);
                                            }
                                            else if(checkedId==R.id.rb_author_history){
                                                Collections.sort(MyHistory,HistoryBooksClass.AuthorComparator);
                                                mAdapter.filterList(MyHistory);
                                            }
                                            else{
                                                Collections.sort(MyHistory,HistoryBooksClass.DateComparator);
                                                mAdapter.filterList(MyHistory);
                                            }
                                        }
                                    });
                                }

                                // */
                                //Toast.makeText(ReadingHistory.this,MyDateList.get(0), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

            });

            webSettings = wv.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            wv.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-readingrecord.pl?limit=full");
        }
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

        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();

        editor.commit();
        complexPreferences.commit();
        editor1.commit();
    }

    private void readBookData()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        int numberOfAllBooks = sharedpreferences.getInt("numberOfBooks",0);

        Toast.makeText(this, "" + numberOfAllBooks, Toast.LENGTH_SHORT).show();

        books.clear();

        for(int i=0;i<numberOfAllBooks;i++)
        {
            books.add(complexPreferences.getObject("Books"+Integer.toString(i),BooksClass.class));
        }

        Toast.makeText(this, "" + books.size() + " hii", Toast.LENGTH_SHORT).show();
    }
}
