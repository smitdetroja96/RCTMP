package com.example.rctmp;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        ArrayList<String> temp = new ArrayList<>();

        int i;
        for(i = 0; i < 15; i++)
        {
            temp.add("" + i);
        }

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListAdapter(this,temp);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId() == 1)
        {
            adapter.removeItem(item.getGroupId());
            displayMessage("Menu1 Clicked...");

        }
        else if(item.getItemId() == 2)
        {
            displayMessage("Menu2 Clicked...");
        }


        return super.onContextItemSelected(item);
    }

    public void displayMessage(String message)
    {
        Snackbar.make(findViewById(R.id.root_layout),message,Snackbar.LENGTH_SHORT).show();
    }
}
