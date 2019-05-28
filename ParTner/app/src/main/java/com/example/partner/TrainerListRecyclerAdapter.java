package com.example.partner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrainerListRecyclerAdapter extends RecyclerView.Adapter<TrainerListRecyclerAdapter.MyViewHolder>{

    private List<TrainerProfile> listData = new ArrayList<>();


    /* 생성자 */
    public  TrainerListRecyclerAdapter(List<TrainerProfile> list) { this.listData = list; }

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
        String train = "";
        for(int i=0; i<listData.get(position).getTraining_type().size(); i++){
            train = train + listData.get(position).getTraining_type().get(i) + " ";
        }
        holder.training.setText(train);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Toast.makeText(context, position+"", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), PopupTrainerInfoActivity.class);
                intent.putExtra("name",listData.get(position).getName());
                intent.putExtra("id",listData.get(position).getId());
                intent.putExtra("star_rate",listData.get(position).getStar_rate().toString());
                intent.putExtra("img",listData.get(position).getProfileImg());
                intent.putExtra("intro",listData.get(position).getSelf_introduction());
                intent.putExtra("traintype", holder.training.getText().toString());
                intent.putExtra("bookmark", listData.get(position).getBookmarked());

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
