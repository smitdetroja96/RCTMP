package com.example.rctmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class SuggestionsActivity extends AppCompatActivity {

    ArrayList<BooksClass> books;
    HashSet<String> Tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        books = new ArrayList<>();
        Tags = new HashSet<>();

        readBookData();
        extractTags();

        Toast.makeText(this, "" + Tags.size(), Toast.LENGTH_SHORT).show();

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
