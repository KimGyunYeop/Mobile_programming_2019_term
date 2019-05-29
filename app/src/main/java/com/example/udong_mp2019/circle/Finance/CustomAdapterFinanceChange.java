package com.example.udong_mp2019.circle.Finance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.udong_mp2019.R;
import com.example.udong_mp2019.circle.Schedule.ScheduleInfoForDB;
import com.example.udong_mp2019.circle.Schedule.CheckAttendanceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapterFinanceChange extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    public static ArrayList<ScheduleInfoForDB> selectedAnswers;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String circleName;

    public CustomAdapterFinanceChange(Context applicationContext, ArrayList<ScheduleInfoForDB> questionsList, String circleName) {
        this.context = applicationContext;
        this.selectedAnswers = questionsList;
        this.circleName = circleName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void reset(Context applicationContext, ArrayList<ScheduleInfoForDB> questionsList) {
        this.context = applicationContext;
        this.selectedAnswers = questionsList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return selectedAnswers.size();
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
        view = inflter.inflate(R.layout.listview_with_radio, null);
        TextView question = (TextView) view.findViewById(R.id.question);
        RadioButton yes = (RadioButton) view.findViewById(R.id.yes);
        RadioButton no = (RadioButton) view.findViewById(R.id.no);
        Button btn_checkAttendance = (Button) view.findViewById(R.id.btn_checkAttendance);
        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ref.child("circle/"+circleName+"/schedule/plan/"+selectedAnswers.get(i).toString()+"/attendance"+uid).setValue(true);
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ref.child("circle/"+circleName+"/schedule/plan/"+selectedAnswers.get(i).toString()+"/attendance"+uid).setValue(false);
                }
            }
        });
        btn_checkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGroup.getContext(), CheckAttendanceActivity.class);
                intent.putExtra("path",selectedAnswers.get(i).toString());
                intent.putExtra("circleName",circleName);
                viewGroup.getContext().startActivity(intent);
            }
        });
        question.setText(selectedAnswers.get(i).toString());
        return view;
    }
}
