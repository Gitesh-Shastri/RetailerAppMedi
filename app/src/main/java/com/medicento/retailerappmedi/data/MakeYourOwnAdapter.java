package com.medicento.retailerappmedi.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicento.retailerappmedi.R;

import java.util.ArrayList;

public class MakeYourOwnAdapter extends RecyclerView.Adapter<MakeYourOwnAdapter.MakeYourOwnViewHolder>{
    public ArrayList<MakeYourOwn> mList;

    public MakeYourOwnAdapter(ArrayList<MakeYourOwn> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MakeYourOwnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_make_your_own, parent, false);

        MakeYourOwnViewHolder viewHolder =  new MakeYourOwnViewHolder(view);
        return viewHolder;
    }

    public ArrayList<MakeYourOwn> getmOrderList() {
        return mList;
    }

    @Override
    public void onBindViewHolder(@NonNull MakeYourOwnViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(MakeYourOwn makeYourOwn) {
        for(MakeYourOwn list: mList){
            if(list.getName().equals(makeYourOwn.getName())){
                return;
            }
        }
        mList.add(0, makeYourOwn);
        Log.i("manu2", mList.get(0).getName());
        notifyItemInserted(0);
    }

    public class MakeYourOwnViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public MakeYourOwnViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_name1);
        }

        public void bind(int pos) {
            final MakeYourOwn make = mList.get(pos);
            name.setText(make.getName());
            Log.i("Manu : ", make.getName() + "pos : " + pos);
        }
    }
}
