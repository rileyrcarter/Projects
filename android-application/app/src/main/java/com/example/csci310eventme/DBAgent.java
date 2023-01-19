package com.example.csci310eventme;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//Todo:
//Add validity checks later (age, valid email, inputs etc)

public class DBAgent {
    FirebaseDatabase root;
    DatabaseReference reference;
    //ATTRIBUTES
    //ArrayList<com.example.csci310eventme.Event> eventList();


///////////////////////////////////////BASIC FEATURES BEHAVIORS//////////////////////////////////////////////////

//LOGIN/ SIGN UP


    //on click sign up, sets new com.example.csci310eventme.User in database
    public void createUser(String pass, String username, String first, String last, String birthday) {
        //get textedit by id for each input
        if (!checkUsernameExists(username)) {
            User newUser = new User(pass, username, first, last, birthday, "");
            //root = FirebaseDatabase.getInstance();
            reference = root.getInstance().getReference();
            reference.child("users").child(username).setValue(newUser);
        } else {

        }


        //1. check if email exists
        // a. if yes = display error msg
        // b. else proceed: create a packaged user and add to firebase DB
        //Instantiate User object
        //Redirect to Home Page

    }

    public void addEvent(String username, String eventID){
        //reference = root.getInstance().getReference();
        //reference.child("users").child(username).child("registeredEvents").setValue(eventID);

        reference = root.getInstance().getReference();
        //String key = reference.child("users").child(username).child("registeredEvents").push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put(eventID, eventID);
        reference.child("users").child(username).child("registeredEvents").updateChildren(map);
        //adding event
    }




//    public boolean checkUser(){
//        root.database().ref(`users/${userId}/email`).once("value", snapshot => {
//            if (snapshot.exists()){
//                console.log("exists!");
//                const email = snapshot.val();
//            }
//        });
//    }

//    public boolean checkUser(Callback callback, String user, String pass){
//        //first check for username
//        //if username matches => check password
//        //if password matches reroute to explore page
//        //Else print error message
//        /////READ USER////////////////////////////////////
//        root = FirebaseDatabase.getInstance();
//        reference = root.getReference("users");
//        //reading user: key is username
//        //String username = currentUser.getUsername();
//        String username = user;
//        reference.child(username).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User value = dataSnapshot.getValue(User.class);
//                callback.onCallback(value);
//
//                if(value.getUsername() == user){
//                    if(value.getPassword() == pass){
//                        //return True;
//
//                    }
//                }
//                Log.d("FirstFragment", "User name: " + value.getUsername());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                //textView.setText("Error in retrieving your message!");
//                Log.w("SecondFragment", "Failed to read value.", error.toException());
//            }
//        });
//    }

    public boolean checkUsernameExists(String username) {


        return false;
    }

    public void loginUser() {}


    //read User from data base
    //NOT USED, FOR TESTING
    public void getUser(Callback callback)
    {
        /////READ USER////////////////////////////////////
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users");
        //reading user: key is username
        //String username = currentUser.getUsername();
        String username = "test";
        reference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                callback.onCallback(value);
                Log.d("FirstFragment", "User name: " + value.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });
    }


    public void getUser(Callback callback, String user)
    {
        /////READ USER////////////////////////////////////
        System.out.println("DBAgent.Java Line 134 user: " + user);
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users");
        //DatabaseReference rootRef = root.getReference();
        //DatabaseReference eventsRef = rootRef.child("users").child(user).child("registeredEvents");
        //reading user: key is username
        //String username = currentUser.getUsername();
        String username = user;
        reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    User value = dataSnapshot.getValue(User.class);
                    callback.onCallback(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });

        System.out.println("DBAgent.Java Line 156 user: " + user);
    }

    public void getEvent(Callback callback)
    {
        System.out.println("Entered get event");
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("events/events");
        reference.child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event value = dataSnapshot.getValue(Event.class);
                callback.onCallback(value);
                Log.d("FirstFragment", "Event name: " + value.getEventName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });
    }

    public void addUserPic(String username, String pic){
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users");
        reference.child(username + "/picture/").setValue(pic);
    }

