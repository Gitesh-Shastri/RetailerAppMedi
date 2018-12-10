package com.medicento.retailerappmedi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.retailerappmedi.data.Manufacturer;
import com.medicento.retailerappmedi.data.Medicine;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Filter extends AppCompatActivity {

    Button offer,comapany,done;
    SharedPreferences sharedPreferences;
    ArrayList<String> medicine1;
    ListView listview;
    public static ArrayList<Medicine> MedicineDataList,medicines;
    TextView result;
    public static String result1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        offer = findViewById(R.id.offers);
        done = findViewById(R.id.apply);
        result1 = " ";
        listview = findViewById(R.id.medicinelistview);
        result = findViewById(R.id.result);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved", null);
        Type type = new TypeToken<ArrayList<Medicine>>() {}.getType();
        MedicineDataList = gson.fromJson(json, type);
        medicines = new ArrayList<>();
        comapany = findViewById(R.id.company);
        comapany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filter.this, Company.class);
                startActivityForResult(intent, 2);
            }
        });
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result1 = "offer";
                medicines = MedicineDataList;
                result.setText(MedicineDataList.size() + " Results ");
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("list", medicines);
                resultIntent.putExtra("result", result1);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> medicinesList = new ArrayList<>();
        if(requestCode == 2) {
            if(resultCode == RESULT_OK){
                medicines = new ArrayList<>();
                ArrayList<Manufacturer> manu = (ArrayList<Manufacturer>)data.getSerializableExtra("result");
                for (Manufacturer man: manu){
                    for(Medicine med: MedicineDataList) {
                        if(man.getName().equals(med.getCompanyName())){
                            medicines.add(med);
                            medicinesList.add(med.getMedicentoName());
                        }
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Filter.this, android.R.layout.simple_list_item_1, medicinesList);
                listview.setAdapter(arrayAdapter);
                result.setText(medicines.size()+ " Results ");
                result1 = "company";
            }
        }
    }
}
