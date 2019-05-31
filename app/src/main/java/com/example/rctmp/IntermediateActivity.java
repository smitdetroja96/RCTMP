package com.example.rctmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntermediateActivity extends AppCompatActivity {

    Button button_web_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        button_web_search = findViewById(R.id.bt_web_search);

        button_web_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(IntermediateActivity.this,OnlineResourceSearchActivity.class);
                startActivity(i);
            }
        });

    }
}
