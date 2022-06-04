package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Movies extends AppCompatActivity {

    String name;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Movie_Adapter movie_adapter;
    ArrayList<movie> list;
    TextView movie;
    Movie_Adapter.RecyclerViewClickListener listener;
    ImageView imageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movies);

        movie = findViewById(R.id.movie);
        imageView = findViewById(R.id.imageView2);
        name = getIntent().getStringExtra("name");
        movie.setText(name+" Movies");
        recyclerView = findViewById(R.id.movieRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference(name);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        setOnClickListener();
        movie_adapter = new Movie_Adapter(this,list,listener);

        recyclerView.setAdapter(movie_adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(com.example.movieapp.movie.class));
                }
                movie_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Movies.this,Add_Movie.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    private void setOnClickListener() {
        listener = new Movie_Adapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(Movies.this,watch_Movie.class);
                intent.putExtra("category",name);
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        };
    }
}