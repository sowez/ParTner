package com.example.partner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrainerCallHistoryRecyclerAdapter extends RecyclerView.Adapter<TrainerCallHistoryRecyclerAdapter.MyViewHolder> {

    private List<CallHistory> listData = new ArrayList<>();

    public TrainerCallHistoryRecyclerAdapter(List<CallHistory> list) {
        this.listData = list;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(listData.get(position).getUser_id());
        holder.time.setText(Long.toString(listData.get(position).getCall_duration()));

        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = dateFormat.format(listData.get(position).getStart_time());
        holder.startTime.setText(date);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.callhistory_view,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView time;
        public TextView startTime;

        MyViewHolder(View view){
            super(view);
            username = (TextView)view.findViewById(R.id.history_user_name);
            time = (TextView)view.findViewById(R.id.history_time);
            startTime = (TextView)view.findViewById(R.id.history_start_date);
        }
    }
}

