package com.example.rctmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangedLayoutActivity extends AppCompatActivity {

    LinearLayout search,issuedBooks,history,wishList,suggestions,signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changed_layout);
        search = findViewById(R.id.ll_search);
        issuedBooks = findViewById(R.id.ll_issuedBooks);
        history = findViewById(R.id.ll_history);
        wishList = findViewById(R.id.ll_wishList);
        suggestions = findViewById(R.id.ll_suggestions);
        signOut = findViewById(R.id.ll_signOut);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Search",0).show();
            }
        });

        issuedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Issued Books",0).show();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"History",0).show();
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Wishlist",0).show();
            }
        });

        suggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Suggestions",0).show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"SignOut",0).show();
            }
        });
    }
}
