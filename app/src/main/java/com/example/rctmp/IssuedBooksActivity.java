package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class IssuedBooksActivity extends AppCompatActivity {

    ArrayList<HistoryBooksClass> MyIssues = new ArrayList<>();
    WebView wv;
    WebSettings webSettings;
    RadioGroup radioGroup;
    ArrayList<BooksClass> books = new ArrayList<>();
    boolean try_once = false;

    private RecyclerView recyclerView;
    private IssuedBooksAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        readBookData();

        AlertDialog.Builder builder = new AlertDialog.Builder(IssuedBooksActivity.this);
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
                if(page_load_error) {
                    dialog.dismiss();
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
                                    int biblionumber = Integer.parseInt(biblio_string.toString());
                                    BooksClass bk = new BooksClass();
                                    for(int i=0 ; i<books.size() ; i++){
                                        if(books.get(i).getBiblionumber() == biblionumber){
                                            bk = books.get(i);
                                            break;
                                        }
                                    }
                                    MyIssues.add(new HistoryBooksClass(bk,date_str));
//                                    MyIssues.add(new HistoryBooksClass(biblio_string.toString(), title_string.toString(), author_string.toString(), date_str));
                                }
                                //MyDateList.add(temp2.toString());
                                //MyBibList.add(temp.toString());
                                index_bib = value.indexOf(search_bib,index_bib+search_bib.length());
                                index_date = value.indexOf(search_date,index_date+search_date.length());
                                index_author = value.indexOf(search_author,index_author+search_author.length());
                            }
//                            MyIssues.add(new HistoryBooksClass("123","An approach to Indian Society","Shreyansh Surana","12/12/2017"));
//                            MyIssues.add(new HistoryBooksClass("125","Busy Bastards On the Street","Smit Detroja","12/01/2017"));
//                            MyIssues.add(new HistoryBooksClass("128","One More approach to Indian Society","Shreyansh Surana","10/01/2019"));
                            Log.d("count=",Integer.toString(MyIssues.size()));


                            //Log.d("date=",Integer.toString(MyDateList.size()));
                            dialog.dismiss();

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
                                        Collections.reverse(MyIssues);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == 1){}
        else if(item.getItemId() == 2){
            Toast.makeText(this, ""+item.getGroupId(), Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(IssuedBooksActivity.this,ViewDetails.class);
            intent1.putExtra("BookDetails",MyIssues.get(item.getGroupId()).getMybook());
            intent1.putExtra("isMain",true);
            startActivity(intent1);
        }

        return super.onContextItemSelected(item);
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
