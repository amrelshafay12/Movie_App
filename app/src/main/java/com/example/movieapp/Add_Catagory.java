package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Catagory extends AppCompatActivity {

    EditText add_category;
    DatabaseReference databaseReference;
    Categories categories;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_catagory);

        add_category = findViewById(R.id.category_Name);
        add = findViewById(R.id.add_category);
        categories = new Categories();
        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
    }


    public void done_add(View view) {
        String category = add_category.getText().toString();
        if(TextUtils.isEmpty(category))
        {
            Toast.makeText(Add_Catagory.this,"All data are required",Toast.LENGTH_LONG).show();
        }
        else
        {
            categories.setName(category);
            String i = databaseReference.push().getKey();
            databaseReference.child(i).setValue(categories);
            Intent intent = new Intent(Add_Catagory.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}