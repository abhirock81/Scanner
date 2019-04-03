package com.example.a1605417.scanner;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.MyViewHolder> {

    private List<MyData> myDataList;
    public Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView reviewTextView,nameTextView;
        RatingBar ratingBar;
        ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
           ratingBar=view.findViewById(R.id.ratingBar);
           reviewTextView=view.findViewById(R.id.review1);
           imageView=view.findViewById(R.id.iconimageview);
           nameTextView=view.findViewById(R.id.nametextview);
        }
    }


    public ProductReviewAdapter(Context mContext,List<MyData> myDataList) {
        this.myDataList=myDataList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_reviewlist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyData myData=myDataList.get(position);
        if(myData.getRating().equalsIgnoreCase("Bad"))
            holder.ratingBar.setRating(1);
        else if(myData.getRating().equalsIgnoreCase("Good"))
            holder.ratingBar.setRating(2);
        else if(myData.getRating().equalsIgnoreCase("Very Good"))
            holder.ratingBar.setRating(3);
        else if(myData.getRating().equalsIgnoreCase("Good"))
            holder.ratingBar.setRating(4);
        else if(myData.getRating().equalsIgnoreCase("Excellent"))
            holder.ratingBar.setRating(5);
        holder.reviewTextView.setText(myData.getEnteredReview());
        holder.nameTextView.setText(myData.getPersonName());
        Glide.with(mContext).load(myData.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }
}