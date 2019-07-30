package com.example.rctmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuggestUs extends AppCompatActivity {

    Toolbar toolbar;
    Button button;
    EditText editText;
    DatabaseReference databaseReference;
    String message = "";
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_us);

//--------------------------------------------------------------------------------------------------
        toolbar = findViewById(R.id.toolbar_suggest_us);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Suggestions");
//--------------------------------------------------------------------------------------------------

       button = findViewById(R.id.btnSuggest);
       editText = findViewById(R.id.etMessage);

       readData();

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(!isInternet())
               {
                    showSnackBar();
               }

               message = editText.getText().toString();

               if(message.isEmpty())
               {
                   Toast.makeText(SuggestUs.this, "Please Enter Suggestions", Toast.LENGTH_SHORT).show();
               }

               databaseReference = FirebaseDatabase.getInstance().getReference("Suggestions");

               String id = databaseReference.push().getKey();

               SuggestUsClass suggestUsClass = new SuggestUsClass(user_id,message);

               databaseReference.child(id).setValue(suggestUsClass).addOnCompleteListener(SuggestUs.this, new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(SuggestUs.this, "Thank You For Your Valuable Suggestion", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SuggestUs.this, "Failed : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        }
                   }
               });

               editText.setText("");

           }
       });


    }

    public boolean isInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;

        return false;
    }

    public void showSnackBar() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.rootLayout);
        Snackbar snackbar = Snackbar.make(linearLayout,"No Internet Connection", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void readData()
    {
        SharedPreferences preferences = getSharedPreferences("ID",Context.MODE_PRIVATE);
        user_id = preferences.getString("ID","No user");
    }
}
