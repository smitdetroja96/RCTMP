package com.example.rctmp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.MyViewHolder>{

    ArrayList<String> names;
    Context context;

    public ListAdapter(Context context1 , ArrayList<String> name)
    {
        this.context = context1;
        this.names = name;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(names.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        TextView title;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            cardView = itemView.findViewById(R.id.item_book_card_view);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select An Option");
            contextMenu.add(this.getAdapterPosition(),1,0,"Menu1");
            contextMenu.add(this.getAdapterPosition(),2,0,"Menu2");
        }
    }

    public void removeItem(int position)
    {
        names.remove(position);
        notifyDataSetChanged();
    }

    public void filter_list(ArrayList<String> filtered)
    {
        names = filtered;
        notifyDataSetChanged();
    }

}