//    public void getEventFromItem(Callback callback, String userItem){
//        root = FirebaseDatabase.getInstance();
//        reference = root.getReference("events/events");
//        reference.child(userItem).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Event value = dataSnapshot.getValue(Event.class);
//                callback.onCallback(value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                //textView.setText("Error in retrieving your message!");
//                Log.w("SecondFragment", "Failed to read value.", error.toException());
//            }
//        });
//    }

    public void getEvent(Callback callback, String eventID)
    {
        System.out.println("Entered get event ID: " + eventID);
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("events/events");
        reference.child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event value = dataSnapshot.getValue(Event.class);
                callback.onCallback(value);
                Log.d("FirstFragment", "Event name: " + value.getEventName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });
    }

    public void checkUserEvent(Callback callback, String username, String eventID){
        System.out.println("Entered get event ID: " + eventID);
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users").child(username);

        reference.child("registeredEvents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Event value = dataSnapshot.getValue(Event.class);
                if(dataSnapshot.hasChild(eventID)){
                    System.out.println("DBAgent line 225: True");
                }
                else{
                    System.out.println("DBAgent line 228: False");
                }
                callback.onCallback(dataSnapshot.hasChild(eventID));
                //dataSnapshot.child("a").exists();
                //Log.d("FirstFragment", "Event name: " + value.getEventName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });
    }

    public void removeUserEvent(String username, String eventID){
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users");
        reference.child(username + "/registeredEvents/" + eventID).removeValue();
    }

    public void getUserEventList(Callback callback, String username){
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users").child(username);


        reference.child("registeredEvents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Debug", "Data changed!");
                HashMap<String,String> tempMap = new HashMap<>();
                ArrayList<String> userEventList = new ArrayList<String>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String eventNum = ds.getValue(String.class);
                    userEventList.add(eventNum);
                    System.out.println("Events: " + eventNum);
                }

                //whats wrong with using strings?
                //bounded wildcards
                callback.onCallback(userEventList, "nothing");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.d("debug", "Failed to read value.", error.toException());
            }
        });
    }

    public void getEventList(Callback callback)
    {
        System.out.println("Entered get eventList");
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("events");

        reference.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Event> eventList = new ArrayList<Event>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Event value = child.getValue(Event.class);
                  //  System.out.println("event name:" + value.getEventName());
                    eventList.add(value);
                }
                callback.onCallback(eventList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });
    }

    /////////////////TESTING METHODS ////////////////////////////////////////////////////////////////////
    public void testReadDB(){

        //READ EVENT////////////////////////////////////
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("events/events");
        reference.child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event eventObj = dataSnapshot.getValue(Event.class);
                Log.d("FirstFragment", "Event name: " + eventObj.getEventName());

                //textView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //textView.setText("Error in retrieving your message!");
                Log.w("SecondFragment", "Failed to read value.", error.toException());
            }
        });

    }

    public void TestDBMessage() {

        root = FirebaseDatabase.getInstance();


        //root.child("users").child(email).setValue(newUser);

        reference = root.getReference("message");
        reference.setValue("Nightmare Team");
    }

    public LocalDate getBirthFormat(User user) {
        String birthday = user.getBirthday();
        String month = birthday.substring(0, 2);
        String day = birthday.substring(2, 4);
        String year = birthday.substring(4);
        String input = year + "-" + month + "-" + day;
        LocalDate dob = LocalDate.parse(input);
        return dob;
    }

    // get age of user
    public int getAge(User user) {

        LocalDate currDate = LocalDate.now();
        LocalDate dob = getBirthFormat(user);
        if ((dob != null) && (currDate != null)) {
            return Period.between(dob, currDate).getYears();
        } else {
            return 0;
        }
    }
}

//on click log in
//get com.example.csci310eventme.User from database
//verify credentials
//public void loginUser()

//getUser();
//validateUser();


//get com.example.csci310eventme.User from database
//public void getUser()



//compare user input & database com.example.csci310eventme.User
//public boolean validateUser()

//public void logoutUser()
//update database with com.example.csci310eventme.User eventIds
//remove com.example.csci310eventme.User object

//PROFILE
//public void getProfile()
//get com.example.csci310eventme.User
//EventIds --> EventList

//EVENT BOX
//public com.example.csci310eventme.Event getEventDetails(int eventId)


////////////////////////////////////////////////////MAIN FEATURES BEHAVIORS//////////////////////////////////////////////////

////////////////////////////////////////////////////REGISTER FEATURE//////////////////////////////////////////////////
//public void registerEvent()



//public void unregisterEvent()





////////////////////////////////////////////////////MAP FEATURE//////////////////////////////////////////////////
//public ArrayList<com.example.csci310eventme.Event> getExploreEvents()

//DB query
//process response
//edit Explore layout

//public void sortExploreEvents()





////////////////////////////////////////////////////EXPLORE FEATURE//////////////////////////////////////////////////
//public ArrayList<com.example.csci310eventme.Event> getMapEvents()

//DB query
//process response
//edit Map layout


//}