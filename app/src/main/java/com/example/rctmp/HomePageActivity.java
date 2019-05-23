package com.example.rctmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }


    public void onClickSearch(View view) {
        Toast.makeText(HomePageActivity.this,"SEARCHING",Toast.LENGTH_SHORT).show();
    }

    public void onClickIssuedBooks(View view) {
        Toast.makeText(HomePageActivity.this,"Showing Issued Books",Toast.LENGTH_SHORT).show();
    }

    public void onClickWishList(View view) {
        Toast.makeText(HomePageActivity.this,"WISH LIST",Toast.LENGTH_SHORT).show();
    }

    public void onClickHistory(View view) {
        Toast.makeText(HomePageActivity.this,"HISTORY",Toast.LENGTH_SHORT).show();
    }

    public void onClickSuggestions(View view) {
        Toast.makeText(HomePageActivity.this,"Suggestions",Toast.LENGTH_SHORT).show();
    }

    public void onClickSignOut(View view) {
        Toast.makeText(HomePageActivity.this,"FUCKING OFF",Toast.LENGTH_SHORT).show();
    }
}
