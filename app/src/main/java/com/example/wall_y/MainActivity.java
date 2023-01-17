package com.example.wall_y;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String D_TAG = "MAIN";
    private CalendarView calView;
    private ListView dayEventView;
    private EventAdapter dayEventAdapter;
    public String date;
    public int day = 0;
    public int month = 0;
    public int year = 0;

    private ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INITIALIZE LISTVIEW
        dayEventView = (ListView) findViewById(R.id.calendarEvents);

        eventList = new ArrayList<>();

        dayEventAdapter = new EventAdapter(this, eventList);
        dayEventView.setAdapter(dayEventAdapter);

        dayEventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = getLayoutInflater();

                builder.setMessage(R.string.delete_confirmation);

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(D_TAG, Integer.toString(position));

                        Event eventToDelete = dayEventAdapter.getItem(position);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("events").whereEqualTo("date", eventToDelete.getEventDate())
                                        .whereEqualTo("name", eventToDelete.getEventName())
                                                .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if(task.isSuccessful()){
                                                                    for(QueryDocumentSnapshot document : task.getResult()){
                                                                        document.getReference().delete();
                                                                        Toast.makeText(getApplicationContext(), "Event deleted", Toast.LENGTH_LONG).show();
                                                                    }
                                                                } else{
                                                                    Toast.makeText(getApplicationContext(), "Event failed to delete", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // CALENDAR
        calView = (CalendarView) findViewById(R.id.calendarView);


        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //date = i + "/" + (i1+1) + "/" + i2;
                //Log.d(D_TAG, "date: " + date);
                day = i2;
                month = i1;
                year = i;
//                date = day + "/" + month + "/" + year;

                eventList = new ArrayList<>();
                dayEventAdapter = new EventAdapter(getApplicationContext(), eventList);
                dayEventView.setAdapter(dayEventAdapter);

                getEventsInDay();
            }

        });

        // NAVBAR LOGIC

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.navBtnHome);

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

    }

    public void handleAddEventRequest(View view){

        // VALIDATE THAT EVENT NAME IS ENTERED BEFORE SHOWING DIALOG

        EditText eventNameField = findViewById(R.id.eventName);
        String eventName = eventNameField.getText().toString();

        if(eventName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter the name of the event first", Toast.LENGTH_LONG).show();
        } else{
            showAddEventDialog(eventName);
        }

    }

    private void pushEvent(@NonNull Event event){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> newEvent = new HashMap<>();
        newEvent.put("userId", event.getUserId());
        newEvent.put("name", event.getEventName());
        newEvent.put("date", event.getEventDate());
        newEvent.put("isDeduct", event.isDeduct());
        newEvent.put("amount", event.getAmount());
        newEvent.put("repeat", event.getRepeat());

        db.collection("events").document()
                .set(newEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Event added successfully", Toast.LENGTH_LONG).show();
                        Log.d(D_TAG, event.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add event", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void showAddEventDialog(String eventName){

        // DIALOG INITIALIZATION

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_add_event, null);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        // SPINNER INITIALIZATION

        Spinner balanceOption = dialogView.findViewById(R.id.balanceOption);
        Spinner repeatOption = dialogView.findViewById(R.id.repeatEvent);

        ArrayAdapter<CharSequence> balanceOptionAdapter = ArrayAdapter.createFromResource(this,
                R.array.balance_option,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> repeatOptionAdapter = ArrayAdapter.createFromResource(this,
                R.array.repeat_option,
                android.R.layout.simple_spinner_item);

        balanceOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        balanceOption.setAdapter(balanceOptionAdapter);
        repeatOption.setAdapter(repeatOptionAdapter);

        // AMOUNT INITIALIZATION

        EditText amountField = (EditText) dialogView.findViewById(R.id.amount);

        // ADD BTN INITIALIZATION

        ImageButton addButton = dialogView.findViewById(R.id.dialogAddBtn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD EVENT
                String userId = "";
                Date eventDate = new Date();
                String eventName = "";
                boolean isDeduct = false;
                int amount = -1;
                int repeat = -1;

                // getting fields
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                EditText nameField = (EditText) findViewById(R.id.eventName);


                // getting value from fields
                userId = user.getUid();
                eventName = nameField.getText().toString();
                amount = Integer.parseInt(amountField.getText().toString());
                String option = balanceOption.getSelectedItem().toString();
                String frequency = repeatOption.getSelectedItem().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // Processing option and frequency
                if(option.equals("Deduct balance"))
                    isDeduct = true;
                else
                    isDeduct = false;

                if(frequency.equals("one-time"))
                    repeat = 0;
                else if(frequency.equals("weekly"))
                    repeat = 1;
                else if(frequency.equals("monthly"))
                    repeat = 2;
                else if(frequency.equals("annual"))
                    repeat = 3;

                // Add event
                Event event = new Event(userId, new Timestamp(calendar.getTime()), eventName, isDeduct, amount, repeat);
                pushEvent(event);

                alertDialog.dismiss();
            }
        });

        // SHOW DIALOG

        alertDialog.show();

    }

    public void getEventsInDay() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String uid = user.getUid();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Log.d(D_TAG, calendar.getTime().toString());
        Log.d(D_TAG, uid);

        db.collection("events")
                .whereEqualTo("userId", uid)
                .whereEqualTo("date", new Timestamp(calendar.getTime()))
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
                                dayEventAdapter.notifyDataSetChanged();


                            }
                        } else{
                            Log.d(D_TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}