package com.example.a1605417.scanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchRecyclerAdapter extends RecyclerView.Adapter<ProductSearchRecyclerAdapter.MyViewHolder> {

    private List<MyData> myDataList;
    public Context mContext;
    List<String> idList=new ArrayList<String>();

    public ProductSearchRecyclerAdapter(Context mContext,List<MyData> myDataList)
    {
        this.mContext=mContext;
        this.myDataList=myDataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcat_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyData myData=myDataList.get(position);
        idList.add(myData.getProductId());
        holder.ratingBar.setRating(myData.getRating1());
        holder.ratereviewTextView.setText(""+myData.getRating1());
        holder.totalreviewsTextView.setText(""+myData.getTotalreviews()+" Reviews");
        holder.tv1.setText(myData.getProductName());
        holder.tv2.setText(myData.getProductDescription());
        Glide.with(mContext).load(myData.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView tv1,tv2,totalreviewsTextView,ratereviewTextView;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            totalreviewsTextView=itemView.findViewById(R.id.totalreviews);
            ratereviewTextView=itemView.findViewById(R.id.ratereview);
            imgView=itemView.findViewById(R.id.subCatimg);
            tv1=itemView.findViewById(R.id.productNametv);
            tv2=itemView.findViewById(R.id.productDescriptiontv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,""+idList.size(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(mContext,ProductReviews.class);
                    intent.putExtra("productId",idList.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
