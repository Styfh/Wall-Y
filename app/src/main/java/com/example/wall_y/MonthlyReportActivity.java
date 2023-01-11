package com.example.wall_y;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Date;

public class MonthlyReportActivity extends AppCompatActivity {

    final String D_TAG = "REPORT";
    private Date date[] = {new Date(2022, 10, 17), new Date(2022, 9, 10), new Date(2022, 8, 1)};
    private int amount[] = {100000, 50000, 30000};
    private String text[] = {"Allowance from parents", "Shopping from online shop", "Dinner at the mall"};

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setSelectedItemId(R.id.navBtnReport);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.navBtnHome:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navBtnReport:
                        startActivity(new Intent(getApplicationContext(), MonthlyReportActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navBtnProfile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;

            }
        });

        listView = findViewById(R.id.listTransaction);
        TransactionAdapter transactionAdapter = new TransactionAdapter(getApplicationContext(), date, amount, text);
        listView.setAdapter(transactionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(MonthlyReportActivity.this, .class);
//                intent.putExtra("image", foodImage[i]);
//                intent.putExtra("text", foodData[i]);
//                intent.putExtra("price", foodPrice[i]);

//                intent.putExtra("itemsBoughtList", itemsBoughtList);

//                startActivity(intent);
            }
        });

    }
}