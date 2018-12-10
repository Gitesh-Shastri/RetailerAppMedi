package com.medicento.retailerappmedi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.medicento.retailerappmedi.data.DistAdapter;
import com.medicento.retailerappmedi.data.DistributorName;
import com.medicento.retailerappmedi.data.Response;
import com.medicento.retailerappmedi.data.ResponseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class SurveyMedi extends AppCompatActivity {
    public static EditText q1;
    private TextView rating;
    public static RadioGroup q3;
    public static RadioButton q3a;
    ProgressDialog pDialog;
    public static Spinner sp;
    CheckBox checkBox;
    RelativeLayout r2,r1,r3,r4,r5,r6,r7,r,r9,r8;
    private RecyclerView d1,d2,d3,d4,d5,d6,d7;
    public static DistAdapter adapter, adapter1,adapter2, adapter3, adapter4, adapter5;
    public static ResponseAdapter response;
    public static String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_medi);
        d1 = (RecyclerView) findViewById(R.id.list_order);
        d2 = (RecyclerView) findViewById(R.id.list_delivery);
        d3 = (RecyclerView) findViewById(R.id.list_bounce);
        d4 = (RecyclerView) findViewById(R.id.list_payment);
        d5 = (RecyclerView)  findViewById(R.id.list_return);
        d6 = (RecyclerView) findViewById(R.id.list_behaviour);
        d7 = (RecyclerView) findViewById(R.id.response);
        sp = (Spinner) findViewById(R.id.AreaSpinner);
        rating = findViewById(R.id.distributor_rating);
        adapter = new DistAdapter(new ArrayList<DistributorName>());
        adapter1 = new DistAdapter(new ArrayList<DistributorName>());
        adapter2 = new DistAdapter(new ArrayList<DistributorName>());
        adapter3 = new DistAdapter(new ArrayList<DistributorName>());
        adapter4 = new DistAdapter(new ArrayList<DistributorName>());
        adapter5 = new DistAdapter(new ArrayList<DistributorName>());
        response = new ResponseAdapter(new ArrayList<Response>());
        q3 = findViewById(R.id.radio_money);
        r1 = findViewById(R.id.l1);
        r2 = findViewById(R.id.l2);
        r3 = findViewById(R.id.l3);
        r4 = findViewById(R.id.l4);
        r5 = findViewById(R.id.l5);
        r6 = findViewById(R.id.l6);
        r7 = findViewById(R.id.l7);
        r8 = findViewById(R.id.l8);
        r9 = findViewById(R.id.l9);
        d7.setLayoutManager(new LinearLayoutManager(this));
        d7.setHasFixedSize(true);
        d6.setLayoutManager(new LinearLayoutManager(this));
        d6.setHasFixedSize(true);
        d6.setAdapter(adapter5);
        d1.setLayoutManager(new LinearLayoutManager(this));
        d1.setHasFixedSize(true);
        d1.setAdapter(adapter);
        d2.setLayoutManager(new LinearLayoutManager(this));
        d2.setHasFixedSize(true);
        d2.setAdapter(adapter1);
        d3.setLayoutManager(new LinearLayoutManager(this));
        d3.setHasFixedSize(true);
        d3.setAdapter(adapter2);
        d4.setLayoutManager(new LinearLayoutManager(this));
        d4.setHasFixedSize(true);
        d4.setAdapter(adapter3);
        d5.setLayoutManager(new LinearLayoutManager(this));
        d5.setHasFixedSize(true);
        d5.setAdapter(adapter4);
        q1 = findViewById(R.id.q1_edit_tv);
        ArrayList<String> area = new ArrayList<>();
        area.add("Kormangla");
        area.add("Taverekere");
        area.add("BTM");
        area.add("HSR Layout");
        area.add("Electronic City");
        area.add("Bellandur");
        area.add("Jayanagar");
        area.add("Whitefield");
        area.add("Bannerghatta Road");
        area.add("Marathali");
        ArrayAdapter<String> aadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,area);
        aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aadapter);
    }

    public void page1(View view) {
        r1.setVisibility(View.VISIBLE);
        r2.setVisibility(View.GONE);
    }

    public void page2(View view) {
        InputMethodManager ime = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        ime.hideSoftInputFromWindow(view.getWindowToken(), 0);
        int id = q3.getCheckedRadioButtonId();
        q3a = (RadioButton) findViewById(id);
        r1.setVisibility(View.GONE);
        r2.setVisibility(View.VISIBLE);
        r3.setVisibility(View.GONE);
    }

    public void page3(View view) {
        r2.setVisibility(View.GONE);
        r3.setVisibility(View.VISIBLE);
        r4.setVisibility(View.GONE);
    }


    public void q4Checked(View view){
        int id = view.getId();
        checkBox = (CheckBox)findViewById(id);
        adapter.add(new DistributorName(checkBox.getText().toString(),0));
        adapter1.add(new DistributorName(checkBox.getText().toString(),0));
        adapter2.add(new DistributorName(checkBox.getText().toString(),0));
        adapter3.add(new DistributorName(checkBox.getText().toString(),0));
        adapter4.add(new DistributorName(checkBox.getText().toString(),0));
        adapter5.add(new DistributorName(checkBox.getText().toString(),0));
    }

    public void page4(View view){
        r3.setVisibility(View.GONE);
        r4.setVisibility(View.VISIBLE);
        r5.setVisibility(View.GONE);
    }

    public void page5(View view) {
        r4.setVisibility(View.GONE);
        r5.setVisibility(View.VISIBLE);
        r6.setVisibility(View.GONE);
    }

    public void page6(View view) {
        r5.setVisibility(View.GONE);
        r6.setVisibility(View.VISIBLE);
        r7.setVisibility(View.GONE);
    }

    public void page7(View view) {
        r6.setVisibility(View.GONE);
        r7.setVisibility(View.VISIBLE);
        r8.setVisibility(View.GONE);
    }

    public void page8(View view) {
        r7.setVisibility(View.GONE);
        r8.setVisibility(View.VISIBLE);
    }

    public void submit(View view)
    {
        ArrayList<DistributorName> distributorRating = adapter.getmOrderList();
        ArrayList<DistributorName> distributorBehaviour = adapter5.getmOrderList();
        ArrayList<DistributorName> distributorReturn = adapter4.getmOrderList();
        ArrayList<DistributorName> distributorPayment = adapter3.getmOrderList();
        ArrayList<DistributorName> distributorBounce = adapter2.getmOrderList();
        ArrayList<DistributorName> distributorDelivery = adapter1.getmOrderList();
        String distributor = "Pharmacy Name : " + q1.getText().toString() + " \n";
        rating.append(" " + q1.getText().toString() + ", ");
        for(int i=0;i<adapter.getItemCount();i++) {
            Response response1 = new Response(distributorRating.get(i).getName(),
                            distributorRating.get(i).getRating(),
                            distributorDelivery.get(i).getRating(),
                            distributorBounce.get(i).getRating(),
                            distributorPayment.get(i).getRating(),
                            distributorReturn.get(i).getRating(),
                            distributorBehaviour.get(i).getRating()
                    );
            response.add(response1);
        }
        d7.setAdapter(response);
        String json = extractJsonFromOrderItemsList(response.getmOrderList());
        new PlaceOrder().execute(json);
    }
    public void login(View view) {
        Intent intent = new Intent(SurveyMedi.this, PlaceOrderActivity.class);
        startActivity(intent);
    }



    private static String extractJsonFromOrderItemsList(ArrayList<Response> data) {
        JSONArray orderItems = new JSONArray();
        String pname = q1.getText().toString();
        if(pname.isEmpty()) {
            pname = " ";
        }
        try {
            for (Response orderedMedicine : data) {
                JSONObject object = new JSONObject();
                object.put("distributor_name", orderedMedicine.getName());
                object.put("distributor_order", orderedMedicine.getOrder());
                object.put("pharma_name", pname);
                object.put("area_name", String.valueOf(sp.getSelectedItem()));
                object.put("sales", q3a.getText().toString());
                object.put("distributor_delivery", orderedMedicine.getDelivery());
                object.put("distributor_payment", orderedMedicine.getPayment());
                object.put("distributor_behaviour", orderedMedicine.getBehaviour());
                object.put("distributor_bounce", orderedMedicine.getBounce());
                object.put("distributor_returns", orderedMedicine.getReturns());
                orderItems.put(object);
            }
        }catch (JSONException e) {
            Log.v("Saf", e.toString());
        }
        return orderItems.toString();
    }

    public class PlaceOrder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SurveyMedi.this);
            pDialog.setTitle("Sending Feedback");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            r8.setVisibility(View.GONE);
            r9.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String jsonResponse = null;
            url = "https://medicento-api.herokuapp.com/product/feedback";
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
}
