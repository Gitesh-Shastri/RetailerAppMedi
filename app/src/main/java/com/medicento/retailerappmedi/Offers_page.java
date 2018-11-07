package com.medicento.retailerappmedi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.retailerappmedi.data.Medicine;
import com.medicento.retailerappmedi.data.OrderedMedicine;
import com.medicento.retailerappmedi.data.OrderedMedicineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Offers_page extends AppCompatActivity implements  OrderedMedicineAdapter.OverallCostChangeListener {

    Button addTOCart, filter;
    TextView available;
    RecyclerView mOrderedMedicinesListView;
    AutoCompleteTextView mMedicineList;
    public static OrderedMedicineAdapter mOrderedMedicineAdapter;
    public static ArrayList<String> medicine1;
    public static ArrayList<Medicine> MedicineDataList;
    Animation mAnimation;
    public static String message = "";
    ProgressDialog pDialog;
    ArrayAdapter<String> mMedicineAdapter;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_page);
        addTOCart = findViewById(R.id.cart);
        filter = findViewById(R.id.filter1);
        available = findViewById(R.id.notavailable);
        mMedicineList = (AutoCompleteTextView) findViewById(R.id.medicine_edit_tv2);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mOrderedMedicinesListView = findViewById(R.id.ordered_medicines_list2);
        mOrderedMedicinesListView.setLayoutManager(new LinearLayoutManager(this));
        mOrderedMedicinesListView.setHasFixedSize(true);
        medicine1 = new ArrayList<>();
        MedicineDataList = new ArrayList<>();
        new GetNames().execute();
        if(message.equals("Offer Available")) {
            available.setText(message);
            available.setVisibility(View.INVISIBLE);
        } else {
            available.setText(message);
            available.setVisibility(View.VISIBLE);
        }
        mMedicineAdapter = new ArrayAdapter<String>(Offers_page.this, R.layout.support_simple_spinner_dropdown_item, medicine1);
        mMedicineList.setAdapter(mMedicineAdapter);
        mOrderedMedicineAdapter = new OrderedMedicineAdapter(new ArrayList<OrderedMedicine>());
        mOrderedMedicinesListView.setAdapter(mOrderedMedicineAdapter);
        mOrderedMedicineAdapter.setOverallCostChangeListener(this);
        addTOCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amIConnect()) {
                    Toast.makeText(Offers_page.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = new Intent(Offers_page.this, AddToCart.class);
                    intent.putExtra("myList", mOrderedMedicineAdapter.getList());
                    startActivity(intent);
                }
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Offers_page.this, Filter1.class);
                startActivityForResult(intent, 1);
            }
        });
        mMedicineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View view1 = getWindow().getCurrentFocus();
                InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                ime.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                mMedicineList.setText("");
                Medicine medicine = null;
                for (Medicine med : MedicineDataList) {
                    if (med.getMedicentoName().equals(mMedicineAdapter.getItem(position))) {
                        medicine = med;
                        break;
                    }
                }
                mOrderedMedicineAdapter.add(new OrderedMedicine(medicine.getMedicentoName(),
                        medicine.getCompanyName(),
                        1,
                        medicine.getPrice(),
                        medicine.getPrice(),
                        medicine.getMstock(),
                        medicine.getCode()));
                mOrderedMedicinesListView.smoothScrollToPosition(0);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = (int) viewHolder.itemView.getTag();
                mOrderedMedicineAdapter.remove(pos);
            }
        }).attachToRecyclerView(mOrderedMedicinesListView);
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
    }

    private boolean amIConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onCostChanged(float newCost) {

    }

    private class GetNames extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Offers_page.this);
            pDialog.setTitle("Loading Initial Data");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser sh = new JsonParser();
            String url = "https://medicento-api.herokuapp.com/product/offer";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e("Gitesh", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    message = jsonObj.getString("message");
                    if (message.equals("Offer Available")) {
                        // Getting JSON Array node
                        Log.i("OfferPage", jsonStr);
                        JSONObject jsonObj1 = jsonObj.getJSONObject("offermedicine");
                        JSONArray medicine = jsonObj1.getJSONArray("products");
                        medicine1 = new ArrayList<>();
                        MedicineDataList = new ArrayList<>();
                        // looping through All Contacts
                        for (int i = 0; i < medicine.length(); i++) {
                            JSONObject c = medicine.getJSONObject(i);
                            MedicineDataList.add(new Medicine(
                                    c.getString("medicento_name"),
                                    c.getString("company_name"),
                                    c.getInt("price"),
                                    c.getString("_id"),
                                    c.getInt("stock"),
                                    c.getString("item_code")
                            ));
                            medicine1.add(c.getString("medicento_name"));
                            }
                        }
                    } catch( final JSONException e){
                        Toast.makeText(Offers_page.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Gitesh", "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
            } else {
                Log.e("Gitesh", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            mMedicineAdapter = new ArrayAdapter<String>(Offers_page.this, R.layout.support_simple_spinner_dropdown_item,medicine1);
            mMedicineList.setAdapter(mMedicineAdapter);
            mMedicineList.setEnabled(true);
            Toast.makeText(Offers_page.this,"Now You Can Choose Medicine", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equals("company")) {
                    ArrayList<Medicine> manu = (ArrayList<Medicine>) data.getSerializableExtra("list");
                    ArrayList<String> medicine2 = new ArrayList<>();
                    for (Medicine manu1 : manu) {
                        medicine2.add(manu1.getMedicentoName());
                    }
                    mMedicineAdapter = new ArrayAdapter<String>(Offers_page.this, R.layout.support_simple_spinner_dropdown_item, medicine2);
                    mMedicineList.setAdapter(mMedicineAdapter);
                }
            }
        }
    }
}
