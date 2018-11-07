package com.medicento.retailerappmedi;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.OrderAdapter;
import com.medicento.retailerappmedi.data.PagerAdapter;
import com.medicento.retailerappmedi.data.RecentOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecentOrderActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private static ArrayList<RecentOrder> mRecentOrder;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    SharedPreferences mSharedPreferences;
    private static String url = "https://medicento-api.herokuapp.com/product/recent_order/";
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        url += mSharedPreferences.getString(Constants.SALE_PHARMA_NAME, "");
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout)findViewById(R.id.rtab);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        PendingFragment mPending = new PendingFragment();
        CompletedFragment mCompleted = new CompletedFragment();
        CanceledFragment canceledFragment = new CanceledFragment();
        pagerAdapter.addFragements(mPending, "Pending");
        pagerAdapter.addFragements(mCompleted, "Completed");
        pagerAdapter.addFragements(canceledFragment, "Canceled");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
