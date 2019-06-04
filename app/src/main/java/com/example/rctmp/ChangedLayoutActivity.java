package com.example.rctmp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangedLayoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout search,issuedBooks,history,wishList,suggestions,signOut;
    private DrawerLayout drawer;

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

//------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("RC");

        drawer = findViewById(R.id.drawer_layout_1);

        NavigationView navigationView = findViewById(R.id.nav_view_1);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //------------------------------------------------------------------------------------------------------------------------------------

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_user_profile:
                displayMessage("Smit");
                break;
            case R.id.change_app_layout:
                Intent i = new Intent(ChangedLayoutActivity.this,DrawerActivityLayout.class);
                startActivity(i);
                finish();
                break;

        }
        return true;
    }

    public void displayMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
