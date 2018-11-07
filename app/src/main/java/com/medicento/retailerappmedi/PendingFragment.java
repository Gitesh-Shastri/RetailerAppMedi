package com.medicento.retailerappmedi;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.OrderAdapter;
import com.medicento.retailerappmedi.data.OrderAdapterC;
import com.medicento.retailerappmedi.data.RecentOrder;
import com.medicento.retailerappmedi.data.RecentOrderC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingFragment extends Fragment {

    public static String url = "";
    ProgressDialog progressDialog;
    private static OrderAdapterC mOrder;
    RecyclerView iv;
    public static View view;
    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                url = "https://medicento-api.herokuapp.com/product/recent_order/"+sharedPreferences.getString(Constants.SALE_PHARMAID, "") + "?status=Active";
                view = inflater.inflate(R.layout.fragment_pending, container, false);
                mOrder = new OrderAdapterC(new ArrayList<RecentOrderC>());
                iv = (RecyclerView) view.findViewById(R.id.listview1);
                iv.setLayoutManager(new LinearLayoutManager(getActivity()));
                iv.setHasFixedSize(true);
                new GetOrder().execute();
                return view;
    }
    public class GetOrder extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonParser sh = new JsonParser();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e("Gitesh", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray("orders");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject order = jsonArray.getJSONObject(i);
                            mOrder.add(new RecentOrderC(order.getString("order_id"),
                                    order.getString("created_at"),
                                    order.getInt("grand_total")
                            ));
                        }
                } catch (final JSONException e) {
                    Log.e("Gitesh", "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e("Gitesh", "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                iv.setAdapter(mOrder);
        }
    }

}
