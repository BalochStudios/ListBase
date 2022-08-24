package com.example.ListBase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {
    private ArrayList<ToDoList> dataSet;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    

    public ListAdapter(ArrayList<ToDoList> dataSet, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_todolist, parent, false);
        return new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        ToDoList currentList = dataSet.get(position);
        holder.tvName.setText(currentList.getListName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null,holder.itemView,holder.getAdapterPosition(),0);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    return onItemLongClickListener.onItemLongClick(null, holder.itemView, holder.getAdapterPosition(), 0);
                } else {
                    return false;
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{
        TextView tvName;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
