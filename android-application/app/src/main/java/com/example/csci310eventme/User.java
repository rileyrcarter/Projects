package com.example.csci310eventme;

import android.net.Uri;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String birthday;
    private String picture;
    //private HashMap<String, String> registeredEvents;
    private ArrayList<String> registeredEvents;

    public User()
    {

    }

    public User(String pass, String userN, String first, String last, String birth, String pic) {
        password = pass;
        username = userN;
        birthday = birth;
        firstName = first;
        lastName = last;
        picture = pic;
        //registeredEvents = new HashMap<String, String>();
        registeredEvents = new ArrayList<String>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPicture() {
        return picture;
    }

    //public HashMap<String, String> getRegisteredEvents(){return registeredEvents;}
    //public void setRegisteredEvents(HashMap<String, String> list){registeredEvents = list;}

    public void setUsername(String u) {
        username = u;
    }

    public  void setPassword(String p) {
        password = p;
    }

    public void setBirthday(String b) {
        birthday = b;
    }

    public void setFirstName(String f) {
        firstName = f;
    }

    public void setLastName(String l) {
        lastName = l;
    }

    public void setPicture(String p) {
        picture = p;
    }


    public String getBirthdayFormat(String b) {
        if (b.length() != 8) return b;
        String birth = "";
        String monthNum = b.substring(0,2);
        // parse month
        if (monthNum.equals("01")) {
            birth = "January";
        } else if (monthNum.equals("02")) {
            birth = "February";
        } else if (monthNum.equals("03")) {
            birth = "March";
        } else if (monthNum.equals("04")) {
            birth = "April";
        } else if (monthNum.equals("05")) {
            birth = "May";
        } else if (monthNum.equals("06")) {
            birth = "June";
        } else if (monthNum.equals("07")) {
            birth = "July";
        } else if (monthNum.equals("08")) {
            birth = "August";
        } else if (monthNum.equals("09")) {
            birth = "September";
        } else if (monthNum.equals("10")) {
            birth = "October";
        } else if (monthNum.equals("11")) {
            birth = "November";
        } else if (monthNum.equals("12")) {
            birth = "December";
        } else {
            return b;
        }

        // parse day
        if (b.substring(2,3).equals("0")) {
            birth = birth + " " + b.substring(3,4) + ", ";
        } else {
            birth = birth + " " + b.substring(2,4) + ", ";
        }

        // parse year
        birth = birth + b.substring(4);
        System.out.println("line 109 birth: " + birth);
        return birth;
    }

    // puts birthday in correct format
//    public LocalDate getBirthFormat() {
//        String month = birthday.substring(0, 2);
//        String day = birthday.substring(2, 4);
//        String year = birthday.substring(4);
//        String input = year + "-" + month + "-" + day;
//        LocalDate dob = LocalDate.parse(input);
//        return dob;
//    }
//
//
//
//    // get age of user
//    public int getAge() {
//        LocalDate currDate = LocalDate.now();
//        LocalDate dob = getBirthFormat();
//        if ((dob != null) && (currDate != null)) {
//            return Period.between(dob, currDate).getYears();
//        } else {
//            return 0;
//        }
//    }


}