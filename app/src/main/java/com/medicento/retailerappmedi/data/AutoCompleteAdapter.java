package com.medicento.retailerappmedi.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<MedicineAuto> {

    private List<MedicineAuto> medicineAutos, tempNames;

    public AutoCompleteAdapter(Context context, ArrayList<MedicineAuto> objects) {
        super(context, R.layout.medicine_row, objects);
        this.medicineAutos = new ArrayList<>(objects);
        this.tempNames = new ArrayList<>(objects);
    }

    @Nullable
    @Override
    public MedicineAuto getItem(int position) {
        return medicineAutos.get(position);
    }

    @Override
    public int getCount() {
        return medicineAutos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MedicineAuto medicineAuto = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.medicine_row,parent,false
            );
        }

        TextView name, company, mrp;

        name = convertView.findViewById(R.id.medicine_name_auto);
        company = convertView.findViewById(R.id.company_name_auto);
        mrp = convertView.findViewById(R.id.medicine_mrp_auto);


        if(medicineAuto != null) {
            name.setText(medicineAuto.getName());
            company.setText("Company : " + medicineAuto.getCompany());
            mrp.setText("\u20B9"+medicineAuto.getMrp()+"");
        }

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return medicineFilter;
    }

    Filter medicineFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            MedicineAuto medicineAuto = (MedicineAuto) resultValue;
            return medicineAuto.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            List<MedicineAuto> medicineautos = new ArrayList<>();

            if(constraint == null || constraint.length() == 0 ) {
                medicineautos.addAll(tempNames);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MedicineAuto medicineAuto : tempNames) {
                    if (medicineAuto.getName().toLowerCase().startsWith(filterPattern)) {
                        medicineautos.add(medicineAuto);
                    }
                }
            }

            results.values = medicineautos;
            results.count = medicineautos.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.values != null) {
                Log.d("gitesh_rsult", results.count+"");
                medicineAutos.clear();
                medicineAutos.addAll((ArrayList<MedicineAuto>)results.values);
            }
            notifyDataSetChanged();
        }
    };
}
