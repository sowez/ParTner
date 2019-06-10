package com.example.partner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class UserCallHistoryRecyclerAdapter extends RecyclerView.Adapter<UserCallHistoryRecyclerAdapter.MyViewHolder> {

    private List<CallHistory> listData = new ArrayList<>();

    public UserCallHistoryRecyclerAdapter(List<CallHistory> list) {
        this.listData = list;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCallHistoryRecyclerAdapter.MyViewHolder holder, int position) {
        holder.username.setText(listData.get(position).getTrainer_id());
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
    public UserCallHistoryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.callhistory_view,parent,false);
        return new UserCallHistoryRecyclerAdapter.MyViewHolder(view);
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
