package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.MyviewHolder> {

    Context context;
    ArrayList<Categories> list;
    static RecyclerViewClickListener listener;

    public Categories_Adapter(Context context, ArrayList<Categories> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        Categories categories = list.get(position);
        holder.category_names.setText(categories.getName());

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

        TextView category_names;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            category_names = itemView.findViewById(R.id.category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(itemView,getAdapterPosition());

        }
    }

}
