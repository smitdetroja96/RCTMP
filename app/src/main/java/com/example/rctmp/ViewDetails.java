package com.example.rctmp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewDetails extends AppCompatActivity {

    TextView tv_author,tv_title,tv_material_type,tv_publisher,tv_description;
    TextView tv_subjects,tv_callnumber,tv_quantity;
    BooksClass book_viewed = new BooksClass();
    HistoryBooksClass book_now = new HistoryBooksClass();
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        boolean whattofetch = (boolean) getIntent().getBooleanExtra("isMain",false);
        if(whattofetch == true){
            book_viewed = (BooksClass) getIntent().getSerializableExtra("BookDetails");
        }
        else{
            book_now = (HistoryBooksClass) getIntent().getSerializableExtra("BookDetails");
        }

        tv_author = findViewById(R.id.tv_author);
        tv_title = findViewById(R.id.tv_title);
        tv_material_type = findViewById(R.id.tv_material_type);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_description = findViewById(R.id.tv_description);
        tv_callnumber = findViewById(R.id.tv_callnumber);
        tv_subjects = findViewById(R.id.tv_subject);
        tv_quantity = findViewById(R.id.tv_quantity);


        if(whattofetch) {
            String temp = "Authors: " + book_viewed.getAuthors();
            tv_author.setText(temp);
            tv_title.setText(book_viewed.getName());
            temp = "Type: " + book_viewed.getMaterialType();
            tv_material_type.setText(temp);
            temp = "Publisher: " + book_viewed.getPublisher();
            tv_publisher.setText(temp);
            temp = "Call Number: " + book_viewed.getCallnumber();
            tv_callnumber.setText(temp);
            temp = "Subjects: " + book_viewed.getSubjects();
            tv_subjects.setText(temp);
            temp = "Quantity: " + book_viewed.getQuantity();
            tv_quantity.setText(temp);
            temp = "Description: " + book_viewed.getDescription();
            tv_description.setText(temp);
            url = book_viewed.getUrl();
        }
        else{
            url = "https://opac.daiict.ac.in/cgi-bin/koha/opac-detail.pl?biblionumber=" + book_now.getBiblionumber();
        }
    }

    public void onClickUrl(View view) {
        Uri uri = Uri.parse(url);
        Intent i1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i1);
    }
}
