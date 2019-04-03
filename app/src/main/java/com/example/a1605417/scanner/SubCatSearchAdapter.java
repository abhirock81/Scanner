package com.example.a1605417.scanner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SubCatSearchAdapter extends RecyclerView.Adapter<SubCatSearchAdapter.MyViewHolder> {

    private List<MyData> myDataList;
    private List<String> catList=new ArrayList<String>();
    private List<String> places=new ArrayList<String>();
    public Context mContext;
    String category;

    SubCatSearchAdapter(Context mContext,List<MyData> myDataList){
        this.mContext=mContext;
       this.myDataList=myDataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyData myData = myDataList.get(position);
            holder.mButton.setText(myData.getSubCategory());
        catList.add(myData.getCategory());
        if (myData.getCategory().equalsIgnoreCase("Services")) {
            holder.mButton.setBackgroundColor(mContext.getResources().getColor(R.color.lightgreen));
        } else if (myData.getCategory().equalsIgnoreCase("Products")) {
            holder.mButton.setBackgroundColor(mContext.getResources().getColor(R.color.lightpink));
        }

        places.add(myData.getPlace());
        Toast.makeText(mContext, "" + places.size(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button mButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            mButton=itemView.findViewById(R.id.placeNameButton);
           mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ProductsSearchActivity.class);
                    intent.putExtra("subCategory",mButton.getText().toString());
                    intent.putExtra("category",catList.get(getAdapterPosition()));
                    intent.putExtra("place",places.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);

                }
            });
        }
    }
}
