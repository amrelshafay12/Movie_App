package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;

public class watch_Movie extends AppCompatActivity {

    String name;
    String Movie;
    String id;
    VideoView video;
    TextView description;
    DatabaseReference databaseReference;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_watch_movie);

        video = findViewById(R.id.videoView);
        description = findViewById(R.id.description);

        name = getIntent().getStringExtra("category");
        Movie = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");

        databaseReference = FirebaseDatabase.getInstance().getReference(name);



    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        DataSnapshot dataSnapshot = task.getResult();
                        String description1 = String.valueOf(dataSnapshot.child("Description").getValue());
                        description.setText(description1);
                        url = String.valueOf(dataSnapshot.child("url").getValue());
                        Uri uri = Uri.parse(url);
                        video.setVideoURI(uri);
                        video.start();
                    }
                    else
                    {
                        Toast.makeText(watch_Movie.this,"Not Found",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(watch_Movie.this,"Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void edit(View view) {
        Intent intent = new Intent(watch_Movie.this,Edit_Movie.class);
        intent.putExtra("category",name);
        intent.putExtra("name",Movie);
        intent.putExtra("id",id);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    public void remove(View view) {
        databaseReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(watch_Movie.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(watch_Movie.this,"Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}