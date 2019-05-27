package com.example.rctmp;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    ListAdapter adapter;
    Spinner spinner;
    EditText search_edit_text;
    ArrayList<String> temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        //-------------------------------------------------------------------------------------------------------------
        temp = new ArrayList<>();

        int i;
        for(i = 0; i < 15; i++)
        {
            temp.add("" + i);
        }

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListAdapter(this,temp);

        recyclerView.setAdapter(adapter);
        //--------------------------------------------------------------------------------------------------------------

        spinner = findViewById(R.id.spinner_search_category);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_items,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //--------------------------------------------------------------------------------------------------------------

        search_edit_text = findViewById(R.id.et_search_books);

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());

            }
        });

    }

    public void filter(String text)
    {
        ArrayList<String> filtered = new ArrayList<>();

        for (String s : temp) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filtered.add(s);
            }
        }

        adapter.filter_list(filtered);
    }

//----------------------------------------------------------------------------------------------------------
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

//-------------------------------------------------------------------------------------------------------
    public void displayMessage(String message)
    {
        Snackbar.make(findViewById(R.id.root_layout),message,Snackbar.LENGTH_SHORT).show();
    }
//-----------------------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
//-------------------------------------------------------------------------------------------------

}
