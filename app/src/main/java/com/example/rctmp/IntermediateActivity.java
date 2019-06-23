package com.example.rctmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class IntermediateActivity extends AppCompatActivity {

    Button button_web_search,button_book_search;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

//-------------------------------------------------------------------------------------------------------------------------------------
        toolbar = findViewById(R.id.toolbar_intermediate_layout);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("RC");
//-------------------------------------------------------------------------------------------------------------------------------------

        button_web_search = findViewById(R.id.bt_web_search);
        button_book_search = findViewById(R.id.bt_book_search);

        button_web_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(IntermediateActivity.this,OnlineResourceSearchActivity.class);
                startActivity(i);
            }
        });

        button_book_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1 = new Intent(IntermediateActivity.this,SearchingActivity.class);
                startActivity(i1);

            }
        });

    }

    public void onClickRaxterSearch(View view) {
        Intent i2 = new Intent(IntermediateActivity.this,RaxterSearchActivity.class);
        startActivity(i2);

    }

    public void onClickScholarlyResourcesSearch(View view){
        Intent i3 = new Intent(IntermediateActivity.this,ScholarlyResourcesActivity.class);
        startActivity(i3);
    }
}
