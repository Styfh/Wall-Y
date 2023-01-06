package com.example.wall_y;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CalendarDialog  extends AppCompatDialogFragment {
    private EditText reminderString;
    private CalendarDialogListener listener;
    private int day;
    private int month;
    private int year;

    public CalendarDialog(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_calendar_event, null);

        builder.setView(view)
                .setTitle("New Reminder for " + day + "/" + month + "/" + year)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String reminder = reminderString.getText().toString();
                        listener.applyText(reminder);
                    }
                });

                reminderString = view.findViewById(R.id.addEvent);
                return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CalendarDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "must implement CalendarDialogListener");
        }
    }

    public interface CalendarDialogListener{
        void applyText(String reminder);
    }
}
