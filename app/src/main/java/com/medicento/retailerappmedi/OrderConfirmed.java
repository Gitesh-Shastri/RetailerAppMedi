package com.medicento.retailerappmedi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.OrderedMedicineAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderConfirmed extends AppCompatActivity {

    TextView mOrderIdTv, pname, mDeliveryDateTv, mTotalCostTv;
    SharedPreferences mShared;
    Button mShareBtn;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderConfirmed.this, PlaceOrderActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);
        mShared = PreferenceManager.getDefaultSharedPreferences(this);
        Intent i = getIntent();
        pname = (TextView)findViewById(R.id.selected_pharmacy_tv);
        mOrderIdTv = (TextView)findViewById(R.id.order_id_tv);
        mTotalCostTv = (TextView)findViewById(R.id.total_cost_tv);
        mDeliveryDateTv = (TextView)findViewById(R.id.delivery_date_tv);
        mShareBtn = findViewById(R.id.share_order_btn);
        pname.setText(mShared.getString(Constants.SALE_PHARMA_NAME, ""));
        mTotalCostTv.setText(i.getStringExtra("TotalCost"));
        String json = i.getStringExtra("json");
        String details[] = extractIdAndDateFromJson(json);
        mOrderIdTv.setText(details[0]);
        mDeliveryDateTv.setText(details[1]);
        mTotalCostTv.setText(details[2]);
        String date = details[1];
        String time = date.substring(0,10);
        final String orderShareDetails = "*Medicento Sales Order Summary*" +
                "\n*Pharmacy Name*: " + mShared.getString(Constants.SALE_PHARMA_NAME, "") +
                "\n*Order id*: " + details[0] +
                "\n*Total Cost*: " + "Rs. " + "*" + mTotalCostTv.getText().toString() + "*" +
                "\n*Delivery schedule*: " + time;
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SUBJECT, "Medicento Order details");
                intent.putExtra(Intent.EXTRA_TEXT, orderShareDetails);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }

    public void new_order(View view) {
        Intent intent = new Intent(OrderConfirmed.this, PlaceOrderActivity.class);
        finish();
        startActivity(intent);
    }

    private static String[] extractIdAndDateFromJson(String jsonResponse) {
        String details[] = new String[3];
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            details[0] = jsonObject.getString("order_id");
            details[1] = jsonObject.getString("delivery_date");
            details[2] = ""+jsonObject.getInt("grand_total");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }
}
