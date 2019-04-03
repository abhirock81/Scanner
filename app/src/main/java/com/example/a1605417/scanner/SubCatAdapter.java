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

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    private List<MyData> subcateList;
    public Context mContext;
    public Context viewholderContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subcategoryTextView;
        public MyViewHolder(View view) {
            super(view);
            viewholderContext=view.getContext();
            subcategoryTextView=view.findViewById(R.id.subcattext);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,ReviewList.class);
                    intent.putExtra("subcat",subcategoryTextView.getText().toString());
                    viewholderContext.startActivity(intent);
                }
            });
        }
    }


    public SubCatAdapter(Context mContext,List<MyData> subcateList) {
        this.subcateList = subcateList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcatitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyData myData = subcateList.get(position);
        holder.subcategoryTextView.setText(myData.getSubCategory());
    }

    @Override
    public int getItemCount() {
        return subcateList.size();
    }
}