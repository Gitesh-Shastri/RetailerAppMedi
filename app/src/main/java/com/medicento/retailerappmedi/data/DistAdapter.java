package com.medicento.retailerappmedi.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class DistAdapter extends RecyclerView.Adapter<DistAdapter.DistributorViewHolder>{
    public ArrayList<DistributorName> mOrderList;

    public DistAdapter(ArrayList<DistributorName> mOrderList) {
        this.mOrderList = mOrderList;
    }

    @NonNull
    @Override
    public DistributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.distributor_review_item, parent, false);

        DistributorViewHolder viewHolder = new DistributorViewHolder(view);
        return viewHolder;

    }

    public ArrayList<DistributorName> getmOrderList() {
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

    public void add(DistributorName distributorName) {
        for(DistributorName list: mOrderList) {
            if(list.getName().equals(distributorName.getName())){
                return;
            }
        }
        mOrderList.add(distributorName);
        notifyItemInserted(0);
    }

    public class DistributorViewHolder extends RecyclerView.ViewHolder{
        TextView distName;
        RadioGroup radioGroup;

        public DistributorViewHolder(View itemView) {
            super(itemView);
            distName = itemView.findViewById(R.id.dist);
            radioGroup = itemView.findViewById(R.id.dist1);
        }

        public void bind(int pos) {
            final DistributorName distributorName = mOrderList.get(pos);
            distName.setText(distributorName.getName());
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (group.getCheckedRadioButtonId()){
                        case R.id.highly: distributorName.setRating(1);
                                        break;
                        case R.id.difficult: distributorName.setRating(2);
                            break;
                        case R.id.neutral: distributorName.setRating(3);
                            break;
                        case R.id.easy: distributorName.setRating(4);
                            break;
                        case R.id.very: distributorName.setRating(5);
                            break;
                    }
                }
            });
        }
    }
}