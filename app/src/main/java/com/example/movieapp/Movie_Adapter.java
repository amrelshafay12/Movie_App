package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Movie_Adapter extends RecyclerView.Adapter<Movie_Adapter.MyviewHolder>{

    Context context;
    ArrayList<movie> list;
    static Movie_Adapter.RecyclerViewClickListener listener;

    public Movie_Adapter(Context context, ArrayList<movie> list, Movie_Adapter.RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Movie_Adapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        return new Movie_Adapter.MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Movie_Adapter.MyviewHolder holder, int position) {
        movie movie = list.get(position);
        holder.moive_names.setText(movie.getName());
        holder.id = movie.getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView moive_names;
        String id;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            moive_names = itemView.findViewById(R.id.movie_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(itemView,getAdapterPosition());
        }
    }
}
