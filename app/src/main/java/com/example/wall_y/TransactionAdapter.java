package com.example.wall_y;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;

public class TransactionAdapter extends BaseAdapter {
    Context context;
    private Date date[];
    private int amount[];
    private String text[];
    LayoutInflater inflater;

    public TransactionAdapter(Context context, Date[] date, int[] amount, String[] text) {
        this.context = context;
        this.date = date;
        this.amount = amount;
        this.text = text;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
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

        dateView.setText(date[i].toString());
        textView.setText(text[i]);
        amountView.setText(amount[i]);

        return view;
    }
}
