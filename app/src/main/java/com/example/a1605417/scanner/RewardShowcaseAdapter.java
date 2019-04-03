package com.example.a1605417.scanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class RewardShowcaseAdapter extends RecyclerView.Adapter<RewardShowcaseAdapter.MyViewHolder> {

     private List<RedeemData> subcateList;
     public Context mContext;
     public Context viewholderContext;
     static int points=0;
     static String gift="";
      public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        CheckBox  itemCheckBox;
        public MyViewHolder(View view) {
            super(view);
            viewholderContext=view.getContext();
            itemCheckBox=view.findViewById(R.id.itemcheckbox);
            itemImageView=view.findViewById(R.id.rewardimageView);
            itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!b)
                    {
                        gift=gift.replaceAll(itemCheckBox.getText().toString().trim()+"/","");

                       if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 10"))
                            points=points-50;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("KeyChain"))
                            points=points-50;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 20"))
                            points=points-100;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Pen"))
                            points=points-100;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 30"))
                            points=points-250;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 40"))
                            points=points-500;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 50"))
                            points=points-750;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Cap"))
                            points=points-750;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Mobile Cover"))
                            points=points-1000;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Wallet"))
                            points=points-2000;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Tshirt"))
                           points=points-3000;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Movie Ticket"))
                           points=points-5000;
                       else if(itemCheckBox.getText().toString().equalsIgnoreCase("Lunch @BBQ Nation"))
                           points=points-10000;

                      }
                    else
                    {
                         gift=gift+itemCheckBox.getText().toString().trim()+"/";
                                               if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 10"))
                            points=points+50;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("KeyChain"))
                            points=points+50;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 20"))
                            points=points+100;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Pen"))
                            points=points+100;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 30"))
                            points=points+250;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 40"))
                            points=points+500;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Rs 50"))
                            points=points+750;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Cap"))
                            points=points+750;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Mobile Cover"))
                            points=points+1000;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Wallet"))
                            points=points+2000;
                        else  if(itemCheckBox.getText().toString().equalsIgnoreCase("Tshirt"))
                            points=points+3000;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Movie Ticket"))
                            points=points+5000;
                        else if(itemCheckBox.getText().toString().equalsIgnoreCase("Lunch @BBQ Nation"))
                            points=points+10000;
                    }
                }
            });
        }
    }


    public RewardShowcaseAdapter(Context mContext,List<RedeemData> subcateList) {
        this.subcateList = subcateList;
        this.mContext=mContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rewards_redeem_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RedeemData redeemData = subcateList.get(position);
       if(Integer.parseInt(redeemData.getPoints())<redeemData.getRewardpoints()){
           holder.itemCheckBox.setEnabled(false);}
            holder.itemCheckBox.setText(redeemData.getReward());
            holder.itemImageView.setImageDrawable(redeemData.getRewardPhoto());
            }
    @Override
    public int getItemCount() {
        return subcateList.size();
    }
}