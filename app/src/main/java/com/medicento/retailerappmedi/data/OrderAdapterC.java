package com.medicento.retailerappmedi.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class OrderAdapterC extends RecyclerView.Adapter<OrderAdapterC.RecentOrderCViewHolder>{

    public static ArrayList<RecentOrderC> mOrderList;

    public OrderAdapterC(ArrayList<RecentOrderC> list) {
        mOrderList = list;
    }


    @NonNull
    @Override
    public RecentOrderCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recent_orderc, parent, false);

        OrderAdapterC.RecentOrderCViewHolder viewHolder = new OrderAdapterC.RecentOrderCViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentOrderCViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public void add(RecentOrderC recentOrder){
        mOrderList.add(recentOrder);
        notifyItemInserted(0);
    }

    public static class RecentOrderCViewHolder extends RecyclerView.ViewHolder{

        TextView orderId, orderDate, orderStatus,orderCost;

        public RecentOrderCViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orders_id_tv);
            orderDate = itemView.findViewById(R.id.order_date_tv);
            orderCost = itemView.findViewById(R.id.order_cost_tv);
        }
        public void bind(int pos) {
            RecentOrderC recentOrder = mOrderList.get(pos);
            orderId.setText(recentOrder.getpOrderId());
            orderDate.setText(recentOrder.getpDate());
            orderCost.setText(recentOrder.getTotal() + "");
        }
    }
}
