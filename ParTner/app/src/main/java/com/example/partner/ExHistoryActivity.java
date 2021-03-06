package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExHistoryActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    List<TrainingHistory> trainingHistories = new ArrayList<>();
    List<CallHistory> callHistories = new ArrayList<>();
    List<History> dayHistories = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ExHistoryRecyclerAdapter recyclerAdapter;

    // side bar

    private Toolbar mToolbar;
    private Context context = ExHistoryActivity.this;
    private ViewGroup mainLayout;
    private ViewGroup viewLayout;
    private ViewGroup sideLayout;
    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    private String id;

    private ImageButton menu_btn;

    int[] dotColors = {
            Color.rgb(255, 0, 0),
            Color.rgb(248, 176, 58)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_history);

        // side bar

        mToolbar = (Toolbar) findViewById(R.id.menu_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mainLayout = (ViewGroup) findViewById(R.id.id_ex_history);
        viewLayout = (ViewGroup) findViewById(R.id.user_fl_slide);
        sideLayout = (ViewGroup) findViewById(R.id.user_view_slidebar);

        menu_btn = (ImageButton) findViewById(R.id.toolbar_menu_btn);
        menu_btn.setOnClickListener(view -> showMenu());

        addSideBar();


        calendarView = (MaterialCalendarView) findViewById(R.id.calender);

        calendarView.setDynamicHeightEnabled(true);
//        calendarView.setTopbarVisible(false);

        // 임의 데이터 추가
        // --------------------------------------
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        Date start = new Date();
        Date end = new Date();
        try {
            start = timeFormat.parse("2019-05-17 15:27");
            end = timeFormat.parse("2019-05-17 15:47");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        callHistories.add(new CallHistory("트레이너2", "운동인1", start, end, 20));

        try {
            start = timeFormat.parse("2019-05-17 13:27");
            end = timeFormat.parse("2019-05-17 13:41");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        callHistories.add(new CallHistory("트레이너1", "운동인1", start, end, 20));

        // --------------------------------------

        // 현재 날짜 적용
        String today = dateFormat.format(new Date());
        String year = today.split("-")[0];
        String month = today.split("-")[1];

        Log.d("ExHistoryActivityyy", "<< "+month+"월 >>");
        id = SharedPreferenceData.getId(this);
        Log.d("ExHistoryActivityyy", "id: "+id);
        searchTraining(id,year,month);
        searchCall(id,year,month);

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
                String year = Integer.toString(date.getYear());
                String month = Integer.toString(date.getMonth() + 1);
                Log.d("ExHistoryActivityyy", "<< " + month + "월 >>");
                searchTraining(id, year, month);
                searchCall(id, year, month);
                if (recyclerAdapter != null) {
                    recyclerAdapter.clear();
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }
        });


    }

    private void searchTraining(String id, String year, String month) {
        if (trainingHistories.size() != 0)
            trainingHistories.clear();
        RetrofitCommnunication retrofitCommnunication = new ServerComm().init();
        Call<List<TrainingHistory>> trainingHist = retrofitCommnunication.getTrainingHist(id, year, month);
        trainingHist.enqueue(new Callback<List<TrainingHistory>>() {
            @Override
            public void onResponse(Call<List<TrainingHistory>> call, Response<List<TrainingHistory>> response) {
                trainingHistories = response.body();
                Log.d("ExHistoryActivityyy", "trainingHist size onResponse: " + trainingHistories.size());
                setDots();
            }

            @Override
            public void onFailure(Call<List<TrainingHistory>> call, Throwable t) {
                Toast.makeText(ExHistoryActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    private void searchCall(String id, String year, String month) {
        // response 결과로
        if (trainingHistories.size() != 0)
            trainingHistories.clear();
        ServerComm.init().getCallHistory(id, SharedPreferenceData.getType(context)).enqueue(new Callback<List<CallHistory>>() {
            @Override
            public void onResponse(Call<List<CallHistory>> call, Response<List<CallHistory>> response) {
                callHistories = response.body();
                setDots();
            }

            @Override
            public void onFailure(Call<List<CallHistory>> call, Throwable t) {
                Toast.makeText(ExHistoryActivity.this, "정보받아오기 실패", Toast.LENGTH_LONG).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setDots() {
        // 각 날짜별로 dot 찍기
        Collection<CalendarDay> trainingDates = new ArrayList<>();
        Collection<CalendarDay> callDates = new ArrayList<>();
        Collection<CalendarDay> bothDates = new ArrayList<>();

        Log.d("ExHistoryActivityyy", "trainingHist size: " + trainingHistories.size());
        Log.d("ExHistoryActivityyy", "callHist size: " + callHistories.size());

        for (int i = 0; i < trainingHistories.size(); i++) {
            CalendarDay day = CalendarDay.from(trainingHistories.get(i).getDate());
            Log.d("ExHistoryActivityyy", "trainingHist: " + day.toString());
            if (!trainingDates.contains(day)) {
                trainingDates.add(day);
            }
        }
        for (int i = 0; i < callHistories.size(); i++) {
            CalendarDay day = CalendarDay.from(callHistories.get(i).getDate());
            Log.d("ExHistoryActivityyy", "callHist: " + day.toString());
            if (!callDates.contains(day) && !trainingDates.contains(day) && !bothDates.contains(day)) {
                callDates.add(day);
            } else if (trainingDates.contains(day)) {
                trainingDates.remove(day);
                bothDates.add(day);
            }
        }
        // decorate 추가
        calendarView.removeDecorators();
        calendarView.addDecorator(new EventDecorator(trainingDates, dotColors, 1));
        calendarView.addDecorator(new EventDecorator(callDates, dotColors, 2));
        calendarView.addDecorator(new EventDecorator(bothDates, dotColors, 3));
    }

    public ArrayList<History> findDailyHistory(Date date) {
        ArrayList<History> histories = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String thisDate = df.format(date);

        for (TrainingHistory hist : trainingHistories) {
            String histDate = df.format(hist.getDate());
            if (histDate.equals(thisDate)) {
                histories.add(new History(hist.getEx_type(), hist.getDate(), hist.getStart_time(), hist.getEx_count(), hist.getEx_difficulty(), hist.getEx_accuracy()));
            }
        }
        for (CallHistory hist : callHistories) {
            String histDate = df.format(hist.getDate());
            if (histDate.equals(thisDate)) {
                histories.add(new History(0, hist.getDate(), hist.getStart_time(), hist.getTrainer_id(), (int)hist.getCall_duration()));
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
            switch (type) {
                case 1:
                    view.addSpan(new DotSpan(5, colors[0]));
                    break;
                case 2:
                    view.addSpan(new DotSpan(5, colors[1]));
                    break;
                case 3:
                    view.addSpan((new CustmMultipleDotSpan(5, colors)));
                    break;
            }
        }

    }

    public class History {

        private String date, time, name;
        private int count;
        private int type;

        private int diff;
        private int ac;

        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        private SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm");

        public History(int type, Date date, Date start_time, String name, int count) {
            this.type = type;
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.date = dateFormat.format(date);
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.time = timeFormat.format(start_time);
            this.name = name;
            this.count = count;
        }

        public History(int type, Date date, Date start_time, int count, int diff, int ac) {
            this.type = type;
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.date = dateFormat.format(date);
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.time = timeFormat.format(start_time);
            this.count = count;
            this.diff = diff;
            this.ac = ac;
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

        public int getDiff() {
            return diff;
        }

        public void setDiff(int diff) {
            this.diff = diff;
        }

        public int getAc() {
            return ac;
        }

        public void setAc(int ac) {
            this.ac = ac;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isMenuShow) {
            closeMenu();
        } else {

            if (isExitFlag) {
                if (!SharedPreferenceData.getAutologinChecked(this)) {
                    SharedPreferenceData.clearUserData(this);
                }
                finish();
            } else {
                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> isExitFlag = false, 2000);
            }
        }

    }

    private void addSideBar() {

        UserSideBarView sidebar = new UserSideBarView(context);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(v -> {
            closeMenu();
        });


        sidebar.setEventListener(new UserSideBarView.EventListener() {

            @Override
            public void btnCancel() {
                closeMenu();
            }

            @Override
            public void btnHome() {
                isMenuShow = false;
                Intent intent = new Intent(context, UserMainMenuActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnTraining() {
                isMenuShow = false;
                Intent intent = new Intent(context, ExHistoryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnCall() {
                isMenuShow = false;
                Intent intent = new Intent(context, UserCallHistoryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnBookmark() {
                isMenuShow = false;
                Intent intent = new Intent(context, UserBookmarkActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnLogout() {

                SharedPreferenceData.clearUserData(context);
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                isMenuShow = false;
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void btnSetting() {
                Toast.makeText(context, "설정버튼", Toast.LENGTH_LONG).show();
                closeMenu();
            }
        });
    }

    public void closeMenu() {
        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        sideLayout.setVisibility(View.GONE);
        new Handler().postDelayed(() -> {
            viewLayout.setVisibility(View.GONE);
            viewLayout.setEnabled(false);
            mainLayout.setEnabled(true);
            viewLayout.setOnTouchListener((v, event) -> false);
        }, 450);
    }

    public void showMenu() {
        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.setVisibility(View.VISIBLE);
        sideLayout.setClickable(true);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
        mainLayout.setClickable(false);
    }

}
