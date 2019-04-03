package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private List<MyData> reviewList;
    public Context mContext;
    public Context viewholderContext;
    List<String> mProductId = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        TextView reviewTextView,ratingTextView,productNameTextView;
        public MyViewHolder(View view) {
            super(view);
             viewholderContext=view.getContext();
              imageView=view.findViewById(R.id.editimageview);
              reviewTextView=view.findViewById(R.id.review);
              ratingTextView=view.findViewById(R.id.ratereview);
              productNameTextView=view.findViewById(R.id.productName);
              view.setOnTouchListener(new View.OnTouchListener() {
                  @SuppressLint("ClickableViewAccessibility")
                  @Override
                  public boolean onTouch(View view, MotionEvent motionEvent) {
                     imageView.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view){
                          Intent intent=new Intent(viewholderContext,Review.class);
                          intent.putExtra("edit","edit");
                          intent.putExtra("productId",mProductId.get(getAdapterPosition()));
                          intent.putExtra("enteredreviews",reviewTextView.getText().toString());
                          intent.putExtra("rating",ratingTextView.getText().toString());
                          viewholderContext.startActivity(intent);
                           }
                     });
                      return false;
                  }
              });
        }
    }


    public ReviewAdapter(Context mContext,List<MyData> reviewList) {
        this.reviewList = reviewList;
        this.mContext=mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyData myData = reviewList.get(position);
        holder.reviewTextView.setText(myData.getEnteredReview());
        holder.ratingTextView.setText(myData.getRating());
        holder.productNameTextView.setText(myData.getProductName());
        mProductId.add(myData.getProductId());
        if(myData.getK()==1){holder.imageView.setVisibility(View.VISIBLE);}
            else
                holder.imageView.setVisibility(View.GONE);
        }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}