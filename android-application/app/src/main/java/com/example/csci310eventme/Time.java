package com.example.csci310eventme;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable {
    private int hour;
    private int minute;
    private String half;

    public Time()
    {

    }

    public Time(String ha, int hr, int min) {
        hour = hr;
        half = ha;
        minute = min;
    }

    protected Time(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
        half = in.readString();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getHalf() {
        return half;
    }

    public void setHour(int hr) {
        hour = hr;
    }

    public void setMinute(int min) {
        minute = min;
    }

    public void setHalf(String ha) {
        half = ha;
    }

    public String addZeroToMinute(int min) {
        if (min == 0) {
            return "00";
        } else {
            return String.valueOf(min);
        }
    }

    public String addZeroToHour(int hr){
        if(0 <= hr && hr <=9){
            return ("0" + String.valueOf(hr));
        }
        else{
            return String.valueOf(hr);
        }
    }

    public String toString() {
        return String.valueOf(hour) + ":" + addZeroToMinute(minute) + " " + half;
    }

    public String toStringHour(){
        return addZeroToHour(reduceHours(hour)) + ":" + addZeroToMinute(minute) + " " + half;

    }

    public String toStringMin(){
        return addZeroToHour(reduceHours(hour)) + ":" + addZeroToMinute(reduceMin(minute)) + " " + half;
    }

    public int reduceHours(int hr){
        int temp = hr;
        while(temp >=24){
            temp -= 24;
        }
        return temp;
    }

    public int reduceMin(int min){
        int temp = min;
        while(temp >=60){
            temp -= 60;
        }
        return temp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeString(half);
    }
/*
    // comparing whether current time is later than time2
    public boolean compareTo(Time time2) {
        int hour2 = time2.getHour();
        int min2 = time2.getMinute();
        String half2 = time2.getHalf();
        int compHalves = compareHalves(half, half2);
        if (compHalves == -1) return false;
        if (compHalves == 1) return true;

    }

    // return 1 if half1 is later than half2, return -1 if half1 is earlier than half2
    public int compareHalves(String half1, String half2) {
        if (half1 == half2) {
            return 0;
        } else if (half1 == "PM" && half2 == "AM") {
            return 1;
        } else {
            return -1;
        }
    }
*/


}
