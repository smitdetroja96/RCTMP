package com.example.rctmp;

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

    public HistoryListAdapter(ArrayList<HistoryBooksClass> historyItemsList) {
        historyItems = historyItemsList;
    }

    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new MyViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListAdapter.MyViewHolder2 holder, int position) {

        String title_val = "Title: " + historyItems.get(position).getMybook().name;
        String author_val = "Author: " + historyItems.get(position).getMybook().authors;
        String date_val = "Check-in Date: " + historyItems.get(position).checkInDate;

        holder.title.setText(title_val);
        holder.author.setText(author_val);
        holder.date.setText(date_val);
    }

    @Override
    public int getItemCount() {return historyItems.size();}


    public static class MyViewHolder2 extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView title,date,author;
        CardView cardView;

        public MyViewHolder2(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_author);
            date = itemView.findViewById(R.id.tv_check_in_date);
            cardView = itemView.findViewById(R.id.history_item_card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select An Option");
            contextMenu.add(this.getAdapterPosition(),1,0,"Add to Favourites");
            contextMenu.add(this.getAdapterPosition(),2,0,"Show Book Details");
            contextMenu.add(this.getAdapterPosition(),3,0,"More Share Book");
        }
    }

    public void filterList(ArrayList<HistoryBooksClass> filtered)
    {
        historyItems = filtered;
        notifyDataSetChanged();
    }


}
