package com.example.wall_y;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MonthlyReportActivity extends AppCompatActivity {

    final String D_TAG = "REPORT";
    private Date date[] = {new Date(2022, 10, 17), new Date(2022, 9, 10), new Date(2022, 8, 1)};
    private int amount[] = {100000, 50000, 30000};
    private String text[] = {"Allowance from parents", "Shopping from online shop", "Dinner at the mall"};

    ListView listView;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Spinner sortSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        // SPINNER INITIALIZATION

        monthSpinner = (Spinner) findViewById(R.id.month);
        yearSpinner = (Spinner) findViewById(R.id.year);
        sortSpinner = (Spinner) findViewById(R.id.sortBy);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.months,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.years,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_option,
                android.R.layout.simple_spinner_item);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthAdapter);
        yearSpinner.setAdapter(yearAdapter);
        sortSpinner.setAdapter(sortAdapter);

        // Select current month and year
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        Log.d(D_TAG, now.toString());

        monthSpinner.setSelection(month);
        yearSpinner.setSelection(year - 2023);

        // BOTTOM NAVBAR INITIALIZATION
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

        // LISTVIEW INITIALIZATION
        ArrayList<Event> eventList = new ArrayList<>();

        listView = findViewById(R.id.listTransaction);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), eventList);
        listView.setAdapter(eventAdapter);

        getAllEvents(eventAdapter, eventList);

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

    public void getAllEvents(EventAdapter adapter, ArrayList<Event> eventList){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();

        String uid = user.getUid();

        int month = monthSpinner.getSelectedItemPosition();
        int year = yearSpinner.getSelectedItemPosition() + 2023;

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Timestamp start = new Timestamp(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        Timestamp end = new Timestamp(calendar.getTime());

        db.collection("events")
                .whereEqualTo("userId", uid)
                .whereGreaterThanOrEqualTo("date", start)
                .whereLessThanOrEqualTo("date", end)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(D_TAG, document.getData().toString());

                                String userId = document.getString("userId");
                                String name = document.getString("name");
                                Timestamp timestamp = null;

                                timestamp = document.getTimestamp("date");
                                boolean isDeduct = document.getBoolean("isDeduct");
                                int amount = document.getLong("amount").intValue();
                                int repeat = document.getLong("repeat").intValue();

                                Event event = new Event(userId, timestamp, name, isDeduct, amount, repeat);
                                eventList.add(event);
                                adapter.notifyDataSetChanged();
                            }
                        } else{
                            Log.d(D_TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}