package com.medicento.retailerappmedi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medicento.retailerappmedi.data.OrderAdapterDelivered;
import com.medicento.retailerappmedi.data.RecentOrderDelivered;
import com.medicento.retailerappmedi.data.RecentOrderMedicine;
import com.medicento.retailerappmedi.data.SalesPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

public class RecentOrderActivity extends AppCompatActivity implements OrderAdapterDelivered.OnItemClickListener {

    ProgressDialog progressDialog;

    private static ArrayList<RecentOrderDelivered> mRecentOrder;

    SharedPreferences sharedPreferences;

    SalesPerson salesPerson;

    OrderAdapterDelivered orderAdapterC;
    SharedPreferences mSharedPreferences;

    String url = "";

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order);

        Paper.init(this);

        String cache = Paper.book().read("user");

        if(cache != null && !cache.isEmpty()) {
            salesPerson = new Gson().fromJson(cache, SalesPerson.class);
        }

        url = "https://medicento-api.herokuapp.com/product/recent_order/" + salesPerson.getmAllocatedPharmaId() + "?status=Delivered";

        recyclerView = findViewById(R.id.order_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        new GetOrder().execute();
    }

    @Override
    public void onItemClick1(int position) {

        Intent intent = new Intent(RecentOrderActivity.this, RecentOrderDetails.class);
        intent.putExtra("order", mRecentOrder.get(position));
        startActivity(intent);
    }

    public class GetOrder extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecentOrder = new ArrayList<>();
            progressDialog = new ProgressDialog(RecentOrderActivity.this);
            progressDialog.setMessage("Loading Orders");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.i("urls", url);

            JsonParser sh = new JsonParser();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e("Gitesh", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray("orders");
                    for (int i =0;i<jsonArray.length();i++) {
                        JSONObject order = jsonArray.getJSONObject(i);
                        RecentOrderDelivered recentOrderDelivered = new RecentOrderDelivered(order.getString("order_id"),
                                order.getString("created_at"),
                                Integer.valueOf(order.getString("grand_total")));
                        recentOrderDelivered.setMedicines(new ArrayList<RecentOrderMedicine>());
                        JSONArray medicine = order.getJSONArray("order_items");
                        for(int j=0;j<medicine.length();j++) {
                            JSONObject ordermedicine = medicine.getJSONObject(j);
                            recentOrderDelivered.getMedicines().add(new RecentOrderMedicine(ordermedicine.getString("medicento_name"),
                                    ordermedicine.getInt("quantity")+"",
                                    ordermedicine.getString("total_amount")));
                        }
                        mRecentOrder.add(recentOrderDelivered);
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
            orderAdapterC = new OrderAdapterDelivered(mRecentOrder);
            recyclerView.setAdapter(orderAdapterC);
            orderAdapterC.setOnItemClicklistener(RecentOrderActivity.this);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(RecentOrderActivity.this, PlaceOrderActivity.class));
    }
}
