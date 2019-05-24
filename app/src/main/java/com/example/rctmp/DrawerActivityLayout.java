package com.example.rctmp;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class DrawerActivityLayout extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();





    }

    public void onClickSearch(View view) {
        Toast.makeText(this,"SEARCHING",Toast.LENGTH_SHORT).show();
    }

    public void onClickIssuedBooks(View view) {
        Toast.makeText(this,"Showing Issued Books",Toast.LENGTH_SHORT).show();
    }

    public void onClickWishList(View view) {
        Toast.makeText(this,"WISH LIST",Toast.LENGTH_SHORT).show();
    }

    public void onClickHistory(View view) {
        Toast.makeText(this,"HISTORY",Toast.LENGTH_SHORT).show();
    }

    public void onClickSuggestions(View view) {
        Toast.makeText(this,"Suggestions",Toast.LENGTH_SHORT).show();
    }

    public void onClickSignOut(View view) {
        Toast.makeText(this,"FUCKING OFF",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
