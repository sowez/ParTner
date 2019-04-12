package com.example.partner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TrainerCallHistoryRecyclerAdapter extends RecyclerView.Adapter<TrainerCallHistoryRecyclerAdapter.MyViewHolder> {

    private ArrayList<CallHistory> listData = new ArrayList<>();

    public TrainerCallHistoryRecyclerAdapter(ArrayList<CallHistory> list) {
        this.listData = list;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(listData.get(position).getUser());
        holder.time.setText(listData.get(position).getCall_duration().toString());
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

        MyViewHolder(View view){
            super(view);
            username = (TextView)view.findViewById(R.id.history_user_name);
            time = (TextView)view.findViewById(R.id.history_time);
        }
    }
}

