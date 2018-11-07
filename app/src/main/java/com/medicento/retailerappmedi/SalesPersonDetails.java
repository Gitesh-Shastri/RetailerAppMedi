package com.medicento.retailerappmedi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.SalesPerson;


public class SalesPersonDetails extends AppCompatActivity {
    TextView sName, sCode, sId, sTotalsales, sNoOrder, sReturn;
    SalesPerson sp;
    String userCode;
    Button GoBack;
    SharedPreferences mSharedPreferences;
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_person_details);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sName = findViewById(R.id.about_me_name);
        sCode = findViewById(R.id.about_me_code);
        sId =  findViewById(R.id.salesPersonId);
        sTotalsales = findViewById(R.id.salesPersonTS);
        sNoOrder = findViewById(R.id.salesPersonNO);
        sReturn = findViewById(R.id.salesPersonR);
        sName.setText(mSharedPreferences.getString(Constants.SALE_PHARMA_NAME, ""));
        sCode.setText(mSharedPreferences.getString(Constants.SALE_PHAMA_CODE, ""));
        sId.setText(mSharedPreferences.getString(Constants.SALE_PHARMAID, ""));
        sTotalsales.setText(""+mSharedPreferences.getFloat(Constants.SALE_PHARMA_TOTAL_SALES, 0));
        sNoOrder.setText(""+mSharedPreferences.getInt(Constants.SALE_PHARMA_NO_OF_ORDERS, 0));
        sReturn.setText(""+mSharedPreferences.getInt(Constants.SALE_PHARMA_RETURNS, 0));
    }
}

