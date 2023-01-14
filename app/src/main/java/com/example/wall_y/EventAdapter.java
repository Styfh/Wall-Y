package com.example.wall_y;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Event> eventList;
    LayoutInflater inflater;

    public EventAdapter(Context context, ArrayList<Event> eventList, LayoutInflater inflater) {
        this.context = context;
        this.eventList = eventList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_transaction, null);

        TextView dateView = view.findViewById(R.id.date);
        TextView textView = view.findViewById(R.id.text);
        TextView amountView = view.findViewById(R.id.amount);

        dateView.setText(eventList.get(i).getEventDate().toString());
        textView.setText(eventList.get(i).getEventName());
        amountView.setText(Integer.toString(eventList.get(i).getAmount()));

        if(eventList.get(i).isDeduct()){
            amountView.setTextColor(Color.RED);
        } else{
            amountView.setTextColor(Color.GREEN);
        }

        return view;
    }
}
