package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SuggestionsActivity extends AppCompatActivity {

    ArrayList<BooksClass> books,toshowbooks;
    HashSet<String> Tags;
    ArrayList<SuggestionsClass> tag_list;
    ArrayList<HistoryBooksClass> MyHistory;
    HashMap<String,String> aging_bits;
    HashSet<String> main_tags;
    ArrayList<SuggestionsBookCount> final_suggested_books;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    RecommendationAdapter adapter;


    WebView webView;
    WebSettings webSettings;
    boolean try_once;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        webView = findViewById(R.id.wv_suggestion_history);

        books = new ArrayList<>();
        toshowbooks = new ArrayList<>();
        Tags = new HashSet<>();
        tag_list = new ArrayList<>();
        MyHistory = new ArrayList<>();
        aging_bits = new HashMap<>();
        main_tags = new HashSet<>();
        final_suggested_books = new ArrayList<>();

//-------------------------------------------------------------------------------------------------------------------------------------
        toolbar = findViewById(R.id.toolbar_suggestions_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Recommended Books");
//-------------------------------------------------------------------------------------------------------------------------------------

        recyclerView = findViewById(R.id.recycler_view_recommend);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        readBookData();

        AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionsActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_dialogue_layout_1);

        final AlertDialog dialog = builder.create();
        dialog.show();
        Window window1 = dialog.getWindow();
        if (window1 != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
        dialog.show();

        if(books.size()==0){
            databaseReference = FirebaseDatabase.getInstance().getReference("Books").child("inside");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BooksClass temp = snapshot.getValue(BooksClass.class);
                        books.add(temp);
                    }

                    saveData();
                    try_once = true;

                    webView.setWebViewClient(new WebViewClient() {
                        boolean page_load_error = false;

                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                dialog.dismiss();
                            page_load_error = true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if (page_load_error) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!try_once) {
                                    return;
                                }
                                try_once = false;
                                Log.d("PAGE LOADED", "SUCCESS");
                                //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();
                                webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();", new ValueCallback<String>() {
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

                                        while (index_bib >= 0 && index_date >= 0 && index_author >= 0) {
                                            Log.d("a", "b");
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
                                                if (q == ' ') break;
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
                                            //Check if the check-in date has value "Checked Out" (it is currently issued and not history)
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
                                                int biblionumber = Integer.parseInt(biblio_string.toString());
                                                BooksClass bk = new BooksClass();
                                                for (int i = 0; i < books.size(); i++) {
                                                    if (books.get(i).getBiblionumber() == biblionumber) {
                                                        bk = books.get(i);
                                                        break;
                                                    }
                                                }
                                                HistoryBooksClass toinsert = new HistoryBooksClass(bk, date_str);
                                                MyHistory.add(toinsert);
                                            }
                                            index_bib = value.indexOf(search_bib, index_bib + search_bib.length());
                                            index_date = value.indexOf(search_date, index_date + search_date.length());
                                            index_author = value.indexOf(search_author, index_author + search_author.length());

                                        }
                                        Log.d("MYHISTORY LOADED", "SUCCESS");

                                        //////////////////////////////////////

                                        extractTags();

                                        Iterator it = Tags.iterator();

                                        Log.e("Books Size", "" + books.size());

                                        Log.e("Tags Size", "" + Tags.size());

                                        while (it.hasNext()) {
                                            aging_bits.put((String) it.next(), "0000000000");
                                        }

                                        Log.e("Aging Bits Size", "" + aging_bits.size());

                                        Collections.sort(MyHistory, HistoryBooksClass.DateComparator);

                                        Log.e("My History Size", "" + MyHistory.size());

                                        for (int i = 0; i < MyHistory.size(); i++) {
                                            BooksClass temp_book = MyHistory.get(i).getMybook();

                                            String s = temp_book.getSubjects();
                                            String[] tokens = s.split(" \\| ");

                                            HashSet<String> tempo = new HashSet<>();

                                            for (int j = 0; j < tokens.length; j++) {
                                                tempo.add(tokens[j]);
                                            }

                                            HashMap<String, String> aging_bits_temp = new HashMap<>();

                                            Iterator it2 = aging_bits.entrySet().iterator();

                                            while (it2.hasNext()) {
                                                Map.Entry mapElement = (Map.Entry) it2.next();
                                                String s1 = (String) mapElement.getKey();
                                                String s2 = (String) mapElement.getValue();

                                                if (tempo.contains(s1)) {
                                                    StringBuilder stringBuilder = new StringBuilder();

                                                    stringBuilder.append('1');

                                                    for (int k = 0; k < 9; k++) {
                                                        stringBuilder.append(s2.charAt(k));
                                                    }

                                                    String t11 = stringBuilder.toString();
                                                    aging_bits_temp.put(s1, t11);
                                                } else {
                                                    StringBuilder stringBuilder = new StringBuilder();

                                                    stringBuilder.append('0');

                                                    for (int k = 0; k < 9; k++) {
                                                        stringBuilder.append(s2.charAt(k));
                                                    }

                                                    String t11 = stringBuilder.toString();
                                                    aging_bits_temp.put(s1, t11);
                                                }

                                            }

                                            aging_bits.clear();
                                            aging_bits = aging_bits_temp;
                                        }

                                        Iterator it1 = aging_bits.entrySet().iterator();

                                        while (it1.hasNext()) {
                                            Map.Entry mapElement = (Map.Entry) it1.next();
                                            String s1 = (String) mapElement.getKey();
                                            String s2 = (String) mapElement.getValue();
                                            SuggestionsClass temp = new SuggestionsClass(s1, s2);

                                            tag_list.add(temp);
                                        }

                                        Collections.sort(tag_list, SuggestionsClass.AgingBitsComparator);

                                        for (int i = 0; i < 10; i++) {
                                            main_tags.add(tag_list.get(i).getName_tag());
                                            Log.e("Tags--------", tag_list.get(i).getName_tag());
                                        }

                                        //        Log.e("Main Tags", "" + main_tags.size());

                                        for (int i = 0; i < books.size(); i++) {
                                            int cnt = 0;

                                            String s = books.get(i).getSubjects();
                                            String[] tokens = s.split(" \\| ");

                                            for (int j = 0; j < tokens.length; j++) {
                                                if (main_tags.contains(tokens[j])) {
                                                    cnt++;
                                                }
                                            }

                                            SuggestionsBookCount temp = new SuggestionsBookCount(books.get(i), cnt);
                                            //            if(cnt > 0)
                                            //            {
                                            //                Log.e("Books With Count",books.get(i).getName()+  "  " + cnt);
                                            //            }

                                            final_suggested_books.add(temp);

                                        }

                                        Collections.sort(final_suggested_books, SuggestionsBookCount.CountComparator);

//                            for (int i = final_suggested_books.size() - 1; i >= final_suggested_books.size() - 11; i--) {
//                                Log.e("Count", "" + final_suggested_books.get(i).getCnt());
//                                Log.e("Book", final_suggested_books.get(i).getBook().getName());
//                            }

                                        //        Toast.makeText(this, "" + Tags.size(), Toast.LENGTH_SHORT).show();


                                        for(int i=final_suggested_books.size()-1 ; i>=0 ; i--) {
                                            if(final_suggested_books.get(i).getBook().getQuantity() > 0){
                                                boolean found = true;
                                                for(int j=0 ; j<MyHistory.size() ; j++){
                                                    if(final_suggested_books.get(i).getBook().getBiblionumber() == MyHistory.get(j).getMybook().getBiblionumber()){
                                                        found = false;
                                                        break;
                                                    }
                                                    if(found)
                                                        toshowbooks.add(final_suggested_books.get(i).getBook());
                                                }
                                            }
                                            if(toshowbooks.size() >= 10)
                                                break;
                                        }
//                                Log.d("Function","Exiting");

                                        adapter = new RecommendationAdapter(SuggestionsActivity.this,toshowbooks);
                                        recyclerView.setAdapter(adapter);
                                        dialog.dismiss();


                                        //////////////////////////////////////
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
                    webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-readingrecord.pl?limit=full");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    dialog.dismiss();
                    Toast.makeText(SuggestionsActivity.this, "Failed : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            try_once = true;

            webView.setWebViewClient(new WebViewClient() {
                boolean page_load_error = false;

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                dialog.dismiss();
                    page_load_error = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (page_load_error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!try_once) {
                            return;
                        }
                        try_once = false;
                        Log.d("PAGE LOADED", "SUCCESS");
                        //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();
                        webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();", new ValueCallback<String>() {
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

                                while (index_bib >= 0 && index_date >= 0 && index_author >= 0) {
                                    Log.d("a", "b");
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
                                        if (q == ' ') break;
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
                                    //Check if the check-in date has value "Checked Out" (it is currently issued and not history)
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
                                        int biblionumber = Integer.parseInt(biblio_string.toString());
                                        BooksClass bk = new BooksClass();
                                        for (int i = 0; i < books.size(); i++) {
                                            if (books.get(i).getBiblionumber() == biblionumber) {
                                                bk = books.get(i);
                                                break;
                                            }
                                        }
                                        HistoryBooksClass toinsert = new HistoryBooksClass(bk, date_str);
                                        MyHistory.add(toinsert);
                                    }
                                    index_bib = value.indexOf(search_bib, index_bib + search_bib.length());
                                    index_date = value.indexOf(search_date, index_date + search_date.length());
                                    index_author = value.indexOf(search_author, index_author + search_author.length());

                                }
                                Log.d("MYHISTORY LOADED", "SUCCESS");

                                //////////////////////////////////////

                                extractTags();

                                Iterator it = Tags.iterator();

                                Log.e("Books Size", "" + books.size());

                                Log.e("Tags Size", "" + Tags.size());

                                while (it.hasNext()) {
                                    aging_bits.put((String) it.next(), "0000000000");
                                }

                                Log.e("Aging Bits Size", "" + aging_bits.size());

                                Collections.sort(MyHistory, HistoryBooksClass.DateComparator);

                                Log.e("My History Size", "" + MyHistory.size());

                                for (int i = 0; i < MyHistory.size(); i++) {
                                    BooksClass temp_book = MyHistory.get(i).getMybook();

                                    String s = temp_book.getSubjects();
                                    String[] tokens = s.split(" \\| ");

                                    HashSet<String> tempo = new HashSet<>();

                                    for (int j = 0; j < tokens.length; j++) {
                                        tempo.add(tokens[j]);
                                    }

                                    HashMap<String, String> aging_bits_temp = new HashMap<>();

                                    Iterator it2 = aging_bits.entrySet().iterator();

                                    while (it2.hasNext()) {
                                        Map.Entry mapElement = (Map.Entry) it2.next();
                                        String s1 = (String) mapElement.getKey();
                                        String s2 = (String) mapElement.getValue();

                                        if (tempo.contains(s1)) {
                                            StringBuilder stringBuilder = new StringBuilder();

                                            stringBuilder.append('1');

                                            for (int k = 0; k < 9; k++) {
                                                stringBuilder.append(s2.charAt(k));
                                            }

                                            String t11 = stringBuilder.toString();
                                            aging_bits_temp.put(s1, t11);
                                        } else {
                                            StringBuilder stringBuilder = new StringBuilder();

                                            stringBuilder.append('0');

                                            for (int k = 0; k < 9; k++) {
                                                stringBuilder.append(s2.charAt(k));
                                            }

                                            String t11 = stringBuilder.toString();
                                            aging_bits_temp.put(s1, t11);
                                        }

                                    }

                                    aging_bits.clear();
                                    aging_bits = aging_bits_temp;
                                }

                                Iterator it1 = aging_bits.entrySet().iterator();

                                while (it1.hasNext()) {
                                    Map.Entry mapElement = (Map.Entry) it1.next();
                                    String s1 = (String) mapElement.getKey();
                                    String s2 = (String) mapElement.getValue();
                                    SuggestionsClass temp = new SuggestionsClass(s1, s2);

                                    tag_list.add(temp);
                                }

                                Collections.sort(tag_list, SuggestionsClass.AgingBitsComparator);

                                for (int i = 0; i < 10; i++) {
                                    main_tags.add(tag_list.get(i).getName_tag());
                                    Log.e("Tags--------", tag_list.get(i).getName_tag());
                                }

                                //        Log.e("Main Tags", "" + main_tags.size());

                                for (int i = 0; i < books.size(); i++) {
                                    int cnt = 0;

                                    String s = books.get(i).getSubjects();
                                    String[] tokens = s.split(" \\| ");

                                    for (int j = 0; j < tokens.length; j++) {
                                        if (main_tags.contains(tokens[j])) {
                                            cnt++;
                                        }
                                    }

                                    SuggestionsBookCount temp = new SuggestionsBookCount(books.get(i), cnt);
                                    //            if(cnt > 0)
                                    //            {
                                    //                Log.e("Books With Count",books.get(i).getName()+  "  " + cnt);
                                    //            }

                                    final_suggested_books.add(temp);

                                }

                                Collections.sort(final_suggested_books, SuggestionsBookCount.CountComparator);

//                            for (int i = final_suggested_books.size() - 1; i >= final_suggested_books.size() - 11; i--) {
//                                Log.e("Count", "" + final_suggested_books.get(i).getCnt());
//                                Log.e("Book", final_suggested_books.get(i).getBook().getName());
//                            }

                                //        Toast.makeText(this, "" + Tags.size(), Toast.LENGTH_SHORT).show();


                                for(int i=final_suggested_books.size()-1 ; i>=0 ; i--) {
                                    if(final_suggested_books.get(i).getBook().getQuantity() > 0)
                                        toshowbooks.add(final_suggested_books.get(i).getBook());
                                    if(toshowbooks.size() >= 10)
                                        break;
                                }
//                                Log.d("Function","Exiting");

                                adapter = new RecommendationAdapter(SuggestionsActivity.this,toshowbooks);
                                recyclerView.setAdapter(adapter);
                                dialog.dismiss();


                                //////////////////////////////////////
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
            webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-readingrecord.pl?limit=full");
        }



//        books = new ArrayList<>();
//        Tags = new HashSet<>();
//        tag_list = new ArrayList<>();
//        MyHistory = new ArrayList<>();
//        aging_bits = new HashMap<>();
//        main_tags = new HashSet<>();
//        final_suggested_books = new ArrayList<>();
//
//        readBookData();
//        extractTags();
//
//        Iterator it = Tags.iterator();
//
//        Log.e("Books Size", "" + books.size());
//
//        Log.e("Tags Size", "" + Tags.size());
//
//        while(it.hasNext())
//        {
//            aging_bits.put((String) it.next(),"0000000000");
//        }
//
//        Log.e("Aging Bits Size", "" + aging_bits.size());
//
//        for(int i = 0 ; i < books.size() ; i++)
//        {
//            if(books.get(i).getBiblionumber() == 19 ||
//                    books.get(i).getBiblionumber() == 20 ||
//                    books.get(i).getBiblionumber() == 18 ||
//                    books.get(i).getBiblionumber() == 757 ||
//                    books.get(i).getBiblionumber() == 800 ||
//                    books.get(i).getBiblionumber() == 876) {
//
//
//
//                MyHistory.add(new HistoryBooksClass(books.get(i),"07/01/2001"));
//            }
//        }
//
//        Collections.sort(MyHistory,HistoryBooksClass.DateComparator);
//
//        Log.e("My History Size", "" + MyHistory.size());
//
//        for(int i = 0 ; i < MyHistory.size() ; i++)
//        {
//            BooksClass temp_book = MyHistory.get(i).getMybook();
//
//            String s = temp_book.getSubjects();
//            String[] tokens = s.split(" \\| ");
//
//            HashSet<String> tempo = new HashSet<>();
//
//            for(int j = 0 ; j < tokens.length ; j++)
//            {
//                tempo.add(tokens[j]);
//            }
//
//            HashMap<String,String> aging_bits_temp = new HashMap<>();
//
//            Iterator it2 = aging_bits.entrySet().iterator();
//
//            while(it2.hasNext())
//            {
//                Map.Entry mapElement = (Map.Entry)it2.next();
//                String s1 = (String) mapElement.getKey();
//                String s2 = (String) mapElement.getValue();
//
//                if(tempo.contains(s1))
//                {
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    stringBuilder.append('1');
//
//                    for(int k = 0 ; k < 9 ; k++)
//                    {
//                        stringBuilder.append(s2.charAt(k));
//                    }
//
//                    String t11 = stringBuilder.toString();
//                    aging_bits_temp.put(s1,t11);
//                }
//                else
//                {
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    stringBuilder.append('0');
//
//                    for(int k = 0 ; k < 9 ; k++)
//                    {
//                        stringBuilder.append(s2.charAt(k));
//                    }
//
//                    String t11 = stringBuilder.toString();
//                    aging_bits_temp.put(s1,t11);
//                }
//
//            }
//
//            aging_bits.clear();
//            aging_bits = aging_bits_temp;
//        }
//
//        Iterator it1 = aging_bits.entrySet().iterator();
//
//        while(it1.hasNext())
//        {
//            Map.Entry mapElement = (Map.Entry)it1.next();
//            String s1 = (String) mapElement.getKey();
//            String s2 = (String) mapElement.getValue();
//            SuggestionsClass temp = new SuggestionsClass(s1,s2);
//
//            tag_list.add(temp);
//        }
//
//        Collections.sort(tag_list,SuggestionsClass.AgingBitsComparator);
//
//        for(int i = 0 ; i < 10 ; i++)
//        {
//            main_tags.add(tag_list.get(i).getName_tag());
//            Log.e("Tags--------",tag_list.get(i).getName_tag());
//        }
//
//        Log.e("Main Tags", "" + main_tags.size());
//
//        for(int i = 0 ; i < books.size(); i++)
//        {
//            int cnt = 0;
//
//            String s = books.get(i).getSubjects();
//            String[] tokens = s.split(" \\| ");
//
//            for(int j = 0 ; j < tokens.length; j++)
//            {
//                if(main_tags.contains(tokens[j]))
//                {
//                    cnt++;
//                }
//            }
//
//            SuggestionsBookCount temp = new SuggestionsBookCount(books.get(i),cnt);
//            if(cnt > 0)
//            {
//                Log.e("Books With Count",books.get(i).getName()+  "  " + cnt);
//            }
//
//            final_suggested_books.add(temp);
//
//        }
//
//        Collections.sort(final_suggested_books,SuggestionsBookCount.CountComparator);
//
//        for(int i = final_suggested_books.size() - 1 ; i >= final_suggested_books.size() - 11 ; i--)
//        {
//            Log.e("Count","" + final_suggested_books.get(i).getCnt());
//            Log.e("Book",final_suggested_books.get(i).getBook().getName());
//        }
//
//        Toast.makeText(this, "" + Tags.size(), Toast.LENGTH_SHORT).show();

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

    private void extractTags()
    {
        for(int i = 0 ; i < books.size() ; i++)
        {
            String s = books.get(i).getSubjects();
            String[] tokens = s.split(" \\| ");

            String s1 = books.get(i).getAuthors();
            String[] tokens1 = s1.split(", ");

            for(int j = 0 ; j < tokens.length; j++)
            {
                Tags.add(tokens[j]);
            }

            for(int j = 0 ; j < tokens1.length; j++)
            {
                Tags.add(tokens1[j]);
            }

            Tags.add(books.get(i).getPublisher());

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

//        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();

        editor.commit();
        complexPreferences.commit();
        editor1.commit();
    }

}
