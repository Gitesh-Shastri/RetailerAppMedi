package com.medicento.retailerappmedi.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class ResponseAdapter  extends RecyclerView.Adapter<ResponseAdapter.DistributorViewHolder>{

    public ArrayList<Response> mOrderList;

    public ResponseAdapter(ArrayList<Response> mOrderList) {
        this.mOrderList = mOrderList;
    }

    @NonNull
    @Override
    public DistributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_response, parent, false);

        DistributorViewHolder viewHolder = new DistributorViewHolder(view);
        return viewHolder;

    }

    public ArrayList<Response> getmOrderList() {
        return mOrderList;
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public void add(Response distributorName) {
        for(Response list: mOrderList) {
            if(list.getName().equals(distributorName.getName())){
                return;
            }
        }
        mOrderList.add(distributorName);
        notifyItemInserted(0);
    }

    public class DistributorViewHolder extends RecyclerView.ViewHolder{
        TextView distName,order,delivery,bounce,payment,returns,behaviour;

        public DistributorViewHolder(View itemView) {
            super(itemView);
            distName = itemView.findViewById(R.id.pdist);
            order = itemView.findViewById(R.id.porder);
            delivery = itemView.findViewById(R.id.pdelivery);
            payment = itemView.findViewById(R.id.ppayment);
            bounce = itemView.findViewById(R.id.pbounce);
            returns = itemView.findViewById(R.id.preturn);
            behaviour = itemView.findViewById(R.id.pbeahviour);
        }
        public void bind(int pos) {
            final Response distributorName = mOrderList.get(pos);
            distName.append(distributorName.getName());
            order.append(distributorName.getOrder()+" ");
            delivery.append(distributorName.getDelivery()+" ");
            payment.append(distributorName.getPayment()+" ");
            bounce.append(distributorName.getBounce()+ " ");
            behaviour.append(distributorName.getBehaviour()+" ");
            returns.append(distributorName.getReturns()+" ");
        }
    }
}