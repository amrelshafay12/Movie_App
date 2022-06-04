package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Edit_Movie extends AppCompatActivity {

    EditText name;
    EditText description;
    EditText rate;
    String name_,Movie,id,url;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_movie);

        name_ = getIntent().getStringExtra("category");
        Movie = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");

        name = findViewById(R.id.Name);
        description = findViewById(R.id.Description);
        rate = findViewById(R.id.rate);

    }

    public void done_edit(View view) {
        String n = name.getText().toString();
        String d = description.getText().toString();
        String r = rate.getText().toString();
        if(TextUtils.isEmpty(n) || TextUtils.isEmpty(d) || TextUtils.isEmpty(r))
        {
            Toast.makeText(Edit_Movie.this,"All data are required",Toast.LENGTH_LONG).show();
        }

        else
        {
            HashMap movie = new HashMap();
            movie.put("id",id);
            movie.put("Name",n);
            movie.put("Description",d);
            movie.put("rate",r);
            movie.put("url",url);

            databaseReference = FirebaseDatabase.getInstance().getReference(name_);
            databaseReference.child(id).updateChildren(movie).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(Edit_Movie.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Edit_Movie.this,"Failed to update",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void ChooseVideo(View view) {
    }
}