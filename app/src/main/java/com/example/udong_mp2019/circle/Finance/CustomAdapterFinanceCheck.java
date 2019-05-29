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

public class CustomAdapterFinanceCheck extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<String> due, amount, name;
    ArrayList<Boolean> check;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String circleName;

    public CustomAdapterFinanceCheck(Context applicationContext, ArrayList<String> due , ArrayList<String> amount, ArrayList<String> name, ArrayList<Boolean> check, String circleName) {
        this.context = applicationContext;
        this.due = due;
        this.amount = amount;
        this.check = check;
        this.name = name;
        this.circleName = circleName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(Context applicationContext, ArrayList<String> due , ArrayList<String> amount, ArrayList<String> name, ArrayList<Boolean> check) {
        this.context = applicationContext;
        this.due = due;
        this.amount = amount;
        this.check = check;
        this.name = name;
        inflter = (LayoutInflater.from(applicationContext));
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
        view = inflter.inflate(R.layout.listview_finance_check, null);
        TextView tv_due = view.findViewById(R.id.TV_listVIewFinanceCheckDeu);
        TextView tv_amount = view.findViewById(R.id.TV_listVIewFinanceCheckAmount);
        TextView tv_result = view.findViewById(R.id.TV_listVIewFinanceCheck);
        TextView tv_name = view.findViewById(R.id.TV_listVIewFinanceCheckPlanName);

        tv_due.setText(due.get(i));
        tv_amount.setText(amount.get(i));
        tv_name.setText(name.get(i));
        if(check.get(i)){
            tv_result.setText("O");
        }else{
            tv_result.setText("X");
        }
        return view;
    }
}
