package com.medicento.retailerappmedi.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class ManufacturerResAdapter extends RecyclerView.Adapter<ManufacturerResAdapter.ManufacturerViewHolder> {
    public ArrayList<Manufacturer> manufacturers;
    int mOverallCost = 0;
    OverallCostChangeListener mOverallCostChangeListener;

    public void setOverallCostChangeListener(OverallCostChangeListener listener) {
        this.mOverallCostChangeListener = listener;
    }

    @NonNull
    public ManufacturerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.manufacturer, parent, false);

        ManufacturerViewHolder viewHolder = new ManufacturerViewHolder(view);
        return viewHolder;

    }
    @NonNull
    public void onBindViewHolder(@NonNull ManufacturerViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setTag(position);
    }

    public ManufacturerResAdapter(ArrayList<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    public ArrayList<Manufacturer> getmOrderList() {
        return manufacturers;
    }

    public void add(Manufacturer distributorName) {
        for(Manufacturer list: manufacturers) {
            if(list.getName().equals(distributorName.getName())){
                return;
            }
        }
        manufacturers.add(distributorName);
        notifyItemInserted(0);
    }

    public int getItemCount() {
        return manufacturers.size();
    }

    public class ManufacturerViewHolder extends RecyclerView.ViewHolder{
        TextView manuName;
        CheckBox selected;

        public ManufacturerViewHolder(View itemView) {
            super(itemView);
            manuName = itemView.findViewById(R.id.company1);
            selected = itemView.findViewById(R.id.companyCheck);
        }
        public void bind(int pos) {
            final Manufacturer manufacturer = manufacturers.get(pos);
            manuName.setText(manufacturer.getName());
            selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    manufacturer.setSelected(isChecked);
                    if(isChecked) {
                        mOverallCost += 1;
                        if (mOverallCostChangeListener != null)
                            mOverallCostChangeListener.onCostChanged(mOverallCost);
                    } else {
                        mOverallCost -= 1;
                        if (mOverallCostChangeListener != null)
                            mOverallCostChangeListener.onCostChanged(mOverallCost);
                    }
                }
            });
        }
    }

    public int getOverallCost() {
        return mOverallCost;
    }

    public interface OverallCostChangeListener {
        void onCostChanged(int newCost);
    }

}
