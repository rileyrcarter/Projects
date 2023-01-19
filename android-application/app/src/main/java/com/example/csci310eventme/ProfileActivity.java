package com.example.csci310eventme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.collection.LLRBNode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    String username;
    ArrayList<Event> events;
    DBAgent db = new DBAgent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "Entering ProfileActivity.java");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        if(username.equals("")){
            setGuestMode();
        }
        //FUNCTIONAL GET EVENT DB --> ACTIVITY
        db.getUser(new Callback() {
            @Override
            public void onCallback(User value) {
                //System.out.println("Entered onCallback()");
                //System.out.println("line 34: " + value.getUsername());
                //WITHIN ONCALLBACK
                //LOCATION WHERE objects are used to update LAYOUT FILES

                setProfile(value);
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

            }
            @Override
            public void onCallback(ArrayList<Event> eventList)
            {

            }
        }, username);

        db.getUserEventList(new Callback(){

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
                db.getEventList(new Callback() {
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

                    }
                    @Override
                    public void onCallback(ArrayList<Event> eventList)
                    {
                        //This is the eventList
                        //compare with list you get here
                        //userEventList;
                        //grab each item in userEventList and compare ID's
                        for(int k=0; k< userEventList.size(); k++){
                            System.out.println("Profile Act:123: " + userEventList.get(k));
                        }

                        ArrayList<Event> updatedList = new ArrayList<Event>();
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
                        setEventList(updatedList);
                    }
                });
            }
        }, username);


        //access button
        View buttonMap = (View) findViewById(R.id.map);
        View buttonExplore = (View) findViewById(R.id.explore);
        View buttonProfile = (View) findViewById(R.id.profile);
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
//        bnv.getMenu().getItem(0).getIcon().setAlpha(130);
//        bnv.getMenu().getItem(1).getIcon().setAlpha(130);
//        bnv.getMenu().getItem(2).getIcon().setAlpha(255);
        //BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //menu.getMenu().getItem(0).set
        //menu.getItem(0).getIcon().setAlpha(130);

        //buttonProfile.getICON

      //  buttonProfile.setBackgroundColor(Color.BLUE);
     //   buttonMap.setBackgroundColor(Color.LTGRAY);
     //   buttonExplore.setBackgroundColor(Color.LTGRAY);
        //add event listener
        //=====================
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set map selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.map:

                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        i.putExtras(bundle);
                        startActivity(i);

                        //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.explore:

                        Intent j = new Intent(getApplicationContext(), ExploreActivity.class);
                        Bundle bundles = new Bundle();
                        bundles.putString("username", username);
                        j.putExtras(bundles);
                        startActivity(j);

                        //startActivity(new Intent(getApplicationContext(),ExploreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



        //==============================
//        buttonMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //redirect
//                Intent i = new Intent(ProfileActivity.this, MapsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                i.putExtras(bundle);
//                startActivity(i);
//            }
//
//        });
//
//        buttonExplore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //redirect
//                Intent i = new Intent(ProfileActivity.this, ExploreActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                i.putExtras(bundle);
//                startActivity(i);
//            }
//        });

    }

    //parse the java object
    public void setProfile(User user){
        setProfilePic(user);
        //how do you set the string from activity to xml?
        String temp;
        TextView firstnameTextView = (TextView) findViewById(R.id.first_name);
        temp = user.getFirstName();
        firstnameTextView.setText(temp);


        final TextView lastnameTextView = (TextView) findViewById(R.id.last_name);
        temp = user.getLastName();
        lastnameTextView.setText(temp);

        final TextView birthdayTextView = (TextView) findViewById(R.id.birthday);
        temp = user.getBirthday();
        temp = user.getBirthdayFormat(temp);
        birthdayTextView.setText(temp);


//        TextView usernameTextView = (TextView) findViewById(R.id.username);
//        temp = "Username: " + user.getUsername();
//        usernameTextView.setText(temp);
    }

    public void setProfilePic(User u) {
        System.out.println("username is: " + u.getUsername());
        if (u.getUsername() != null) {
            String uriString = u.getPicture();
            Uri pic = Uri.parse(uriString);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pic);
            } catch (IOException e) {
            }
            ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
            profilePic.setVisibility(View.VISIBLE);
            if (bitmap != null) {
                profilePic.setImageBitmap(bitmap);
            }
        } else {
            ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
            profilePic.setVisibility(View.GONE);
        }
    }




    public void setWidget(Event event) {
        String eventText = event.setEventWidget();
        // inflates the event widget
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.event_widget, null);
        TextView eventView = popupView.findViewById(R.id.eventWidget);

        eventView.setText(eventText);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // code to make pop up automatically; however, will be adjusted as a clickable button
        findViewById(R.id.holder).post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.holder), Gravity.CENTER, 0, 0);
                Intent i = new Intent(ProfileActivity.this, EventRegistration.class);
                Bundle bundle = new Bundle();
                String activity = "profile";
                bundle.putString("username", username);
                bundle.putString("activity", activity);
                bundle.putString("eventID", String.valueOf(event.getEventId()));
                i.putExtras(bundle);
                startActivity(i);
                //System.out.println("Popup window opened");
            }
        });
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

//    public ArrayList<Event> getEventsFromList(ArrayList<String> userList){
//        //for each item, pull from db and get event information
//
//        for(int i =0; i< userList.size(); i++){
//            db.getEventFromItem(new Callback() {
//                @Override
//                public void onCallback(Event event) {
//
//                }
//
//                @Override
//                public void onCallback(ArrayList<Event> events) {
//
//                }
//
//                @Override
//                public void onCallback(User user) {
//
//                }
//
//                @Override
//                public void onCallback(boolean exists) {
//
//                }
//
//                @Override
//                public void onCallback(ArrayList<String> userEventList, String nothing) {
//
//                }
//            }, userList.get(0));
//        }
//    }

    public void setEventList(ArrayList<Event> eventList)
    {
        System.out.println("Profile Activity: 301 events: " + eventList.size());
        final ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this, R.layout.event_widget, R.id.eventWidget, eventList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event selectedEvent = (Event)listView.getItemAtPosition(position);

                //Add this
                Intent i = new Intent(ProfileActivity.this, EventRegistration.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                String activity = "profile";
                bundle.putString("activity", activity);
                bundle.putString("eventID", String.valueOf(selectedEvent.getEventId()));
                i.putExtras(bundle);
                startActivity(i);
                //Ends here
            }
        });
    }

    public void createListener(TextView v, Event e) {
        v.setText(e.toString());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWidget(e);
            }
        });
    }

    public void rerouteToSignIn(View v){
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void setGuestMode() {
        Button logB = findViewById(R.id.loginButton);
        logB.setVisibility(View.VISIBLE);
        TextView first = findViewById(R.id.first_name);
        first.setVisibility(View.GONE);
        TextView last = findViewById(R.id.last_name);
        last.setVisibility(View.GONE);
        TextView birth = findViewById(R.id.birthday);
        birth.setVisibility(View.GONE);
        TextView logO = findViewById(R.id.logOutButton);
        logO.setVisibility(View.GONE);
        ListView eL = (ListView) findViewById(R.id.list_view);
        if(eL != null){
            eL.setVisibility(View.GONE);
        }
        /*
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rerouteToSignIn(v);
            }
        });

         */
    }

}