package com.example.partner;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExHistoryRecyclerAdapter extends RecyclerView.Adapter<ExHistoryRecyclerAdapter.MyViewHolder>{

    private String[] histType = {"call", "플랭크", "스쿼트", "점핑잭"};
    private List<ExHistoryActivity.History> histories = new ArrayList<>();

    public ExHistoryRecyclerAdapter(List<ExHistoryActivity.History> histories){
        this.histories = histories;
    }

    @Override
    public void onBindViewHolder(ExHistoryRecyclerAdapter.MyViewHolder holder, int position) {
        holder.tv_date.setText(histories.get(position).getDate());
        holder.tv_time.setText(histories.get(position).getTime());
        int gettype = histories.get(position).getType();
        if (gettype == 0){
            String text1 = "'"+histories.get(position).getName()+"'님과의 영상통화";
            String text2 = histories.get(position).getCount() + "초";
            holder.tv_name.setText(text1);
            holder.tv_count.setText(text2);
            holder.iv_exIcon.setImageResource(R.drawable.icon_training);
        }
        else{
            String text;
            if (gettype == 1){
                text = histories.get(position).getCount() + "초";
                holder.iv_exIcon.setImageResource(R.drawable.icon_flank);
            }
            else if (gettype == 2){
                text = histories.get(position).getCount() + "회";
                holder.iv_exIcon.setImageResource(R.drawable.icon_squat);
            }
            else {
                text = histories.get(position).getCount() + "회";
                holder.iv_exIcon.setImageResource(R.drawable.icon_jumpingjack);
            }
            holder.tv_name.setText(histType[histories.get(position).getType()]);
            holder.tv_count.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exhistorylist_view,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_date;
        public TextView tv_time;
        public TextView tv_name;
        public TextView tv_count;
        public ImageView iv_exIcon;
        public final View mView;

        public MyViewHolder(View view){
            super(view);
            mView = view;
            tv_date = (TextView) view.findViewById(R.id.tv_exhist_date);
            tv_time = (TextView) view.findViewById(R.id.tv_exhist_time);
            tv_name = (TextView) view.findViewById(R.id.tv_exhist_name);
            tv_count = (TextView) view.findViewById(R.id.tv_exhist_count);
            iv_exIcon = (ImageView) view.findViewById(R.id.iv_exHistoryType);
        }
    }

    public void clear() {
        int size = histories.size();
        histories.clear();
        notifyItemRangeRemoved(0, size);
    }
}
