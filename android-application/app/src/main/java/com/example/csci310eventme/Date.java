package com.example.csci310eventme;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.Comparator;

public class Date implements Parcelable {
    private int day;
    private String month;
    private int year;
    private int monthVal;

    public Date()
    {

    }

    public Date(Parcel in) {
        day = in.readInt();
        month = in.readString();
        year = in.readInt();
        monthVal = in.readInt();

    }

    public Date(int d, int m, int y) {
        day = d;
        monthVal = m;
        year = y;
    }

    public Date(int d, String mon, int y, int m) {
        day = d;
        month = mon;
        monthVal = m;
        year = y;
    }

    public static final Creator<Date> CREATOR = new Creator<Date>() {
        @Override
        public Date createFromParcel(Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };

    public int getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public int getMonthVal() {
        return monthVal;
    }

    public int getYear() {
        return year;
    }

    public void setDay(int d) {
        day = d;
    }

    public void setMonth(String m) {
        month = m;
    }

    public void setYear(int y) {
        year = y;
    }

    public void setMonthVal(int m) {
        monthVal = m;
    }

    public String toString() {
        return month + " " + String.valueOf(day) + ", " + String.valueOf(year);
    }

    public String toStringVal() {
        return monthVal + " " + String.valueOf(day) + ", " + String.valueOf(year);
    }

    public int getMonthVal(String m) {
        if (m.equals("January")) {
            return 1;
        } else if (m.equals("Febuary")) {
            return 2;
        } else if (m.equals("March")) {
            return 3;
        } else if (m.equals("April")) {
            return 4;
        } else if (m.equals("May")) {
            return 5;
        } else if (m.equals("June")) {
            return 6;
        } else if (m.equals("July")) {
            return 7;
        } else if (m.equals("August")) {
            return 8;
        } else if (m.equals("September")) {
            return 9;
        } else if (m.equals("October")) {
            return 10;
        } else if (m.equals("November")) {
            return 11;
        } else if (m.equals("December")) {
            return 12;
        } else {
            return -1;
        }
    }


    public Boolean validDate(Date date1, Date date2) {
        int begMonthVal = date1.getMonthVal();
        int begDay = date1.getDay();
        int begYear = date1.getYear();

        int endMonthVal = date2.getMonthVal();
        int endDay = date2.getDay();
        int endYear = date2.getYear();

        int nowMonthVal = getMonthVal(this.month);
        if(nowMonthVal == -1 || endMonthVal == -1 || begMonthVal == -1) {
            return false;
        }

        if (this.year < begYear || this.year > endYear) {
            return false;
        } else if (this.year == begYear) {
            if (nowMonthVal < begMonthVal) {
                return false;
            } else if (nowMonthVal == begMonthVal) {
                if (this.day < begDay) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        if (this.year == endYear) {
            if (nowMonthVal > endMonthVal) {
                return false;
            } else if (nowMonthVal == endMonthVal) {
                if (this.day > endDay) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(day);
        parcel.writeString(month);
        parcel.writeInt(year);
        parcel.writeInt(monthVal);
    }

}



class SortByDate implements Comparator<Date>
{
    @Override
    public int compare(Date date, Date d) {
        int dMonthVal = d.getMonthVal(d.getMonth());
        int dDay = d.getDay();
        int dYear = d.getYear();

        int dateMonthVal = date.getMonthVal(date.getMonth());
        int dateDay = date.getDay();
        int dateYear = date.getYear();

        if (dateYear < dYear) {
            return -1;
        } else if (dateYear > dYear) {
            return 1;
        } else {
            if (dateMonthVal < dMonthVal) {
                return -1;
            } else if (dateMonthVal > dMonthVal) {
                return 1;
            } else {
                if (dateDay < dDay) {
                    return -1;
                } else if (dateDay == dDay) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    }
}