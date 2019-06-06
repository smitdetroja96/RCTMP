package com.example.rctmp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OnlineResourceSearchActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ImageButton search_web;
    EditText search_text;
    RadioGroup rg;
    Intent intent;

    BottomNavigationView bottomNavigationView;

    boolean loaded = false;
    String search_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_resource_search);

        rg = findViewById(R.id.rg_web_site_options);
        search_text = findViewById(R.id.et_search_text);
        search_web = findViewById(R.id.bt_searchWeb);
        search_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int website_id = rg.getCheckedRadioButtonId();
                search_query = search_text.getText().toString();
                search_query = search_query.replaceAll("^\\s*","");
                if(search_query.length()<3){
                    Toast.makeText(getApplicationContext(),"Please enter at least 3 characters",Toast.LENGTH_SHORT).show();
                    return;
                }


                intent = new Intent(getApplicationContext(),WebSearchActivity.class);

                intent.putExtra("search_query",search_query);
                switch (website_id)
                {
                    case R.id.rb_bloomsbury:
                        intent.putExtra("site_to_be_searched","bloomsbury");
                        break;
                    case R.id.rb_ieee:
                        intent.putExtra("site_to_be_searched","ieee");
                        break;
                    case R.id.rb_jstor:
                        intent.putExtra("site_to_be_searched","jstor");
                        break;
                    case R.id.rb_science_direct:
                        intent.putExtra("site_to_be_searched","science_direct");
                        break;
                    case R.id.rb_springer:
                        intent.putExtra("site_to_be_searched","springer");
                        break;
                    case R.id.rb_usenix:
                        intent.putExtra("site_to_be_searched","usenix");
                        break;
                }
                startActivity(intent);

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.getMenu().getItem(0).setCheckable(true);
        bottomNavigationView.getMenu().getItem(1).setCheckable(true);
        bottomNavigationView.getMenu().getItem(2).setCheckable(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.bottom_nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_nav_home:
                Intent i = new Intent(OnlineResourceSearchActivity.this,DrawerActivityLayout.class);
                startActivity(i);
                finish();
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_nav_search:
                Intent i1 = new Intent(OnlineResourceSearchActivity.this,IntermediateActivity.class);
                startActivity(i1);
                finish();
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
