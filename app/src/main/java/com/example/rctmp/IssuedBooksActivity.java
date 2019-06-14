package com.example.rctmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class IssuedBooksActivity extends AppCompatActivity {

    ArrayList<HistoryBooksClass> MyIssues = new ArrayList<>();
    WebView wv;
    WebSettings webSettings;
    RadioGroup radioGroup;
    boolean try_once = false;

    private RecyclerView recyclerView;
    private IssuedBooksAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        recyclerView = findViewById(R.id.rv_issued);

        wv = findViewById(R.id.wv_issued_book);

        try_once = true;
        wv.setWebViewClient(new WebViewClient(){
            boolean page_load_error = false;
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error=true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(page_load_error)
                    Toast.makeText(getApplicationContext(),"Failed to Connect!",Toast.LENGTH_SHORT).show();
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

                            //ind = index where substring search_bib is present in HTML PAGE(string value)
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
                                    if(q=='T')break;
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

                                    MyIssues.add(new HistoryBooksClass(biblio_string.toString(), title_string.toString(), author_string.toString(), date_str));
                                }
                                //MyDateList.add(temp2.toString());
                                //MyBibList.add(temp.toString());
                                index_bib = value.indexOf(search_bib,index_bib+search_bib.length());
                                index_date = value.indexOf(search_date,index_date+search_date.length());
                                index_author = value.indexOf(search_author,index_author+search_author.length());
                            }
                            MyIssues.add(new HistoryBooksClass("123","An approach to Indian Society","Shreyansh Surana","12/12/2017"));
                            MyIssues.add(new HistoryBooksClass("125","Busy Bastards On the Street","Smit Detroja","12/01/2017"));
                            MyIssues.add(new HistoryBooksClass("128","One More approach to Indian Society","Shreyansh Surana","10/01/2019"));
                            Log.d("count=",Integer.toString(MyIssues.size()));


                            //Log.d("date=",Integer.toString(MyDateList.size()));

                            Collections.sort(MyIssues,HistoryBooksClass.TitleComparator);
                            layoutManager = new LinearLayoutManager(IssuedBooksActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            mAdapter = new IssuedBooksAdapter(MyIssues);
                            recyclerView.setAdapter(mAdapter);

                            radioGroup = findViewById(R.id.rg_issues_sort_choices);
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    if(checkedId==R.id.rb_title_issue){
                                        Collections.sort(MyIssues,HistoryBooksClass.TitleComparator);
                                        mAdapter.filterList(MyIssues);
                                    }
                                    else if(checkedId==R.id.rb_author_issue){
                                        Collections.sort(MyIssues,HistoryBooksClass.AuthorComparator);
                                        mAdapter.filterList(MyIssues);
                                    }
                                    else{
                                        Collections.sort(MyIssues,HistoryBooksClass.DateComparator);
                                        mAdapter.filterList(MyIssues);
                                    }
                                }
                            });

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
        wv.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-user.pl");


    }
}
