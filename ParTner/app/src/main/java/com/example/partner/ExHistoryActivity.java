package com.example.partner;

//import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.example.partner.R;

import java.util.Calendar;

public class ExHistoryActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history);

        calendarView = (MaterialCalendarView)findViewById(R.id.calender);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

//
//        calendarView.setOnDateChangedListener(this);
//        calendarView.setOnMonthChangedListener(this);
//        calendarView.setTopbarVisible(false);
//
//        int beforeYear = Integer.parseInt(getYear(String.valueOf(beforeMonth().getTime.getYear())));
//        int afterYear = Integer.parseInt(getYear(String.valueOf(afterMonth().getTime().getYear())));
//
//        calendarView.setSelectedDate(calendarDayToday);

    }


}
