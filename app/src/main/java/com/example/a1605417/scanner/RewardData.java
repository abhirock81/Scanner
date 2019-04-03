package com.example.a1605417.scanner;

public class RewardData {
    String crown;
    String level;
    String points;
    String reward;
    String currentPos;
    public  RewardData(){}
    public  RewardData(String crown,String level,String points,String reward,String currentPos){
        this.reward=reward;
        this.points=points;
        this.level=level;
        this.crown=crown;
        this.currentPos=currentPos;
    }
    public String getCrown() {
        return crown;
    }
    public void setCrown(String crown){
        this.crown=crown;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level){
        this.level=level;
    }
    public String getPoints() {
        return points;
    }
    public void setPoints(String points){
        this.points=points;
    }
    public String getReward() {
        return reward;
    }
    public void setReward(String reward){
        this.reward=reward;
    }
    public String getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(String currentPos) {
        this.currentPos = currentPos;
    }



}
