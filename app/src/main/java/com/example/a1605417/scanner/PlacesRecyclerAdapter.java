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


public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.MyViewHolder> {
    private List<MyData> myDataList;
    private List<String> catList=new ArrayList<String>();
    public Context mContext;
   // String category;

    public PlacesRecyclerAdapter(Context context,List<MyData> myDataList){
        this.mContext=context;
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
           MyData myData=myDataList.get(position);
           String subCategory;
           subCategory=myData.getKey();
           holder.mButton.setText(subCategory);
           if(myData.getKey().equalsIgnoreCase("More >>")){
               holder.mButton.setTextSize(14);
           }
           catList.add(myData.getCategory());
           if(myData.getCategory().equalsIgnoreCase("Services")){
               holder.mButton.setBackgroundColor(mContext.getResources().getColor(R.color.neworange));
           }
           else if(myData.getCategory().equalsIgnoreCase("Products")){
               holder.mButton.setBackgroundColor(mContext.getResources().getColor(R.color.blueish));
           }


    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
      Button mButton;

        public MyViewHolder(final View view) {
            super(view);
            mButton=view.findViewById(R.id.placeNameButton);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!mButton.getText().toString().equalsIgnoreCase("More >>")){
                        Intent intent=new Intent(mContext,ProductsSearchActivity.class);
                        intent.putExtra("subCategory",mButton.getText().toString());
                       intent.putExtra("category",catList.get(getAdapterPosition()));
                        intent.putExtra("place",CategoryReviews.location);
                        v.getContext().startActivity(intent);}
                    else{
                    Intent intent=new Intent(mContext,SubCatSearch.class);
                        Toast.makeText(mContext, ""+CategoryReviews.location+catList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        intent.putExtra("category",catList.get(getAdapterPosition()));
                        intent.putExtra("place",CategoryReviews.location);
                        v.getContext().startActivity(intent);}
                }
            });

        }
    }
}
