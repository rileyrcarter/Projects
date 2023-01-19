package com.example.csci310eventme;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    //ATTRIBUTES
    private int eventId;
    private String eventName;
    private float latitude;
    private float longitude;
    private Date date;
    private Time time;
    private int cost;
    private String sponsor;
    private String description;
    private String category;
    private String parking;
    private int numPeople;
    private String location;
    private int ageReq;

    public Event()
    {

    }


    public Event(Parcel in) {
        eventId = in.readInt();
        eventName = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        date = in.readParcelable(Date.class.getClassLoader());
        time = in.readParcelable(Time.class.getClassLoader());
        cost = in.readInt();
        sponsor = in.readString();
        description = in.readString();
        category = in.readString();
        parking = in.readString();
        numPeople = in.readInt();
        location = in.readString();
        ageReq = in.readInt();

    }


    //COPY CONSTRUCTOR
    public Event(Event eventToCopy)
    {
        this.eventId = eventToCopy.eventId;
        this.eventName = eventToCopy.eventName;
        this.latitude = eventToCopy.latitude;
        this.longitude = eventToCopy.longitude;
        this.date = eventToCopy.date;
        this.time = eventToCopy.time;
        this.cost = eventToCopy.cost;
        this.sponsor = eventToCopy.sponsor;
        this.description = eventToCopy.description;
        this.category = eventToCopy.category;
        this.parking = eventToCopy.parking;
        this.numPeople = eventToCopy.numPeople;
        this.location = eventToCopy.location;
        this.ageReq = eventToCopy.ageReq;
    }
    //CONSTRUCTOR
    public Event(int eventId, String eventName, float latitude, float longitude,
                 Date date, Time time, int cost, String sponsor,
                 String description, String category, String parking, int numPeople, String location, int ageReq)
    {
        this.eventId = eventId;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.sponsor = sponsor;
        this.description = description;
        this.category = category;
        this.parking = parking;
        this.numPeople = numPeople;
        this.location = location;
        this.ageReq = ageReq;
    }
    ///BEHAVIORS

    //////////////////////////////////////////////////GETTERS//////////////////////////////////////////////////
    public int getEventId(){
        return eventId;
    }

    public String getEventName(){
        return eventName;
    }
    public float getLatitude()
    {
        return latitude;
    }
    public float getLongitude()
    {
        return longitude;
    }
    public Date getDate(){
       return date;
    }
    public Time getTime(){
        return time;
    }
    public int getCost(){
        return cost;
    }
    public String getSponsor(){
        return sponsor;
    }
    public String getDescription(){
        return description;
    }
    public String getCategory(){
        return category;
    }
    public String getParking(){return parking;}
    public int getNumPeople(){return numPeople;}
    public String getLocation() {return location;}
    public int getAgeReq(){return ageReq;}

    //////////////////////////////////////////////////SETTERS//////////////////////////////////////////////////
    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public void setEventName(String name){
        this.eventName =  name;
    }
    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }
    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void setTime(Time time){
        this.time = time;
    }
    public void setCost(int cost){
        this.cost = cost;
    }
    public void setSponsor(String sponsor){
        this.sponsor = sponsor;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setParking(String parking){this.parking = parking;}
    public void setNumPeople(int numPeople){this.numPeople = numPeople;}
    public void setLocation(String location) {this.location = location;}
    public void setAgeReq(int ageReq){this.ageReq = ageReq;}

    public String setEventWidget() {
        String date = getDate().toString();
        String time = getTime().toString();
        String eventText = this.eventName + "\n" + this.sponsor + "\n" + this.category + "\n" + date + "   " + time + "\n" + "$" + this.cost + "\n" + this.description;
        return eventText;
    }

    public String toString() {

        String date = getDate().toString();
        String time = getTime().toString();
        String eventText = this.eventName + "\n" + this.sponsor + "\n" + this.category + "\n" + date + "   " + time + "\n" + "$" + this.cost;
        return eventText;

    }

    public String setEventBox() {
        String date = getDate().toString();
        String time = getTime().toString();
        String eventText = this.eventName + "\n" + this.sponsor + "\n" + this.category + "\n" + date + "   " + time + "\n" + "$" + this.cost;
        return eventText;
    }

    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(eventId);
        out.writeString(eventName);
        out.writeFloat(latitude);
        out.writeFloat(longitude);
        out.writeParcelable(date, i);
        out.writeParcelable(time, i);
        out.writeInt(cost);
        out.writeString(sponsor);
        out.writeString(description);
        out.writeString(category);
        out.writeString(parking);
        out.writeInt(numPeople);
        out.writeString(location);
        out.writeInt(ageReq);
    }

}

