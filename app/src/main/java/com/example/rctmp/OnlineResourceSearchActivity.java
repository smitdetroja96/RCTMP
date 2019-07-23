package com.example.rctmp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OnlineResourceSearchActivity extends AppCompatActivity { //implements BottomNavigationView.OnNavigationItemSelectedListener {

    EditText search_text;
    Intent intent;
    CardView acm,aps,bloom,ieee,jstor,scidir,springer,usenix;
    BottomNavigationView bottomNavigationView;

    boolean loaded = false;
    String search_query;

    Toolbar online_resources_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_resource_search);

        //-------------------------------------------------------------------------------------------------------------------------------------------
        online_resources_toolbar = findViewById(R.id.toolbar_online_resources);
        setSupportActionBar(online_resources_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Online Resources");
        //-------------------------------------------------------------------------------------------------------------------------------------------




        search_text = findViewById(R.id.et_online_res);
        acm = findViewById(R.id.cv_acm);
        aps = findViewById(R.id.cv_aps);
        bloom = findViewById(R.id.cv_blooms);
        ieee = findViewById(R.id.cv_ieee);
        jstor = findViewById(R.id.cv_jstor);
        scidir = findViewById(R.id.cv_science);
        springer = findViewById(R.id.cv_springer);
        usenix = findViewById(R.id.cv_usenix);


        intent = new Intent(getApplicationContext(),WebSearchActivity.class);


        acm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "acm");
                startActivity(intent);
            }
        });

        aps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "aps");
                startActivity(intent);
            }
        });

        bloom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "bloomsbury");
                startActivity(intent);
            }
        });

        ieee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "ieee");
                startActivity(intent);
            }
        });

        jstor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "jstor");
                startActivity(intent);
            }
        });

        scidir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "science_direct");
                startActivity(intent);
            }
        });

        springer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "springer");
                startActivity(intent);
            }
        });

        usenix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query = search_text.getText().toString();
                if(search_query.trim().isEmpty()){
                    Toast.makeText(OnlineResourceSearchActivity.this, "Please enter a valid query!", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("search_query",search_query);
                intent.putExtra("site_to_be_searched", "usenix");
                startActivity(intent);
            }
        });


//        bottomNavigationView = findViewById(R.id.bottom_nav_view);
//        bottomNavigationView.getMenu().getItem(0).setCheckable(true);
//        bottomNavigationView.getMenu().getItem(1).setCheckable(true);
//        bottomNavigationView.getMenu().getItem(2).setCheckable(true);
//
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId())
//        {
//            case R.id.bottom_nav_profile:
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.bottom_nav_home:
//                Intent i = new Intent(OnlineResourceSearchActivity.this,DrawerActivityLayout.class);
//                startActivity(i);
//                finish();
////                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.bottom_nav_search:
//                Intent i1 = new Intent(OnlineResourceSearchActivity.this,IntermediateActivity.class);
//                startActivity(i1);
//                finish();
////                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        return true;
//    }
}
