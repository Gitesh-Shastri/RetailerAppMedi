package com.medicento.retailerappmedi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.retailerappmedi.data.Manufacturer;
import com.medicento.retailerappmedi.data.Medicine;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Filter1 extends AppCompatActivity {

    Button comapany,done;
    SharedPreferences sharedPreferences;
    ArrayList<String> medicine1;
    public static ArrayList<Medicine> MedicineDataList,medicines;
    TextView result;
    public static String result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter1);
        done = findViewById(R.id.apply1);
        comapany = findViewById(R.id.company1);
        result1 = "";
        result = findViewById(R.id.result1);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved", null);
        Type type = new TypeToken<ArrayList<Medicine>>() {}.getType();
        MedicineDataList = gson.fromJson(json, type);
        medicines = new ArrayList<>();
        comapany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filter1.this, Company.class);
                startActivityForResult(intent, 2);
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
        if(requestCode == 2) {
            if(resultCode == RESULT_OK){
                medicines = new ArrayList<>();
                ArrayList<Manufacturer> manu = (ArrayList<Manufacturer>)data.getSerializableExtra("result");
                for (Manufacturer man: manu){
                    for(Medicine med: MedicineDataList) {
                        if(man.getName().equals(med.getCompanyName())){
                            medicines.add(med);
                        }
                    }
                }
                result.setText(medicines.size()+ " Results ");
                result1 = "company";
            }
        }
    }
}
