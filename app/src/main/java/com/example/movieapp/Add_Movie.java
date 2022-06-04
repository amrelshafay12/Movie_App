package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Add_Movie extends AppCompatActivity {

    String movie;
    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button button;
    EditText movie_name,movie_id,movie_rate,movie_description;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_movie);

        movie = getIntent().getStringExtra("name");
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference(movie);

        videoView = findViewById(R.id.videoView2);
        movie_id = findViewById(R.id.movie_id);
        movie_name = findViewById(R.id.movie_name);
        movie_description = findViewById(R.id.movie_description);
        movie_rate = findViewById(R.id.movie_rate);
        button = findViewById(R.id.movie_add);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadVideo();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_VIDEO || resultCode == RESULT_OK || data != null || data.getData() != null)
        {
            videoUri = data.getData();

            videoView.setVideoURI(videoUri);
        }
    }

    public void ChooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO);
    }


    private String getExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void ShowVideo(View view) {
    }


    private void UploadVideo()
    {
        String videoName = movie_name.getText().toString();
        String id = movie_id.getText().toString();
        String description = movie_description.getText().toString();
        String rate = movie_rate.getText().toString();
        if(videoUri != null || !TextUtils.isEmpty(videoName) || !TextUtils.isEmpty(id) || !TextUtils.isEmpty(description) || !TextUtils.isEmpty(rate))
        {
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getExt(videoUri));
            uploadTask = reference.putFile(videoUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        Toast.makeText(Add_Movie.this,"Data Saved",Toast.LENGTH_LONG).show();
                        HashMap movie = new HashMap();
                        movie.put("id",id);
                        movie.put("Name",videoName);
                        movie.put("Description",description);
                        movie.put("rate",rate);
                        movie.put("url",downloadUrl.toString());
                        databaseReference.child(id).setValue(movie);
                        Intent intent = new Intent(Add_Movie.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Add_Movie.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Add_Movie.this,"All Files are required",Toast.LENGTH_LONG).show();
        }
    }
}