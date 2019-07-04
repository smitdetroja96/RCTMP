package com.example.rctmp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder2> {

    private ArrayList<HistoryBooksClass> historyItems;
    Context context;

    public HistoryListAdapter(ArrayList<HistoryBooksClass> historyItemsList,Context cont) {
        historyItems = historyItemsList;
        context = cont;
    }

    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new MyViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListAdapter.MyViewHolder2 holder, final int position) {

        String title_val = "Title: " + historyItems.get(position).getMybook().name;
        String author_val = "Author: " + historyItems.get(position).getMybook().authors;
        String date_val = "Check-in Date: " + historyItems.get(position).checkInDate;

        holder.title.setText(title_val);
        holder.author.setText(author_val);
        holder.date.setText(date_val);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewDetails.class);
                i.putExtra("BookDetails",historyItems.get(position).getMybook());
                i.putExtra("isMain",true);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {return historyItems.size();}


    public static class MyViewHolder2 extends RecyclerView.ViewHolder {

        TextView title,date,author;
        CardView cardView;

        public MyViewHolder2(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_author);
            date = itemView.findViewById(R.id.tv_check_in_date);
            cardView = itemView.findViewById(R.id.history_item_card_view);
        }
    }

    public void filterList(ArrayList<HistoryBooksClass> filtered)
    {
        historyItems = filtered;
        notifyDataSetChanged();
    }


}
