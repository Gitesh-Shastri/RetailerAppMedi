package com.medicento.retailerappmedi.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class RecentOrderItems extends RecyclerView.Adapter<RecentOrderItems.ViewHolder> {

    public static ArrayList<RecentOrderMedicine> mRecentOrderItems;

    public RecentOrderItems(ArrayList<RecentOrderMedicine> recentOrderItems) {
        mRecentOrderItems = recentOrderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_ordered_medicine,viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name.setText(mRecentOrderItems.get(i).getName());
        viewHolder.price.setText(mRecentOrderItems.get(i).getPrice());
        viewHolder.qty.setText(mRecentOrderItems.get(i).getQty());

    }

    @Override
    public int getItemCount() {
        return mRecentOrderItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, qty, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_name);
            qty = itemView.findViewById(R.id.medicine_qty);
            price = itemView.findViewById(R.id.medicine_cost);
        }
    }
}
