package com.medicento.retailerappmedi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.medicento.retailerappmedi.data.SalesPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.medicento.retailerappmedi.data.Constants;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SignUp Activity";
    private final static int RC_SIGN_IN = 2;

    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    EditText mEmailEditTv;

    TextView call, forget;

    RelativeLayout relativeLayout;

    String usercode;

    SalesPerson sp;

    AlertDialog alert;

    ImageView mLogo;

    TextView terms, signUp;

    Snackbar snackbar;

    ProgressBar progressBar;

    Button signIn;

    SharedPreferences sharedPreferences;

    ImageView facebook, google, twitter;

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    private void init() {

        facebook = findViewById(R.id.facebook_login);

        google = findViewById(R.id.google_login);

        twitter = findViewById(R.id.twitter_login);

        forget = findViewById(R.id.forget);

        terms = findViewById(R.id.termsOfService);

        mLogo = findViewById(R.id.medicento_logo);

        mEmailEditTv = findViewById(R.id.user_code_tv);

        signUp = findViewById(R.id.signUp);

        relativeLayout = findViewById(R.id.relative);

        progressBar = findViewById(R.id.progress_sign_up);

        signIn = findViewById(R.id.sign_in_btn);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.termsOfService:

                startActivity(new Intent(SignUpActivity.this, TermsAndCondition.class));
                break;

            case R.id.signUp:

                startActivity(new Intent(SignUpActivity.this, Register.class));
                break;

            case R.id.forget:

                break;

            case R.id.google_login:

                Snackbar.make(relativeLayout, "google", Toast.LENGTH_SHORT).show();
                break;

            case R.id.facebook_login:

                Snackbar.make(relativeLayout, "facebook", Toast.LENGTH_SHORT).show();
                break;

            case R.id.twitter_login:

                Snackbar.make(relativeLayout, "twitter", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sign_in_btn:

                hideSoftKeyboard(v);

                usercode = mEmailEditTv.getText().toString();

                if (usercode.isEmpty()) {

                    Snackbar.make(relativeLayout, "Please Enter Data To Login", Toast.LENGTH_SHORT).show();

                } else if (!amIConnect()) {

                    showAlertDialog();

                } else {

                    progressBar.setVisibility(View.VISIBLE);

                    snackbar = Snackbar.make(relativeLayout, "Please wait signing you in ..", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

                    String url = "https://medicento-api.herokuapp.com/user/login?usercode=" + mEmailEditTv.getText().toString();
                    signUp(url, queue);

                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Paper.init(this);

        init();

        forget.setOnClickListener(this);

        terms.setOnClickListener(this);

        facebook.setOnClickListener(this);

        google.setOnClickListener(this);

        twitter.setOnClickListener(this);

        signUp.setOnClickListener(this);

        signIn.setOnClickListener(this);


    }

    private boolean amIConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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

    }

    private void signUp(String url, RequestQueue queue) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        sp = null;
                        try {

                            JSONObject spo = new JSONObject(response);

                            snackbar.dismiss();

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

                            sp.setUsercode(usercode);

                            saveData(sp);

                            progressBar.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);

                            finish();

                        } catch (JSONException e) {

                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                            snackbar.dismiss();
                            Snackbar.make(relativeLayout, "Some Error Occured Please try again!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mEmailEditTv.setText("");

                        progressBar.setVisibility(View.INVISIBLE);

                        snackbar.dismiss();
                        Snackbar.make(relativeLayout, "Invalid Usercode ", Snackbar.LENGTH_SHORT).show();
                    }
                });
        queue.add(stringRequest);
    }

    private void saveData(SalesPerson salesPerson) {

        Paper.book().write("user", new Gson().toJson(salesPerson));
    }

    private void hideSoftKeyboard(View view) {

        InputMethodManager ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ime.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
