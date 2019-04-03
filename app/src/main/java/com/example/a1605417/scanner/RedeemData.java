package com.example.a1605417.scanner;

import android.graphics.drawable.Drawable;

public class RedeemData{
    private String place;
    private String name;
    private String number;
    private String uId;
    private String redeemPoints;
    private String reward;
    private String points;
    private Drawable rewardPhoto;
    private int rewardpoints;
    public RedeemData(){ }
    public RedeemData(String name,String number,String place,String uId,String redeemPoints){
        this.place=place;
        this.name=name;
        this.number=number;
        this.uId=uId;
        this.redeemPoints=redeemPoints;
    }
    public RedeemData(String name,String number,String place,String uId){
        this.place=place;
        this.name=name;
        this.number=number;
        this.uId=uId;
    }
    public RedeemData(String reward,Drawable rewardPhoto,String points,int rewardpoints){
        this.reward=reward;
        this.points=points;
        this.rewardPhoto=rewardPhoto;
        this.rewardpoints=rewardpoints; }
    public int getRewardpoints() {
        return rewardpoints;
    }

    public void setRewardpoints(int rewardpoints) {
        this.rewardpoints = rewardpoints;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }


    public Drawable getRewardPhoto() {
        return rewardPhoto;
    }

    public void setRewardPhoto(Drawable rewardPhoto) {
        this.rewardPhoto = rewardPhoto;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place){
        this.place=place;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number){
        this.number=number;
    }

    public String getuId() {
        return uId;
    }
    public void setuId(String uId){
        this.uId=uId;
    }

    public String getRedeemPoints() {
        return redeemPoints;
    }
    public void setRedeemPoints(String redeemPoints){
        this.redeemPoints=redeemPoints;
    }



}
