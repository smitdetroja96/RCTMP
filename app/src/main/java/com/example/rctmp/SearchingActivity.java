package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchingActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    ListAdapter adapter;
    Spinner spinner;
    EditText search_edit_text;
    ArrayList<BooksClass> books;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        //***********************************************************************************************

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchingActivity.this);
        builder.setCancelable(false);
//        TextView textView = findViewById(R.id.loader);
//        textView.setText("Fetching Books...");
        builder.setView(R.layout.progress_dialogue_layout_1);

        final AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.show();

        //***********************************************************************************************

        //-------------------------------------------------------------------------------------------------------------

        books = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    BooksClass temp = snapshot.getValue(BooksClass.class);
                    books.add(temp);
                }

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dialog.dismiss();
                Toast.makeText(SearchingActivity.this, "Failed : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListAdapter(this,books);

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
        ArrayList<BooksClass> filtered = new ArrayList<>();

        for (BooksClass s : books) {
            //if the existing elements contains the search input
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
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
//            adapter.removeItem(item.getGroupId());
            displayMessage("Menu1 Clicked...");
        }
        else if(item.getItemId() == 2)
        {
            displayMessage("Menu2 Clicked...");
        }
        else if(item.getItemId()==3)
        {
            Intent i = new Intent(SearchingActivity.this,ViewDetials.class);
            startActivity(i);
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
