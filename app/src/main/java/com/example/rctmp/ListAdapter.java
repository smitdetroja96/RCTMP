package com.example.rctmp;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.MyViewHolder>{

    ArrayList<BooksClass> firebase_books;
    Context context;

    public ListAdapter(Context context1 , ArrayList<BooksClass> books)
    {
        this.context = context1;
        this.firebase_books = books;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book,parent,false);

        return new MyViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

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
    }

    @Override
    public int getItemCount() {
        return firebase_books.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        TextView title,author,material_type,status,publisher,due_date;
        CardView cardView;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_author);
            material_type = itemView.findViewById(R.id.tv_material_type);
            status = itemView.findViewById(R.id.tv_status);
            publisher = itemView.findViewById(R.id.tv_publisher);
            relativeLayout = itemView.findViewById(R.id.item_relative_layout);
            due_date = itemView.findViewById(R.id.tv_due_date);
            cardView = itemView.findViewById(R.id.item_book_card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select An Option");
            contextMenu.add(this.getAdapterPosition(),1,0,"Add To WishList");
            contextMenu.add(this.getAdapterPosition(),2,0,"Show Book Details");
            contextMenu.add(this.getAdapterPosition(),3,0,"Share Book");
        }

    }

    public void removeItem(int position)
    {
        firebase_books.remove(position);
        notifyDataSetChanged();
    }

    public void filter_list(ArrayList<BooksClass> filtered)
    {
        firebase_books = filtered;
        notifyDataSetChanged();
    }

}
