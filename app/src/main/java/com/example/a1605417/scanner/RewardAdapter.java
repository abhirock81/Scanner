package com.example.a1605417.scanner;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.Objects;

import java.util.ArrayList;
import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder> {

    private List<RewardData> rewardList;
    public Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView levelTextView,rewardTextView,pointsTextView,crownTextView;
        ImageView crownImageView,markerImageView;
        public MyViewHolder(View view) {
            super(view);
            levelTextView=view.findViewById(R.id.leveltextview);
            rewardTextView=view.findViewById(R.id.rewardtextview);
            pointsTextView=view.findViewById(R.id.pointstextview);
            crownImageView=view.findViewById(R.id.crownimageview);
            crownTextView=view.findViewById(R.id.crowntextview);
            markerImageView=view.findViewById(R.id.markerimageView);
            markerImageView.setVisibility(View.GONE);

        }
    }


    public RewardAdapter(Context mContext,List<RewardData> rewardList) {
        this.rewardList=rewardList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rewardlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RewardData rewardData=rewardList.get(position);
        holder.pointsTextView.setText(rewardData.getPoints());
        holder.rewardTextView.setText(rewardData.getReward());
        holder.levelTextView.setText(rewardData.getLevel());
        if(position==0){
            holder.crownImageView.setVisibility(View.INVISIBLE);
            holder.crownTextView.setText("CROWN");
            holder.markerImageView.setVisibility(View.GONE);
        }
        else{
            holder.crownTextView.setVisibility(View.INVISIBLE);
        Glide.with(mContext).load(rewardData.getCrown()).into(holder.crownImageView);}
        if(rewardData.getCurrentPos().equalsIgnoreCase("1")){
            holder.markerImageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }
}