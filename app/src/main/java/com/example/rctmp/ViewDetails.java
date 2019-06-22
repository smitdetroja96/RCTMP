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
    TextView tv_subjects,tv_callnumber;
    BooksClass book_viewed = new BooksClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        book_viewed = (BooksClass) getIntent().getSerializableExtra("BookDetails");

        tv_author = findViewById(R.id.tv_author);
        tv_title = findViewById(R.id.tv_title);
        tv_material_type = findViewById(R.id.tv_material_type);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_description = findViewById(R.id.tv_description);
        tv_callnumber = findViewById(R.id.tv_callnumber);
        tv_subjects = findViewById(R.id.tv_subject);

        tv_author.setText(book_viewed.getAuthors());
        tv_title.setText(book_viewed.getName());
        tv_material_type.setText(book_viewed.getMaterialType());
        tv_publisher.setText(book_viewed.getPublisher());
        tv_callnumber.setText(book_viewed.getCallnumber());
        tv_subjects.setText(book_viewed.getSubjects());
    }

    public void onClickUrl(View view) {
        String url = book_viewed.getUrl();
        Uri uri = Uri.parse(url);
        Intent i1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i1);
    }
}
