package com.medicento.retailerappmedi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.retailerappmedi.data.Constants;
import com.medicento.retailerappmedi.data.MakeYourOwn;
import com.medicento.retailerappmedi.data.MakeYourOwnAdapter;
import com.medicento.retailerappmedi.data.Medicine;
import com.medicento.retailerappmedi.data.OrderedMedicine;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Medicine_List extends AppCompatActivity {
    AutoCompleteTextView mMedicineList;
    Button submit,continue1;
    RecyclerView make;
    SharedPreferences mSharedPref;
    public static ArrayList<Medicine> MedicineDataList;
    ArrayList<String> medicine1;
    public static ArrayList<OrderedMedicine> medicines;
    public  static MakeYourOwnAdapter makeYourOwnAdapter;
    ArrayAdapter<String> mMedicineAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine__list);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mMedicineList = (AutoCompleteTextView) findViewById(R.id.medicine_edit_tv3);
        submit = findViewById(R.id.submit);
        continue1 = findViewById(R.id.Continue);
        make = findViewById(R.id.list_make);
        make.setLayoutManager(new LinearLayoutManager(this));
        make.hasFixedSize();
        medicines = (ArrayList<OrderedMedicine>)getIntent().getSerializableExtra("myList");
        makeYourOwnAdapter = new MakeYourOwnAdapter(new ArrayList<MakeYourOwn>());
        make.setAdapter(makeYourOwnAdapter);
        Gson gson = new Gson();
        String json = mSharedPref.getString("saved", null);
        Type type = new TypeToken<ArrayList<Medicine>>() {}.getType();
        MedicineDataList = gson.fromJson(json, type);
        medicine1 = new ArrayList<>();
        for(Medicine med: MedicineDataList){
            medicine1.add(med.getMedicentoName());
        }
        mMedicineAdapter = new ArrayAdapter<String>(Medicine_List.this, R.layout.support_simple_spinner_dropdown_item,medicine1);
        mMedicineList.setAdapter(mMedicineAdapter);
        mMedicineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View view1 = getWindow().getCurrentFocus();
                InputMethodManager ime = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                ime.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                mMedicineList.setText("");
                Medicine medicine = null;
                for(Medicine med: MedicineDataList) {
                    if(med.getMedicentoName().equals(mMedicineAdapter.getItem(position))) {
                        medicine = med;
                        break;
                    }
                }
                Log.i("Manu1", medicine.getMedicentoName());
                makeYourOwnAdapter.add(new MakeYourOwn(medicine.getMedicentoName()));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Medicine_List.this);
                mBuilder.setTitle(mSharedPref.getString(Constants.SALE_PHARMA_NAME, ""));
                mBuilder.setMessage("You have successfully added " + makeYourOwnAdapter.getItemCount() + " Medicines \nTotal Medicines in Your List : " + getIntent().getIntExtra("count", 0));
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("myList", medicines);
                        intent.putExtra("result", "submit");
                        intent.putExtra("make", makeYourOwnAdapter.getmOrderList());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                AlertDialog alertDialog  = mBuilder.create();
                alertDialog.show();
            }
        });
        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("myList", medicines);
                intent.putExtra("result", "continue");
                intent.putExtra("make", makeYourOwnAdapter.getmOrderList());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
