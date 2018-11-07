package com.medicento.retailerappmedi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.SalesArea;
import com.medicento.retailerappmedi.data.SalesPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText mEmailEditTv;
    TextView salesData,call;
    String usercode;
    SalesPerson sp;
    AlertDialog alert;

    Animation mAnimation;
    ImageView mLogo;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mLogo = findViewById(R.id.medicento_logo);
        salesData = findViewById(R.id.salesPerson);
        mEmailEditTv = findViewById(R.id.email_edit_tv);
        Button btn = findViewById(R.id.sign_in_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usercode = mEmailEditTv.getText().toString();
                if (usercode.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Data To Login", Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                ime.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!amIConnect()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                    mAnimation.setDuration(200);
                    mLogo.startAnimation(mAnimation);
                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                    String url = "https://medicento-api.herokuapp.com/user/login?usercode=" + mEmailEditTv.getText().toString();
                    StringRequest str = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    sp = null;
                                    try {
                                        JSONObject spo = new JSONObject(response.toString());
                                        JSONArray spa = spo.getJSONArray("Sales_Person");
                                        JSONObject user = spa.getJSONObject(0);
                                        sp = new SalesPerson(user.getString("Name"),
                                                user.getLong("Total_sales"),
                                                user.getInt("No_of_order"),
                                                user.getInt("Returns"),
                                                user.getLong("Earnings"),
                                                user.getString("_id"),
                                                user.getString("Allocated_Area"),
                                                user.getString("Allocated_Pharma"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Constants.SALE_PERSON_ID, sp.getId());
                                    editor.putString(Constants.SALE_PHAMA_CODE, usercode);
                                    editor.putString(Constants.SALE_PHARMAID, sp.getmAllocatedPharmaId());
                                    editor.putString(Constants.SALE_PHARMA_NAME, sp.getName());
                                    editor.putFloat(Constants.SALE_PHARMA_TOTAL_SALES, sp.getTotalSales());
                                    editor.putInt(Constants.SALE_PHARMA_NO_OF_ORDERS, sp.getNoOfOrder());
                                    editor.putInt(Constants.SALE_PHARMA_RETURNS, sp.getReturn());
                                    editor.putString(Constants.SALE_PHARMA_ALLOCATED_AREA_ID, sp.getAllocatedArea());
                                    editor.apply();
                                    while(sharedPreferences.getString(Constants.SALE_PERSON_ID,"").equals("")) {
                                        Log.i("shared1", "1");
                                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                        editor1.putString(Constants.SALE_PERSON_ID, sp.getId());
                                        editor1.putString(Constants.SALE_PHAMA_CODE, usercode);
                                        editor1.putString(Constants.SALE_PHARMAID, sp.getmAllocatedPharmaId());
                                        editor1.putString(Constants.SALE_PHARMA_NAME, sp.getName());
                                        editor1.putFloat(Constants.SALE_PHARMA_TOTAL_SALES, sp.getTotalSales());
                                        editor1.putInt(Constants.SALE_PHARMA_NO_OF_ORDERS, sp.getNoOfOrder());
                                        editor1.putInt(Constants.SALE_PHARMA_RETURNS, sp.getReturn());
                                        editor1.putString(Constants.SALE_PHARMA_ALLOCATED_AREA_ID, sp.getAllocatedArea());
                                        editor1.apply();
                                    }
                                    Intent intent = new Intent();
                                    if (getParent() == null) {
                                        setResult(Activity.RESULT_OK, intent);
                                    } else {
                                        getParent().setResult(RESULT_OK);
                                    }
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mEmailEditTv.setText("");
                                    Toast.makeText(SignUpActivity.this, "Invalid Usercode ", Toast.LENGTH_SHORT).show();
                                }
                            });
                    queue.add(str);
                }
            }
        });
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(2000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mLogo.startAnimation(mAnimation);
    }
    public void view(View view) {
        call = findViewById(R.id.call);
        call.setVisibility(View.VISIBLE);
    }

    public void Submit(View view) {
        Intent intent = new Intent(SignUpActivity.this, SurveyMedi.class);
        startActivity(intent);
    }

    private boolean amIConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
