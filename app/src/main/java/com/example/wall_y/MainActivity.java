package com.example.wall_y;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    final String D_TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // ADD BTN INITIALIZATION

        ImageButton addButton = dialogView.findViewById(R.id.dialogAddBtn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD EVENT

                alertDialog.dismiss();
            }
        });

        // SHOW DIALOG

        alertDialog.show();

    }

}