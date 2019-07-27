package com.example.rctmp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouritesAdapter extends  RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>{

    ArrayList<BooksClass> firebase_books;
    Context context;

    public FavouritesAdapter(Context context1 , ArrayList<BooksClass> books)
    {
        this.context = context1;
        this.firebase_books = books;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book,parent,false);

        return new MyViewHolder(v,context);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(firebase_books.get(position).getName());
        String empty = "empty@2019";
        String temp = firebase_books.get(position).getAuthors();
        if(temp.equals(empty))
            holder.author.setVisibility(View.GONE);
        else
            holder.author.setText(temp);

        holder.status.setText("" + firebase_books.get(position).getQuantity());

        temp = firebase_books.get(position).getPublisher();
        if(temp.equals(empty))
            holder.publisher.setVisibility(View.GONE);
        else
            holder.publisher.setText(temp);
        holder.due_date.setVisibility(View.GONE);
        int quantity = firebase_books.get(position).getQuantity();
        if(quantity==0){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#CFC9D1"));
        }
        else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FCFCFC"));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewDetails.class);
                i.putExtra("BookDetails",firebase_books.get(position));
                i.putExtra("isMain",true);
                i.putExtra("showRemove",true);
                ((Activity)context).startActivityForResult(i,420);
            }
        });
    }

    @Override
    public int getItemCount() {
        return firebase_books.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,author,material_type,status,publisher,due_date;
        CardView cardView;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView,final Context context) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_author);
            material_type = itemView.findViewById(R.id.tv_material_type);
            status = itemView.findViewById(R.id.tv_status);
            publisher = itemView.findViewById(R.id.tv_publisher);
            relativeLayout = itemView.findViewById(R.id.item_relative_layout);
            due_date = itemView.findViewById(R.id.tv_due_date);
            cardView = itemView.findViewById(R.id.item_book_card_view);
        }

    }

    public void removeItem(int position)
    {
        firebase_books.remove(position);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<BooksClass> filtered)
    {
        firebase_books = filtered;
        notifyDataSetChanged();
    }



}
