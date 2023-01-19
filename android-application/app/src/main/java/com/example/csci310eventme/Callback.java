package com.example.csci310eventme;

import java.util.ArrayList;

public interface Callback {

    void onCallback(Event event);
    void onCallback(ArrayList<Event> events);
    void onCallback(User user);
    void onCallback(boolean exists);

    void onCallback(ArrayList<String> userEventList, String nothing);

    //void onCallback(ArrayList<String> userEventList, String nothing);
}
