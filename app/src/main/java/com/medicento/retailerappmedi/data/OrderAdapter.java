package com.medicento.retailerappmedi.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.RecentOrderViewHolder>{
    public ArrayList<RecentOrder> mOrderList;

    public OrderAdapter(ArrayList<RecentOrder> list) {
        mOrderList = list;
    }

    @NonNull
    @Override
    public RecentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recent_order, parent, false);

        RecentOrderViewHolder viewHolder = new RecentOrderViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecentOrderViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public void add(RecentOrder recentOrder){
        mOrderList.add(recentOrder);
        notifyItemInserted(0);
    }

    public class RecentOrderViewHolder extends RecyclerView.ViewHolder{

        TextView orderId, orderDate, orderStatus,orderCost;

        public RecentOrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orders_id_tv);
            orderDate = itemView.findViewById(R.id.order_date_tv);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderCost = itemView.findViewById(R.id.order_cost_tv);
        }
        public void bind(int pos) {
            RecentOrder recentOrder = mOrderList.get(pos);
            orderId.setText(recentOrder.getOrderId());
            orderDate.setText(recentOrder.getDate());
            orderStatus.setText(recentOrder.getStatus());
            orderCost.setText(recentOrder.getCost() + "");
        }
    }
}
