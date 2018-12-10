package com.medicento.retailerappmedi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.medicento.retailerappmedi.data.RecentOrderDelivered;
import com.medicento.retailerappmedi.data.RecentOrderItems;

public class RecentOrderDetails extends AppCompatActivity {

    RecentOrderDelivered recentOrderDelivered;

    RecyclerView recyclerView;

    TextView order_id, grand_total, created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order_details);

        recyclerView = findViewById(R.id.order_rv);

        order_id = findViewById(R.id.order_id);

        grand_total = findViewById(R.id.grand_total);

        created_at = findViewById(R.id.created);

        recentOrderDelivered = (RecentOrderDelivered) getIntent().getSerializableExtra("order");

        order_id.append(recentOrderDelivered.getpOrderId());

        grand_total.append(recentOrderDelivered.getTotal()+"");

        created_at.append(recentOrderDelivered.getpDate().substring(0, 10)+" "+recentOrderDelivered.getpDate().substring(11, 19));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecentOrderItems recentOrderItems = new RecentOrderItems(recentOrderDelivered.getMedicines());
        recyclerView.setAdapter(recentOrderItems);
    }
}
