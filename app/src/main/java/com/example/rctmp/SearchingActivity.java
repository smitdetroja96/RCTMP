package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchingActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    ListAdapter adapter;
    Spinner spinner;
    EditText search_edit_text;
    ArrayList<BooksClass> books;
    String Search_tag;
    int count = 0;

    String current_date,todays_date;

    ArrayList<BooksClass> filtered = new ArrayList<>();

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);


        books = new ArrayList<>();

        readData();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        todays_date = df.format(date);

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

        if(todays_date.equals(current_date) == false)
        {

            //-------------------------------------------------------------------------------------------------------------

            databaseReference = FirebaseDatabase.getInstance().getReference("Books").child("inside");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BooksClass temp = snapshot.getValue(BooksClass.class);
                        books.add(temp);
                    }

                    saveData();
                    dialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    dialog.dismiss();
                    Toast.makeText(SearchingActivity.this, "Failed : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
        {
            readBookData();
            dialog.dismiss();
        }

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

//        Toast.makeText(this, Search_tag, Toast.LENGTH_SHORT).show();

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

//                Toast.makeText(SearchingActivity.this, Search_tag + "+++++++", Toast.LENGTH_SHORT).show();
                filter(editable.toString());

            }
        });

    }

    public void filter(String text)
    {
        filtered.clear();

        if(Search_tag.compareTo("Title") == 0)
        {
//            Toast.makeText(this, "111111111", Toast.LENGTH_SHORT).show();

            for (BooksClass s : books) {
                //if the existing elements contains the search input
                if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filtered.add(s);
                }
            }
        }
        else if(Search_tag.compareTo("Keyword") == 0)
        {

//            Toast.makeText(this, Search_tag + "---------", Toast.LENGTH_SHORT).show();

//            Toast.makeText(this, "2222222222222222", Toast.LENGTH_SHORT).show();

            for (BooksClass s : books) {
                //if the existing elements contains the search input
                if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filtered.add(s);
                }
                else if(s.getAuthors().toLowerCase().contains(text.toLowerCase()))
                {
                    filtered.add(s);
                }
                else if(s.getPublisher().toLowerCase().contains(text.toLowerCase()))
                {
                    filtered.add(s);
                }
                else if(s.getSubjects().toLowerCase().contains(text.toLowerCase()))
                {
                    filtered.add(s);
                }
            }
        }
        else if(Search_tag.compareTo("Author") == 0)
        {
//            Toast.makeText(this, "3333333333333333333", Toast.LENGTH_SHORT).show();

            for (BooksClass s : books) {
                //if the existing elements contains the search input
                if (s.getAuthors().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filtered.add(s);
                }
            }
        }
        else if(Search_tag.compareTo("Publisher") == 0)
        {
//            Toast.makeText(this, "444444444444", Toast.LENGTH_SHORT).show();

            for (BooksClass s : books) {
                //if the existing elements contains the search input
                if (s.getPublisher().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filtered.add(s);
                }
            }
        }
        else if(Search_tag.compareTo("Subject") == 0)
        {

//            Toast.makeText(this, "555555555555555555", Toast.LENGTH_SHORT).show();

            for (BooksClass s : books) {
                //if the existing elements contains the search input
                if (s.getSubjects().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filtered.add(s);
                }
            }
        }

        adapter.filter_list(filtered);
    }


//-------------------------------------------------------------------------------------------------------
    public void displayMessage(String message)
    {
        Snackbar.make(findViewById(R.id.root_layout),message,Snackbar.LENGTH_SHORT).show();
    }
//-----------------------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Search_tag = adapterView.getSelectedItem().toString();
        String change = search_edit_text.getText().toString();
//        Toast.makeText(this, change, Toast.LENGTH_SHORT).show();
        filter(change);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
//-------------------------------------------------------------------------------------------------

    private void saveData()
    {

        SharedPreferences preferences = getSharedPreferences("CUR_DATE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String cur_date = df.format(date);

        editor1.putString("CUR_DATE",cur_date);

        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        complexPreferences.clearObject();

        editor.putInt("numberOfBooks",books.size());

        int i;

        for(i = 0 ; i < books.size() ; i++)
        {
            complexPreferences.putObject("Books" + Integer.toString(i), books.get(i));
        }

        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();

        editor.commit();
        complexPreferences.commit();
        editor1.commit();
    }

    private void readBookData()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        int numberOfAllBooks = sharedpreferences.getInt("numberOfBooks",0);

        Toast.makeText(this, "" + numberOfAllBooks, Toast.LENGTH_SHORT).show();

        books.clear();

        for(int i=0;i<numberOfAllBooks;i++)
        {
            books.add(complexPreferences.getObject("Books"+Integer.toString(i),BooksClass.class));
        }

        Toast.makeText(this, "" + books.size() + " hii", Toast.LENGTH_SHORT).show();
    }

    private void readData()
    {
        SharedPreferences preferences = getSharedPreferences("CUR_DATE",Context.MODE_PRIVATE);
        current_date = preferences.getString("CUR_DATE","No date");
    }

//-------------------------------------------------------------------------------------------------

}
