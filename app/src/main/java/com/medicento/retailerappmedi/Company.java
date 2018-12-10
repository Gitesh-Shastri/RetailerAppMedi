package com.medicento.retailerappmedi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.retailerappmedi.data.DistAdapter;
import com.medicento.retailerappmedi.data.DistributorName;
import com.medicento.retailerappmedi.data.Manufacturer;
import com.medicento.retailerappmedi.data.ManufacturerResAdapter;
import com.medicento.retailerappmedi.data.Medicine;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Company extends AppCompatActivity implements ManufacturerResAdapter.OverallCostChangeListener{

    SharedPreferences mSharedPref;
    public RecyclerView list;
    public static ManufacturerResAdapter manufacturerResAdapter;
    public static ArrayList<Medicine> MedicineDataList;
    ArrayList<String> medicine1;
    public static ArrayList<Manufacturer> manulist;
    Button clear,done;
    TextView result;
    public String manu = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        list = (RecyclerView)findViewById(R.id.list_company);
        manufacturerResAdapter = new ManufacturerResAdapter(new ArrayList<Manufacturer>());
        manufacturerResAdapter.setOverallCostChangeListener(this);
        result = findViewById(R.id.selected);
        result.setText("0 Selected");
        clear = findViewById(R.id.clear);
        done = findViewById(R.id.done);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.hasFixedSize();
        list.setAdapter(manufacturerResAdapter);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mSharedPref.getString("saved", null);
        Type type = new TypeToken<ArrayList<Medicine>>() {}.getType();
        MedicineDataList = gson.fromJson(json, type);
        medicine1 = new ArrayList<>();
        for(Medicine med: MedicineDataList){
            if(manu.equals(med.getCompanyName())) {
                medicine1.add(med.getCompanyName());
            } else {
                manu = med.getCompanyName();
                manufacturerResAdapter.add(new Manufacturer(med.getCompanyName(), false));
            }
        }
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manufacturerResAdapter = new ManufacturerResAdapter(new ArrayList<Manufacturer>());
                manufacturerResAdapter.setOverallCostChangeListener(Company.this);
                list.setLayoutManager(new LinearLayoutManager(Company.this));
                list.hasFixedSize();
                list.setAdapter(manufacturerResAdapter);
                for(Medicine med: MedicineDataList){
                    if(manu.equals(med.getCompanyName())) {
                        medicine1.add(med.getCompanyName());
                    } else {
                        manu = med.getCompanyName();
                        manufacturerResAdapter.add(new Manufacturer(med.getCompanyName(), false));
                    }
                }
                result.setText("0 Selected");
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                ArrayList<Manufacturer> manu1 = new ArrayList<>();
                manulist = manufacturerResAdapter.getmOrderList();
                for(Manufacturer manu: manulist) {
                    if(manu.isSelected() == true) {
                        manu1.add(manu);
                    }
                }
                resultIntent.putExtra("result", manu1);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onCostChanged(int newCost) {
        result.setText(newCost + " Selected ");
    }
}
