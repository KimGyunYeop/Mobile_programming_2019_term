package com.example.udong_mp2019;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity implements CalendarView {

    private static final String DATE_TEMPLATE = "yyyy-MM-dd";
    private static final String MONTH_TEMPLATE = "MMMM yyyy";
    private Animation fab_click, fab_close;
    private ListView lv_schedule;
    private Date date;
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TEMPLATE);

    private final CalendarPresenter presenter = new CalendarPresenter(this);
    private ArrayList<ScheduleInfoForDB> arrayData = new ArrayList<>();
    private CustomAdapterSchedule aad_schedule;

    @BindView(R.id.LV_schedule)
    ListView listView;

    @BindView(R.id.fab)
    FloatingActionButton fab_calendar;

    @BindView(R.id.calendar_view)
    io.blackbox_vision.materialcalendarview.view.CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        fab_click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_click);

        presenter.addCalendarView();
        presenter.addTextView();
        aad_schedule = new CustomAdapterSchedule(getApplicationContext(), arrayData,"a");
        listView.setAdapter(aad_schedule);

        fab_calendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                anim();
                Intent intent = new Intent(getApplicationContext(), com.example.udong_mp2019.ScheduleRegisterFormActivity.class);
                intent.putExtra("date",sdf.format(date));
                intent.putExtra("circleName","하눌");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirebaseDatabase(sdf.format(date));
    }

    @Override
    public void prepareTextView() {
        date = new Date(System.currentTimeMillis());
    }
    @Override
    public void prepareCalendarView() {
        Calendar disabledCal = Calendar.getInstance();
        disabledCal.set(Calendar.DATE, disabledCal.get(Calendar.DATE) - 1);

        calendarView.setFirstDayOfWeek(Calendar.SUNDAY)
                .setOnDateClickListener(this::onDateClick)
                .setOnMonthChangeListener(this::onMonthChange)
                .setOnDateLongClickListener(this::onDateLongClick)
                .setOnMonthTitleClickListener(this::onMonthTitleClick);

        if (calendarView.isMultiSelectDayEnabled()) {
            calendarView.setOnMultipleDaySelectedListener((month, dates) -> {
                //Do something with your current selection
            });
        }

        calendarView.update(Calendar.getInstance(Locale.getDefault()));
    }

    private void onDateLongClick(@NonNull final Date date) {
        this.date = date;
        getFirebaseDatabase(sdf.format(date));
    }

    private void onDateClick(@NonNull final Date date) {
        Log.d("date",date.toString());
        this.date = date;
        getFirebaseDatabase(sdf.format(date));
        //여기에 일정 출력 넣기
    }

    private void onMonthTitleClick(@NonNull final Date date) {
        //Do something after month selection
    }

    private void onMonthChange(@NonNull final Date date) {
        final ActionBar actionBar = getSupportActionBar();

        if (null != actionBar) {
            String dateStr = formatDate(MONTH_TEMPLATE, date);
            dateStr = dateStr.substring(0, 1).toUpperCase() + dateStr.substring(1, dateStr.length());

            actionBar.setTitle(dateStr);
        }
    }

    private String formatDate(@NonNull String dateTemplate, @NonNull Date date) {
        return new SimpleDateFormat(dateTemplate, Locale.getDefault()).format(date);
    }

    private void anim(){
            fab_calendar.startAnimation(fab_click);
    }

    public void getFirebaseDatabase(String date){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayData.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    ScheduleInfoForDB get = postSnapshot.child("info").getValue(ScheduleInfoForDB.class);
                    Log.d("date",get.toString());
                    arrayData.add(get);
                }
                Log.d("date",arrayData.toString());
                Collections.sort(arrayData);
                aad_schedule.reset(getApplicationContext(),arrayData);
                aad_schedule.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        Query qurey = FirebaseDatabase.getInstance().getReference().child("circle/"+"가천대학교:하눌신폭"+"/schedule/plan/"+date).orderByValue();
        Log.d("date","circle/"+"가천대학교:하눌신폭"+"/schedule/plan/"+date);
        qurey.addListenerForSingleValueEvent(postListener);
    }
}
