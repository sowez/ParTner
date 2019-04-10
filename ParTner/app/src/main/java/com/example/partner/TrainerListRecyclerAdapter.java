package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrainerListRecyclerAdapter extends RecyclerView.Adapter<TrainerListRecyclerAdapter.MyViewHolder>{

    private ArrayList<TrainerProfile> listData = new ArrayList<>();


    /* 생성자 */
    public  TrainerListRecyclerAdapter(ArrayList<TrainerProfile> list) { this.listData = list; }

    /*
    * 넘겨 받은 데이터를 화면에 출력하는 역할
    * 재활용 되는 뷰가 호출하여 실행되는 메소드
    * viewholder를 전달하고 adapter는 position의 데이터를 결합
    * */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.ratingBar.setRating(listData.get(position).getStar_rate());
        holder.name.setText(listData.get(position).getName());
        holder.profile.setText(listData.get(position).getSelf_introduction());
        holder.sex.setText(listData.get(position).getSex());
//        문제 발생 -- 스크롤하면 운동종류 계속해서 늘어남
//        for(int i=0; i<listData.get(position).getTraining_type().length; i++){
//            holder.training.append(listData.get(position).getTraining_type()[i]+" ");
//        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Toast.makeText(context, position+"", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), PopupTrainerInfoActivity.class);
                intent.putExtra("name",listData.get(position).getName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /*
    * 레이아웃을 만들어서 Holder에 저장
    * viewholder를 생성하고 view를 붙여주는 부분
    * */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainerlist_view, parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public RatingBar ratingBar;
        public TextView name;
        public TextView profile;
        public TextView sex;
        public TextView training;
        public final View mView;

        public MyViewHolder(View view){
            super(view);
            mView = view;
            ratingBar = (RatingBar)view.findViewById(R.id.ratingbar);
            name = (TextView)view.findViewById(R.id.profile_name);
            profile = (TextView)view.findViewById(R.id.profile_text);
            sex = (TextView)view.findViewById(R.id.profile_sex);
            training = (TextView)view.findViewById(R.id.profile_train);
        }
    }
}
