package com.example.csci310eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class EventRegistration extends AppCompatActivity {
    String username = "blank";
    String eventID = "blank";
    String activity = "";
    private DBAgent db = new DBAgent();
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("debug", "Entering EventRegistration.java");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);

        getIntentInformation();
        //Get event from click
        getEventFromClick();

        //set the registers button
        regButton = (Button) findViewById(R.id.registerButton);

        System.out.println("EVRegis.line60: ID: " + eventID);
        //create a callback to check if eventID exists
        //getevent - if event exists, then set text to unregister

        if(username.equals("")){
            regButton.setText("Login/Sign up");

            regButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {                     //sign in/login
                        //redirect to login/signup page
                        Intent i = new Intent(EventRegistration.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                }
            });
        }else{
            Log.d("Debug", "Entered onCallBack!");
            db.checkUserEvent(new Callback() {
                @Override
                public void onCallback(Event value) {
                    //all we need is this
                }

                @Override
                public void onCallback(ArrayList<Event> events) {

                }

                @Override
                public void onCallback(User user) {

                }

                @Override
                public void onCallback(boolean exists) {
                    if(!exists){
                        System.out.println("Profile Line 68: Object is null, setting button to register");
                        regButton.setText("Register");
                        //grab the event date class
                        //compare the dates with the userevent list

                        System.out.println("EVREG.Java: 112, EventExists");

                        db.getEvent(new Callback() {
                            @Override
                            public void onCallback(Event event) {

                                Log.d("Debug","EVREG.Java: 118, Getting Event");
                                db.getUserEventList(new Callback() {
                                    @Override
                                    public void onCallback(Event event) {

                                    }

                                    @Override
                                    public void onCallback(ArrayList<Event> events) {

                                    }

                                    @Override
                                    public void onCallback(User user) {

                                    }

                                    @Override
                                    public void onCallback(boolean exists) {

                                    }

                                    @Override
                                    public void onCallback(ArrayList<String> userEventList, String nothing) {

                                        Log.d("Debug","EVREG.Java: 143 Getting UserEventList");

                                        db.getEventList(new Callback() {
                                            @Override
                                            public void onCallback(Event event) {

                                            }

                                            @Override
                                            public void onCallback(ArrayList<Event> eventList) {

                                                Log.d("Debug","EVREG.Java: 154, Getting EventList, Checking Conflicts");
                                                //updateList is the usersList filled with event objects
                                                ArrayList<Event> updatedList = new ArrayList<Event>();
                                                System.out.println("UserEventListsize: " + userEventList.size());
                                                System.out.println("EventListSize:  " + eventList.size());
                                                for(int i = 0; i < userEventList.size(); i++){
                                                    for(int j = 0; j< eventList.size(); j++){
                                                        int temp = Integer.valueOf(userEventList.get(i));
                                                        if(eventList.get(j).getEventId() == temp){
                                                            updatedList.add(eventList.get(j));
                                                            System.out.println("Adding onto list");
                                                            break;
                                                        }
                                                    }
                                                }


                                                Date eventDate = event.getDate();
                                                int day = eventDate.getDay();
                                                String month = eventDate.getMonth();
                                                int year = eventDate.getYear();

                                                Log.d("debug","EVREG.java Line174: " + month + " " + day + " " + year);

                                                for(int i = 0; i < updatedList.size(); i++){
                                                    Date tempDate = updatedList.get(i).getDate();
                                                    if(tempDate.getYear() == year){
                                                        System.out.println("Same year!");
                                                        if(tempDate.getMonth().equals(month)){
                                                            Log.d("debug", "EVREG.java Line 178 Month: " + month + "TempMonth: " + tempDate.getMonth());
                                                            if(tempDate.getDay() == day){
                                                                Toast toast = Toast.makeText(EventRegistration.this, "This event conflicts with your registered events list.", Toast.LENGTH_LONG);
                                                                toast.show();
                                                                Log.d("debug","EVREG.java: 181 Conflicting Schedules");
                                                                break;
                                                            }
                                                        }
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCallback(User user) {

                                            }

                                            @Override
                                            public void onCallback(boolean exists) {

                                            }

                                            @Override
                                            public void onCallback(ArrayList<String> userEventList, String nothing) {

                                            }
                                        });

                                    }
                                }, username);

                                db.getUser(new Callback(){
                                    @Override
                                    public void onCallback(Event event) {

                                    }

                                    @Override
                                    public void onCallback(ArrayList<Event> events) {

                                    }

                                    @Override
                                    public void onCallback(User user) {

                                        int userAge = db.getAge(user);
                                        event.getAgeReq();
                                        if(userAge <= event.getAgeReq()){
                                            Toast toast = Toast.makeText(EventRegistration.this, "You do not meet the age requirements for this event.", Toast.LENGTH_LONG);
                                            toast.show();
                                            regButton.setText("Age Restricted");
                                            Log.d("debug","Age Restricted");
                                        }else {
                                            regButton.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View arg0) {
                                                    //sign in/login
                                                    //register
                                                    if (regButton.getText().equals("Register")) {
                                                        Log.d("Debug", "Added event!");
                                                        db.addEvent(username, eventID);
                                                    } else {
                                                        //unregister the event
                                                        Log.d("Debug", "Removed event!");
                                                        db.removeUserEvent(username, eventID);
                                                    }

                                                    Intent i = new Intent(EventRegistration.this, ProfileActivity.class);
                                                    Log.d("debug", "regButtonOnCLick");
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("username", username);
                                                    bundle.putString("eventID", eventID);
                                                    i.putExtras(bundle);
                                                    startActivity(i);
                                                    //overridePendingTransition(0,0);
                                                }
                                            });
                                            Log.d("debug","Age Not Restricted");
                                        }

                                    }

                                    @Override
                                    public void onCallback(boolean exists) {

                                    }

                                    @Override
                                    public void onCallback(ArrayList<String> userEventList, String nothing) {

                                    }
                                }, username);
                            }

                            @Override
                            public void onCallback(ArrayList<Event> events) {

                            }

                            @Override
                            public void onCallback(User user) {

                            }

                            @Override
                            public void onCallback(boolean exists) {

                            }

                            @Override
                            public void onCallback(ArrayList<String> userEventList, String nothing) {

                            }
                        }, eventID);


                    }else{
                        System.out.println("Profile Line 71: Object is not null, setting button to unregister");
                        regButton.setText("Unregister");

                        regButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                //sign in/login
                                //register
                                if (regButton.getText().equals("Register")) {
                                    Log.d("Debug", "Added event!");
                                    db.addEvent(username, eventID);
                                } else {
                                    //unregister the event
                                    Log.d("Debug", "Removed event!");
                                    db.removeUserEvent(username, eventID);
                                }

                                Intent i = new Intent(EventRegistration.this, ProfileActivity.class);
                                Log.d("debug", "regButtonOnCLick");
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                bundle.putString("eventID", eventID);
                                i.putExtras(bundle);
                                startActivity(i);
                                //overridePendingTransition(0,0);
                            }
                        });
                    }



//                    regButton.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View arg0) {
//                            //sign in/login
//                            //register
//                            if (regButton.getText().equals("Register")) {
//                                Log.d("Debug", "Added event!");
//                                db.addEvent(username, eventID);
//                            } else {
//                                //unregister the event
//                                Log.d("Debug", "Removed event!");
//                                db.removeUserEvent(username, eventID);
//                            }
//
//                            Intent i = new Intent(EventRegistration.this, ProfileActivity.class);
//                            Log.d("debug", "regButtonOnCLick");
//                            Bundle bundle = new Bundle();
//                            bundle.putString("username", username);
//                            bundle.putString("eventID", eventID);
//                            i.putExtras(bundle);
//                            startActivity(i);
//                            //overridePendingTransition(0,0);
//                        }
//                    });
                }

                @Override
                public void onCallback(ArrayList<String> userEventList, String nothing) {

                }
            }, username, eventID);


        }


    }

    public void getIntentInformation(){
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        eventID = bundle.getString("eventID");
        activity = bundle.getString("activity");
        System.out.println("User: " + username);
        System.out.println("EventID: " + eventID);
        System.out.println("Activity: " + activity);
    }

    public void registerEvent(View v){
       //get the current event and then store it into the db
        //EventID, pass eventID into UserList or events
        //Registered!
        //dbagent.Addevent
        db.addEvent(username, eventID);
    }

    public void reroute(View v) {
        if (activity.equals("maps")) {
            Intent i = new Intent(EventRegistration.this, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            i.putExtras(bundle);
            startActivity(i);
        } else if (activity.equals("profile")) {
            Intent i = new Intent(EventRegistration.this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            i.putExtras(bundle);
            startActivity(i);
        } else {
            Intent i = new Intent(EventRegistration.this, ExploreActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            i.putExtras(bundle);
            startActivity(i);
        }
    }


    public void setEventInfo(Event value){
        TextView eventNameTextView = (TextView) findViewById(R.id.eventName);
        String temp = value.getEventName();
        eventNameTextView.setText(temp);

        TextView eventDateTV = (TextView) findViewById(R.id.eventDate);
        temp = value.getDate().toString();
        eventDateTV.setText(temp);

        TextView eventTimeTV = (TextView) findViewById(R.id.eventTime);
        temp = value.getTime().toString();
        eventTimeTV.setText(temp);

        TextView locationTV = (TextView) findViewById(R.id.eventLocation);
        temp = value.getLocation();
        locationTV.setText("Location: " + temp);

        //needs to be changed to picture
        TextView picturesTextView = (TextView) findViewById(R.id.pictures);
        temp = value.getEventName();
        picturesTextView.setText(temp);

        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        temp = value.getDescription();
        descriptionTextView.setText(temp);

        TextView eventTypeTextView = (TextView) findViewById(R.id.event_type);
        temp = value.getCategory();
        eventTypeTextView.setText("Category: " + temp);

        TextView parkingTextView = (TextView) findViewById(R.id.parking_availability);
        temp = value.getParking();
        parkingTextView.setText("Parking: " + temp);

        TextView numRegisteredTextView = (TextView) findViewById(R.id.num_people);
        int temp1 = value.getNumPeople();
        numRegisteredTextView.setText("Number of People: " + String.valueOf(temp1));

        setPhoto(value.getEventId());
    }

    public void getEventFromClick(){
        db.getEvent(new Callback() {
            @Override
            public void onCallback(User value) {

            }

            @Override
            public void onCallback(boolean exists) {

            }

            @Override
            public void onCallback(ArrayList<String> userEventList, String nothing) {

            }

            @Override
            public void onCallback(Event value)
            {
                System.out.println("Event: " + value.getEventName());
                //set text values here
                setEventInfo(value);
            }
            @Override
            public void onCallback(ArrayList<Event> eventList)
            {

            }
        }, eventID);
    }


    public void setPhoto(int num) {
        ImageView pic = (ImageView) findViewById(R.id.pic);
        if (num == 0) {
            pic.setImageResource(R.drawable.coliseum);
        } else if (num == 1) {
            pic.setImageResource(R.drawable.uglysweater);
        } else if (num == 2) {
            pic.setImageResource(R.drawable.blockparty);
        } else if (num == 4) {
            pic.setImageResource(R.drawable.thriftstore);
        } else if (num == 5) {
            pic.setImageResource(R.drawable.jazz);
        } else if (num == 6) {
            pic.setImageResource(R.drawable.marchingband);
        } else if (num == 7) {
            pic.setImageResource(R.drawable.cards);
        } else if (num == 8) {
            pic.setImageResource(R.drawable.kidssoccer);
        } else if (num == 9) {
            pic.setImageResource(R.drawable.christmasgifts);
        } else if (num == 10) {
            pic.setImageResource(R.drawable.bookstore);
        } else if (num == 11) {
            pic.setImageResource(R.drawable.tommysplace);
        } else if (num == 12) {
            pic.setImageResource(R.drawable.mixer);
        } else if (num == 13) {
            pic.setImageResource(R.drawable.boardgames);
        } else if (num == 14) {
            pic.setImageResource(R.drawable.swimmingpool);
        } else if (num == 15) {
            pic.setImageResource(R.drawable.bar);
        } else if (num == 16) {
            pic.setImageResource(R.drawable.donuts);
        } else if (num == 17) {
            pic.setImageResource(R.drawable.kidssoccer);
        } else if (num == 18) {
            pic.setImageResource(R.drawable.magnetschool);
        } else if (num == 19) {
            pic.setImageResource(R.drawable.baseball_stadium);
        }
    }


    //onclick

}