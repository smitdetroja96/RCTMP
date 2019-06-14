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

public class IssuedBooksAdapter extends RecyclerView.Adapter<IssuedBooksAdapter.MyViewHolder3> {

    private ArrayList<HistoryBooksClass> historyItems;

    public IssuedBooksAdapter(ArrayList<HistoryBooksClass> historyItemsList) {
        historyItems = historyItemsList;
    }

    @Override
    public MyViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);

        return new MyViewHolder3(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuedBooksAdapter.MyViewHolder3 holder, int position) {

        String title_val = "Title: " + historyItems.get(position).title;
        String author_val = "Author: " + historyItems.get(position).author;
        String date_val = "Due Date: " + historyItems.get(position).checkInDate;

        holder.title.setText(title_val);
        holder.author.setText(author_val);
        holder.date.setText(date_val);
    }

    @Override
    public int getItemCount() {return historyItems.size();}


    public static class MyViewHolder3 extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView title,date,author;
        CardView cardView;

        public MyViewHolder3(View itemView) {
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
            contextMenu.add(this.getAdapterPosition(),1,0,"Menu1");
            contextMenu.add(this.getAdapterPosition(),2,0,"Menu2");
            contextMenu.add(this.getAdapterPosition(),3,0,"More Details");
        }
    }

    public void filterList(ArrayList<HistoryBooksClass> filtered)
    {
        historyItems = filtered;
        notifyDataSetChanged();
    }


}
