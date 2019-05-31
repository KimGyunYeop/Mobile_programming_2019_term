package com.example.udong_mp2019.circle.Finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.udong_mp2019.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapterReceipt extends BaseAdapter {
    final Context context;
    LayoutInflater inflter;
    ArrayList<String> due, amount, name;

    public CustomAdapterReceipt(Context applicationContext, ArrayList<String> due , ArrayList<String> amount, ArrayList<String> name) {
        this.context = applicationContext;
        this.due = due;
        this.amount = amount;
        this.name = name;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(Context applicationContext, ArrayList<String> due , ArrayList<String> amount, ArrayList<String> name) {
        this.due = due;
        this.amount = amount;
        this.name = name;
    }

    @Override
    public int getCount() {
        return due.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listview_receipt, null);
        TextView tv_due = view.findViewById(R.id.TV_listVIewReceiptDate);
        TextView tv_amount = view.findViewById(R.id.TV_listVIewReceiptAmount);
        TextView tv_name = view.findViewById(R.id.TV_listVIewReceiptPlanName);

        tv_due.setText(due.get(i));
        tv_amount.setText(amount.get(i));
        tv_name.setText(name.get(i));
        return view;
    }
}