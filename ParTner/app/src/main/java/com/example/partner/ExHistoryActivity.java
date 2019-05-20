package com.example.partner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

public class ExHistoryActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    ArrayList<TrainingHistory> trainingHistories = new ArrayList<>();
    ArrayList<CallHistory> callHistories = new ArrayList<>();
    ArrayList<History> dayHistories = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ExHistoryRecyclerAdapter recyclerAdapter;

    int[] dotColors = {
            Color.rgb(255, 255, 0),
            Color.rgb(248, 176, 58)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history);
        calendarView = (MaterialCalendarView)findViewById(R.id.calender);

        calendarView.setDynamicHeightEnabled(true);
//        calendarView.setTopbarVisible(false);

        // 임의 데이터 추가
        // --------------------------------------
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(); Date start = new Date(); Date end = new Date();
        try {
            start = timeFormat.parse("2019-05-16 13:27");
            end = timeFormat.parse("2019-05-16 13:41");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        trainingHistories.add(new TrainingHistory(start, end, 15, 2));

        try {
            start = timeFormat.parse("2019-05-17 15:27");
            end = timeFormat.parse("2019-05-17 15:47");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        callHistories.add(new CallHistory(start, end,20, "트레이너2", "운동인1"));

        try {
            start = timeFormat.parse("2019-05-17 13:27");
            end = timeFormat.parse("2019-05-17 13:41");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        trainingHistories.add(new TrainingHistory(start, end, 60, 1));
        callHistories.add(new CallHistory(start, end,20, "트레이너1", "운동인1"));

        // --------------------------------------


        // 각 날짜별로 dot 찍기
        Collection<CalendarDay> trainingDates = new ArrayList<>();
        Collection<CalendarDay> callDates = new ArrayList<>();
        Collection<CalendarDay> bothDates = new ArrayList<>();

        for (int i=0;i<trainingHistories.size();i++){
            CalendarDay day = CalendarDay.from(trainingHistories.get(i).getDate());
            if (!trainingDates.contains(day)){
                trainingDates.add(day);
            }
        }
        for (int i=0;i<callHistories.size();i++){
            CalendarDay day = CalendarDay.from(callHistories.get(i).getDate());
            if(!callDates.contains(day) && !trainingDates.contains(day) && !bothDates.contains(day)){
                callDates.add(day);
            }
            else if(trainingDates.contains(day)){
                trainingDates.remove(day);
                bothDates.add(day);
            }
        }

        // decorate 추가
        calendarView.addDecorator(new EventDecorator(trainingDates, dotColors,1));
        calendarView.addDecorator(new EventDecorator(callDates, dotColors,2));
        calendarView.addDecorator(new EventDecorator(bothDates, dotColors,3));

        // Recycler View 설정
        recyclerView = (RecyclerView) findViewById(R.id.rv_calendar);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 특정 날짜 선택 시
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dayHistories.clear();
                Date selectedDate = date.getDate();
                dayHistories = findDailyHistory(selectedDate);
                recyclerAdapter = new ExHistoryRecyclerAdapter(dayHistories);
                recyclerView.setAdapter(recyclerAdapter);
            }
        });


        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });


    }

    public ArrayList<History> findDailyHistory(Date date){
        ArrayList<History> histories = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String thisDate = df.format(date);

        for (TrainingHistory hist: trainingHistories){
            String histDate = df.format(hist.getDate());
            if (histDate.equals(thisDate)){
                histories.add(new History(hist.getEx_type(),hist.getDate(), hist.getStart_time(), hist.getEx_count()));
            }
        }
        for (CallHistory hist: callHistories){
            String histDate = df.format(hist.getDate());
            if (histDate.equals(thisDate)){
                histories.add(new History(0,hist.getDate(), hist.getStart_time(), hist.getTrainer(), hist.getCall_duration()));
            }
        }
        Collections.sort(histories, new Comparator<History>() {
            @Override
            public int compare(History h1, History h2) {
                return h1.getTime().compareTo(h2.getTime());
            }
        });
        return histories;
    }

    public class EventDecorator implements DayViewDecorator {

        private final int[] colors;
        private HashSet<CalendarDay> dates;
        private int type;

        // type - 1: only training, 2: only call, 3: both
        EventDecorator(Collection<CalendarDay> dates, int[] colors, int type) {
            this.dates = new HashSet<>(dates);
            this.colors = colors;
            this.type = type;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            switch (type){
                case 1:
                    view.addSpan(new DotSpan(5,colors[0])); break;
                case 2:
                    view.addSpan(new DotSpan(5, colors[1])); break;
                case 3:
                    view.addSpan((new CustmMultipleDotSpan(5,colors))); break;
            }
        }

    }

    public class History {

        private String date, time, name;
        private int count, type;

        public History(int type, Date date, Date start_time, String name, int count){
            this.type = type;
            this.date = new SimpleDateFormat("yyyy/MM/dd").format(date);
            this.time = new SimpleDateFormat("HH : mm").format(start_time);
            this.name = name;
            this.count = count;
        }

        public History(int type, Date date, Date start_time, int count){
            this.type = type;
            this.date = new SimpleDateFormat("yyyy/MM/dd").format(date);
            this.time = new SimpleDateFormat("HH : mm").format(start_time);
            this.count = count;
        }

        public String getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = new SimpleDateFormat("yyyy/MM/dd").format(date);
        }

        public String getTime() {
            return time;
        }

        public void setTime(Date start_time) {
            this.time = new SimpleDateFormat("HH : mm").format(start_time);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

}
