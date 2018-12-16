package com.medicento.retailerappmedi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medicento.retailerappmedi.data.Area;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Spinner areaSpinner;

    ProgressDialog pDialog;

    ArrayList<Area> areas;

    ArrayList<String> area_name;

    TextView create;

    TextView terms;

    EditText c_name, c_phone, pharma_name, email, address, gst, drug, pincode;

    Button submit;

    AlertDialog alert;

    String areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        areaSpinner = findViewById(R.id.area_spinner);

        terms = findViewById(R.id.termsOfService);

        submit = findViewById(R.id.create);

        create = findViewById(R.id.login);

        pharma_name = findViewById(R.id.shop_name);

        pincode = findViewById(R.id.owner_pincode);

        address = findViewById(R.id.shop_address);

        c_name = findViewById(R.id.owner_name);

        c_phone = findViewById(R.id.phone_number);

        email = findViewById(R.id.email);

        drug = findViewById(R.id.drug_license);

        gst = findViewById(R.id.gst_license);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register.this, TermsAndCondition.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = areaSpinner.getSelectedItem().toString();
                for(Area area: areas)  {
                    if(name.equals(area.getName())) {
                        areaId = area.getId();
                    }
                }
                final ProgressDialog progressDialog = new ProgressDialog(Register.this);
                progressDialog.setMessage("Creatin Please wait");
                progressDialog.show();
                RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        "https://retailer-app-api.herokuapp.com/pharma/new",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                    Log.i("email", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setTitle("Your UserCode is : ");
                                    builder.setIcon(R.mipmap.ic_launcher_new);
                                    builder.setCancelable(false);
                                    builder.setMessage(jsonObject.getString("code"))
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    alert.dismiss();
                                                }
                                            });
                                    alert = builder.create();
                                    alert.show();
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Register.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("email", error.getMessage());
                        Toast.makeText(Register.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to login url
                        Map<String, String> params = new HashMap<>();
                        params.put("pharma_name", pharma_name.getText().toString());
                        params.put("area_id", areaId);
                        params.put("pharma_address", address.getText().toString());
                        params.put("phone", c_phone.getText().toString());
                        params.put("gst", gst.getText().toString());
                        params.put("drug", drug.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("name", c_name.getText().toString());
                        params.put("pincode", pincode.getText().toString());

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register.this, SignUpActivity.class));
                finish();
            }
        });

        fetchArea();
    }

    private void fetchArea() {

        areas = new ArrayList<>();

        area_name = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String  url = "https://retailer-app-api.herokuapp.com/area";

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading Area");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("area", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("areas");
                            for(int i=0; i<jsonArray.length();i++) {
                                JSONObject area = jsonArray.getJSONObject(i);
                                areas.add(new Area(
                                        area.getString("area_name"),
                                        area.getString("_id"),
                                        area.getString("area_state"),
                                        area.getString("area_city")
                                ));
                                area_name.add(area.getString("area_name"));
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Register.this, R.layout.support_simple_spinner_dropdown_item, area_name);
                            areaSpinner.setAdapter(arrayAdapter);

                            pDialog.dismiss();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            pDialog.dismiss();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

}
