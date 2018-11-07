package com.medicento.retailerappmedi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.MakeYourOwn;
import com.medicento.retailerappmedi.data.Medicine;
import com.medicento.retailerappmedi.data.OrderedMedicine;
import com.medicento.retailerappmedi.data.OrderedMedicineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.medicento.retailerappmedi.PlaceOrderActivity.mOrderedMedicineAdapter;

public class AddToCart extends AppCompatActivity implements OrderedMedicineAdapter.OverallCostChangeListener{
    RecyclerView mOrderedMedicinesListView;
    Animation mAnimation;
    AutoCompleteTextView mMedicineList;
    private ProgressDialog pDialog;
    ArrayList<String> medicine1;
    public static ArrayList<Medicine> MedicineDataList;
    public static String url;
    Button place,addMore,make;
    AlertDialog alert;
    public static ArrayList<MakeYourOwn> makeYourOwns;
    SharedPreferences mSharedPref;
    ArrayAdapter<String> mMedicineAdapter;
    public static String cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        place = findViewById(R.id.place);
        mMedicineList = findViewById(R.id.medicine_edit_tv1);
        addMore = findViewById(R.id.AddMore1);
        make = findViewById(R.id.make);
        mOrderedMedicinesListView = findViewById(R.id.ordered_medicines_list1);
        mOrderedMedicinesListView.setLayoutManager(new LinearLayoutManager(this));
        mOrderedMedicinesListView.setHasFixedSize(true);
        mOrderedMedicineAdapter = new OrderedMedicineAdapter(new ArrayList<OrderedMedicine>());
        mOrderedMedicinesListView.setAdapter(mOrderedMedicineAdapter);
        mOrderedMedicineAdapter.setOverallCostChangeListener(this);
        ArrayList<OrderedMedicine> medicinesList = (ArrayList<OrderedMedicine>)getIntent().getSerializableExtra("myList");
        Gson gson = new Gson();
        String json = mSharedPref.getString("saved", null);
        Type type = new TypeToken<ArrayList<Medicine>>() {}.getType();
        MedicineDataList = gson.fromJson(json, type);
        medicine1 = new ArrayList<>();
        for(Medicine med: MedicineDataList){
            medicine1.add(med.getMedicentoName());
        }
        makeYourOwns = new ArrayList<>();
        mMedicineAdapter = new ArrayAdapter<String>(AddToCart.this, R.layout.support_simple_spinner_dropdown_item,medicine1);
        mMedicineList.setAdapter(mMedicineAdapter);
        mMedicineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View view1 = getWindow().getCurrentFocus();
                InputMethodManager ime = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                ime.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                mMedicineList.setText("");
                Medicine medicine = null;
                for(Medicine med: MedicineDataList) {
                    if(med.getMedicentoName().equals(mMedicineAdapter.getItem(position))) {
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
        for (OrderedMedicine medicine: medicinesList){
            mOrderedMedicineAdapter.add(medicine);
            mOrderedMedicinesListView.smoothScrollToPosition(0);
        }
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
        makeYourOwns = (ArrayList<MakeYourOwn>) getIntent().getSerializableExtra("make");
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amIConnect()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddToCart.this);
                    builder.setTitle("No Internet Connection");
                    builder.setIcon(R.mipmap.ic_launcher_new);
                    builder.setCancelable(false);
                    builder.setMessage("Please Connect To The Internet")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            });
                    alert = builder.create();
                    alert.show();
                    return;
                } else {
                    String json = extractJsonFromOrderItemsList(mOrderedMedicineAdapter.getList(), makeYourOwns, mSharedPref.getString(Constants.SALE_PHARMAID, ""), mSharedPref.getString(Constants.SALE_PERSON_ID, ""));
                    new PlaceOrder().execute(json);
                }
            }
        });
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMedicineList.setVisibility(View.VISIBLE);
            }
        });
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddToCart.this, Medicine_List.class);
                intent.putExtra("myList", mOrderedMedicineAdapter.getList());
                ArrayList<OrderedMedicine> medicines = mOrderedMedicineAdapter.getList();
                int count = 0;
                for(OrderedMedicine med: medicines) {
                    count += med.getQty();
                }
                intent.putExtra("count", count);
                startActivityForResult(intent, 3);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_to_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.place_order) {
            if(!amIConnect()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddToCart.this);
                builder.setTitle("No Internet Connection");
                builder.setIcon(R.mipmap.ic_launcher_new);
                builder.setCancelable(false);
                builder.setMessage("Please Connect To The Internet")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.dismiss();
                            }
                        });
                alert = builder.create();
                alert.show();
            } else {
                String json = extractJsonFromOrderItemsList(mOrderedMedicineAdapter.getList(), makeYourOwns, mSharedPref.getString(Constants.SALE_PHARMAID, ""), mSharedPref.getString(Constants.SALE_PERSON_ID, ""));
                new PlaceOrder().execute(json);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private static String extractJsonFromOrderItemsList(ArrayList<OrderedMedicine> data, ArrayList<MakeYourOwn> data1, String pId, String sId) {
        JSONArray orderItems = new JSONArray();
        try {
            for (OrderedMedicine orderedMedicine : data) {
                JSONObject object = new JSONObject();
                object.put("medicento_name", orderedMedicine.getMedicineName());
                object.put("company_name", orderedMedicine.getMedicineCompany());
                object.put("pharma_id", pId);
                object.put("code", orderedMedicine.getCode());
                object.put("qty", String.valueOf(orderedMedicine.getQty()));
                object.put("rate", String.valueOf(orderedMedicine.getRate()));
                object.put("cost", String.valueOf(orderedMedicine.getCost()));
                object.put("salesperson_id", sId);
                orderItems.put(object);
            }
            JSONArray allDataArray = new JSONArray();
            for(MakeYourOwn makeYourOwn: makeYourOwns){
                JSONObject each = new JSONObject();
                try {
                    Log.i("Make", makeYourOwn.getName());
                    each.put("Chosen", makeYourOwn.getName());
                } catch (JSONException e){
                    e.printStackTrace();
                }
                allDataArray.put(each);
            }
            JSONObject object = new JSONObject();
            try {
                object.put("choosen_data", allDataArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderItems.put(object);
        } catch (JSONException e) {
            Log.v("Saf", e.toString());
        }
        Log.i("orderItems", orderItems.toString());
        return orderItems.toString();
    }
    @Override
    public void onCostChanged(float newCost) {
        cost = newCost + "";
    }

    public class PlaceOrder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddToCart.this);
            pDialog.setTitle("Placing Order");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            Intent intent = new Intent(AddToCart.this, OrderConfirmed.class);
            intent.putExtra("TotalCost", cost);
            intent.putExtra("json", s);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {
            String jsonResponse = null;
            url = Constants.PLACE_ORDER_URL;
            String jsonData = strings[0];
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                URL url1 = new URL(url);
                urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(jsonData);
                writer.close();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e("Git", "Error response code : " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("Git", "Error IOException");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 3) {
            if(resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                ArrayList<MakeYourOwn> makeYourOwns1 = (ArrayList<MakeYourOwn>) data.getSerializableExtra("make");
                for(MakeYourOwn list: makeYourOwns1) {
                    makeYourOwns.add(list);
                }
                if(result.equals("continue")) {
                    Intent intent = new Intent();
                    intent.putExtra("myList", (ArrayList<OrderedMedicine>)data.getSerializableExtra("myList"));
                    intent.putExtra("make", makeYourOwns);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if(result.equals("submit")) {
                    Toast.makeText(AddToCart.this, "Submiited Your Own List", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private boolean amIConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
