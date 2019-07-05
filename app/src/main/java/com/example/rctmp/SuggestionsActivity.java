package com.example.rctmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SuggestionsActivity extends AppCompatActivity {

    ArrayList<BooksClass> books;
    HashSet<String> Tags;
    ArrayList<SuggestionsClass> tag_list;
    ArrayList<HistoryBooksClass> MyHistory;
    HashMap<String,String> aging_bits;
    HashSet<String> main_tags;
    ArrayList<SuggestionsBookCount> final_suggested_books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        books = new ArrayList<>();
        Tags = new HashSet<>();
        tag_list = new ArrayList<>();
        MyHistory = new ArrayList<>();
        aging_bits = new HashMap<>();
        main_tags = new HashSet<>();
        final_suggested_books = new ArrayList<>();

        readBookData();
        extractTags();

        Iterator it = Tags.iterator();

        Log.e("Books Size", "" + books.size());

        Log.e("Tags Size", "" + Tags.size());

        while(it.hasNext())
        {
            aging_bits.put((String) it.next(),"0000000000");
        }

        Log.e("Aging Bits Size", "" + aging_bits.size());

        for(int i = 0 ; i < books.size() ; i++)
        {
            if(books.get(i).getBiblionumber() == 19 ||
                    books.get(i).getBiblionumber() == 20 ||
                    books.get(i).getBiblionumber() == 18 ||
                    books.get(i).getBiblionumber() == 757 ||
                    books.get(i).getBiblionumber() == 800 ||
                    books.get(i).getBiblionumber() == 876)
            {
                MyHistory.add(new HistoryBooksClass(books.get(i),"07/01/2001"));
            }
        }

        Collections.sort(MyHistory,HistoryBooksClass.DateComparator);

        Log.e("My History Size", "" + MyHistory.size());

        for(int i = 0 ; i < MyHistory.size() ; i++)
        {
            BooksClass temp_book = MyHistory.get(i).getMybook();

            String s = temp_book.getSubjects();
            String[] tokens = s.split(" \\| ");

            HashSet<String> tempo = new HashSet<>();

            for(int j = 0 ; j < tokens.length ; j++)
            {
                tempo.add(tokens[j]);
            }

            HashMap<String,String> aging_bits_temp = new HashMap<>();

            Iterator it2 = aging_bits.entrySet().iterator();

            while(it2.hasNext())
            {
                Map.Entry mapElement = (Map.Entry)it2.next();
                String s1 = (String) mapElement.getKey();
                String s2 = (String) mapElement.getValue();

                if(tempo.contains(s1))
                {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append('1');

                    for(int k = 0 ; k < 9 ; k++)
                    {
                        stringBuilder.append(s2.charAt(k));
                    }

                    String t11 = stringBuilder.toString();
                    aging_bits_temp.put(s1,t11);
                }
                else
                {
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append('0');

                    for(int k = 0 ; k < 9 ; k++)
                    {
                        stringBuilder.append(s2.charAt(k));
                    }

                    String t11 = stringBuilder.toString();
                    aging_bits_temp.put(s1,t11);
                }

            }

            aging_bits.clear();
            aging_bits = aging_bits_temp;
        }

        Iterator it1 = aging_bits.entrySet().iterator();

        while(it1.hasNext())
        {
            Map.Entry mapElement = (Map.Entry)it1.next();
            String s1 = (String) mapElement.getKey();
            String s2 = (String) mapElement.getValue();
            SuggestionsClass temp = new SuggestionsClass(s1,s2);

            tag_list.add(temp);
        }

        Collections.sort(tag_list,SuggestionsClass.AgingBitsComparator);

        for(int i = 0 ; i < 10 ; i++)
        {
            main_tags.add(tag_list.get(i).getName_tag());
            Log.e("Tags--------",tag_list.get(i).getName_tag());
        }

//        Log.e("Main Tags", "" + main_tags.size());

        for(int i = 0 ; i < books.size(); i++)
        {
            int cnt = 0;

            String s = books.get(i).getSubjects();
            String[] tokens = s.split(" \\| ");

            for(int j = 0 ; j < tokens.length; j++)
            {
                if(main_tags.contains(tokens[j]))
                {
                    cnt++;
                }
            }

            SuggestionsBookCount temp = new SuggestionsBookCount(books.get(i),cnt);
//            if(cnt > 0)
//            {
//                Log.e("Books With Count",books.get(i).getName()+  "  " + cnt);
//            }

            final_suggested_books.add(temp);

        }

        Collections.sort(final_suggested_books,SuggestionsBookCount.CountComparator);

        for(int i = final_suggested_books.size() - 1 ; i >= final_suggested_books.size() - 11 ; i--)
        {
            Log.e("Count","" + final_suggested_books.get(i).getCnt());
            Log.e("Book",final_suggested_books.get(i).getBook().getName());
        }

//        Toast.makeText(this, "" + Tags.size(), Toast.LENGTH_SHORT).show();

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

}
