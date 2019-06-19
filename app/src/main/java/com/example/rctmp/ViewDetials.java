package com.example.rctmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewDetials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detials);

        BooksClass book_viewed = (BooksClass) getIntent().getSerializableExtra("BookDetails");

    }
}
