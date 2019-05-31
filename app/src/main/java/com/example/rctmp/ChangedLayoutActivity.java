package com.example.rctmp;

import android.content.Intent;
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
                Intent i = new Intent(ChangedLayoutActivity.this,IntermediateActivity.class);
                startActivity(i);
            }
        });

        issuedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChangedLayoutActivity.this, "Issued Books", Toast.LENGTH_SHORT).show();

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"History",Toast.LENGTH_SHORT).show();
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Wishlist",Toast.LENGTH_SHORT).show();
            }
        });

        suggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"Suggestions",Toast.LENGTH_SHORT).show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangedLayoutActivity.this,"SignOut",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
