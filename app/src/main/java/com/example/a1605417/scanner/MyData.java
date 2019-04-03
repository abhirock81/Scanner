package com.example.a1605417.scanner;

import java.util.Comparator;

public class MyData {
    private float rating1;
    private  int totalreviews;
    private String number;
    private String productId;
    private String productName;
    private String brandName;
    private String personName;
    private String place;
    private String rating;
    private String personEmail;
    private String image;
    private String clubname;
    private String date;
    private String level;
    private String timing;
    private String deviceNo;
    private String deviceNoUsername;
    private String photoUrl;
    private String points;
    private String uId;
    private String enteredReview;
    private String dateOfReview;
    private String productDescription;
    private String subCategory;
    private String category;
    private String flag;
    private String gifts;
    private String redeemMethod;
    private String dateOfJoin;
    private String redeemPoints;
    private int k,l;
    private String key;
    Integer value;

    public ProductAddress getProductAddress() {
        return productAddress;
    }

    public void setProductAddress(ProductAddress productAddress) {
        this.productAddress = productAddress;
    }

    private ProductAddress productAddress=new ProductAddress();

    public MyData(){ }
    public MyData(String productId,String productName,String brandName,String image,String clubname,String date,String timing){
        this.productId=productId;
        this.productName=productName;
        this.brandName=brandName;
        this.image=image;
        this.clubname=clubname;
        this.date=date;
        this.timing=timing;
        }
    public MyData(String subCategory){
        this.subCategory=subCategory;
    }
    public MyData(String personEmail,String personName,String rating,String enteredReview,String dateOfReview,String productId,String productName,int k,String flag,String category,String place,String subCategory){
        this.productId=productId;
        this.flag=flag;
        this.productName=productName;
        this.personEmail=personEmail;
        this.personName=personName;
        this.rating=rating;
        this.enteredReview=enteredReview;
        this.dateOfReview=dateOfReview;
        this.category=category;
        this.place=place;
        this.subCategory=subCategory;
    }

    public MyData(String deviceNo,String deviceNoUsername,String personName,String personEmail,String photoUrl,String points){
        this.deviceNo=deviceNo;
        this.deviceNoUsername=deviceNoUsername;
        this.personName=personName;
        this.personEmail=personEmail;
        this.points=points;
        this.photoUrl=photoUrl;
    }
    public MyData(String productName,String rating,String enteredReview,int k,int l,String productId){
        this.productName=productName;
        this.rating=rating;
        this.enteredReview=enteredReview;
        this.k=k;
        this.l=l;
        this.productId=productId;
        }
    public MyData(String personName,String rating,String enteredReview,int k,int l,String productId,String photoUrl){
        this.personName=personName;
        this.rating=rating;
        this.enteredReview=enteredReview;
        this.k=k;
        this.l=l;
        this.productId=productId;
        this.photoUrl=photoUrl;
    }


    public MyData(String deviceNo, String points) {
        this.deviceNo=deviceNo;
        this.points=points;

    }
    public MyData(String key, Integer value,String category) {
        this.key=key;
        this.value=value;
        this.category=category;

    }

    public MyData(String personName, String personEmail, String photoUrl, String points,String uId) {
        this.personName=personName;
        this.personEmail=personEmail;
        this.photoUrl=photoUrl;
        this.points=points;
          this.uId=uId;
    }
    public MyData(String category,String subCategory,String place,String productName){
        this.category=category;
        this.subCategory=subCategory;
        this.place=place;
        this.productName=productName;
    }

    public MyData(String productName ,String productDescription,String productId,String image,int p,float rating1,int totalreviews ){
        this.productDescription=productDescription;
        this.productId=productId;
        this.productName=productName;
        this.image=image;
        this.totalreviews=totalreviews;
        this.rating1=rating1;}


    @Override
    public boolean equals(Object obj){
        return (this.subCategory.equals(((MyData)obj).subCategory));
        }
    public static Comparator<MyData> lcomparator = new Comparator<MyData>() {
        @Override
        public int compare(MyData jc1, MyData jc2) {
            return (Integer.compare(jc2.getL(), jc1.getL()));
        }
    };
    public static Comparator<MyData> jcomparator = new Comparator<MyData>() {
        @Override
        public int compare(MyData jc1, MyData jc2) {
            return (Integer.compare(jc2.getValue(), jc1.getValue()));
        }
    };

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId){
        this.productId=productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName){
        this.brandName=brandName;
    }

    public String getPersonName() {
        return personName;
    }
    public void setPersonName(String personName){
        this.personName=personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }
    public void setPersonEmail(String personEmail){
        this.personEmail=personEmail;
    }

    public String getRating() {
        return rating;
    }
    public void setRating(String rating){
        this.rating=rating;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image){
        this.image=image;
    }

    public String getClubname() {
        return clubname;
    }
    public void setClubname(String clubname){
        this.clubname=clubname;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

    public String getTiming() {
        return timing;
    }
    public void setTiming(String timing){
        this.timing=timing;
    }
    public String getDeviceNo() {
        return deviceNo;
    }
    public void setDeviceNo(String deviceNo){
        this.deviceNo=deviceNo;
    }

    public String getDeviceNoUsername() {
        return deviceNoUsername;
    }
    public void setDeviceNoUsername(String deviceNoUsername){
        this.deviceNoUsername=deviceNoUsername;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl=photoUrl;
    }


    public String getPoints() {
        return points;
    }
    public void setPoints(String points){
        this.points=points;
    }
    public String getuId() {
        return uId;
    }
    public void setuId(String uId){
        this.uId=uId;
    }

    public String getEnteredReview() {
        return enteredReview;
    }
    public void setEnteredReview(String enteredReview){
        this.enteredReview=enteredReview;
    }


    public String getDateOfReview() {
        return dateOfReview;
    }
    public void setDateOfReview(String dateOfReview){
        this.dateOfReview=dateOfReview;
    }
    public  int getK(){return  k;}
    public  void setK(int k){this.k=k;}

    public String getProductDescription() {
        return productDescription;
    }
    public void setProductDescription(String productDescription){
        this.productDescription=productDescription;
    }
    public String getSubCategory() {
        return subCategory;
    }
    public void setSubCategory(String subCategory){
        this.subCategory=subCategory;
    }
    public int getL() {
        return l;
    }
    public void setL(int l){
        this.l=l;
    }
    public void setFlag(String flag){
        this.flag=flag;
    }
    public String getFlag() {
        return flag;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getGifts() {
        return gifts;
    }

    public void setGifts(String gifts) {
        this.gifts = gifts;
    }
    public String getRedeemMethod() {
        return redeemMethod;
    }

    public void setRedeemMethod(String redeemMethod) {
        this.redeemMethod = redeemMethod;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }



    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }
    public String getRedeemPoints() {
        return redeemPoints;
    }

    public void setRedeemPoints(String redeemPoints) {
        this.redeemPoints = redeemPoints;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public float getRating1() {
        return rating1;
    }

    public void setRating1(float rating1) {
        this.rating1 = rating1;
    }

    public int getTotalreviews() {
        return totalreviews;
    }

    public void setTotalreviews(int totalreviews) {
        this.totalreviews = totalreviews;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


}
