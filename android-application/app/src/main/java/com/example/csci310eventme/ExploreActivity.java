package com.example.csci310eventme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csci310eventme.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

public class ExploreActivity extends AppCompatActivity implements OnItemSelectedListener {
    DBAgent db = new DBAgent();
    String sortType = "cost";
    String cat = "All";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "Entering ExploreActivity.java");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        //Redirect to this page after successful signup/login
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        // create nav bar buttons and reroute features
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set map selected
        bottomNavigationView.setSelectedItemId(R.id.explore);

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
                    case R.id.profile:

                        Intent j = new Intent(getApplicationContext(), ProfileActivity.class);
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

//        View buttonMap = (View) findViewById(R.id.map);
//        View buttonProfile = (View) findViewById(R.id.profile);
//
//        //add event listener
//        buttonMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //redirect
//                Intent i = new Intent(ExploreActivity.this, MapsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                i.putExtras(bundle);
//                startActivity(i);
//
//            }
//        });
//
//        buttonProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //redirect
//                Intent i = new Intent(ExploreActivity.this, ProfileActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                i.putExtras(bundle);
//                startActivity(i);
//            }
//        });


        // Spinner elements
        Spinner spinCategories = (Spinner) findViewById(R.id.sortCategories);
        // Spinner click listener
        spinCategories.setOnItemSelectedListener(this);

        // Categories drop down elements
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Sports");
        categories.add("Social");
        categories.add("Shopping");
        categories.add("Music");
        categories.add("Service");

        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategories.setAdapter(catAdapter);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cat = parent.getItemAtPosition(position).toString();
    }

    public ArrayList<Event> sortByCategories(ArrayList<Event> eventList, String cat) {
        if (cat.equals("All")) return eventList;
        ArrayList<Event> matchingEventsCat = new ArrayList<Event>();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getCategory().equals(cat)) {
                matchingEventsCat.add(eventList.get(i));
            }
        }
        return matchingEventsCat;
    }

    public ArrayList<Event> search(ArrayList<Event> eventList) {
        EditText input1 = (EditText) findViewById(R.id.search_bar);
        String input = input1.getText().toString();
        ArrayList<Event> matchings = new ArrayList<Event>();
        input = input.toLowerCase(Locale.ROOT);
        if (input != "") {
            for (int i = 0; i < eventList.size(); i++) {
                String eventName = eventList.get(i).getEventName().toLowerCase(Locale.ROOT);
                String sponsor = eventList.get(i).getSponsor().toLowerCase(Locale.ROOT);
                String location = eventList.get(i).getLocation().toLowerCase(Locale.ROOT);
                if (eventName.contains(input)) {
                    matchings.add(eventList.get(i));
                } else if (sponsor.contains(input)) {
                    matchings.add(eventList.get(i));
                } else if (location.contains(input)) {
                    matchings.add(eventList.get(i));
                }
            }
        }
        return matchings;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public Date makeDate(String input) {
        int day = Integer.parseInt(input.substring(2,4));
        int month = Integer.valueOf(input.substring(0,2));
        int year = Integer.parseInt(input.substring(4));
        Date d = new Date(day, month, year);
        return d;
    }

    public ArrayList<Event> checkEventsDates(ArrayList<Event> events) {
        EditText date1 = (EditText) findViewById(R.id.dateRangeBeg);
        String begDate = date1.getText().toString();
        EditText date2 = (EditText) findViewById(R.id.dateRangeEnd);
        String endDate = date2.getText().toString();
        if (!begDate.equals("") && !endDate.equals(""))  {
            if (begDate.length() != 8 || endDate.length() != 8) {
                Toast toast = Toast.makeText(this, "Dates must be in MMDDYYYY format", Toast.LENGTH_LONG);
                toast.show();
            } else if (Integer.parseInt(begDate.substring(0, 2)) > 12 || Integer.parseInt(endDate.substring(0, 2)) > 12) {
                Toast toast = Toast.makeText(this, "Invalid month", Toast.LENGTH_LONG);
                toast.show();
            } else if (Integer.parseInt(begDate.substring(2, 4)) > 31 ||  Integer.parseInt(begDate.substring(2, 4)) < 0) {
                Toast toast = Toast.makeText(this, "Invalid day", Toast.LENGTH_LONG);
                toast.show();
            } else if (Integer.parseInt(endDate.substring(2, 4)) > 31 ||  Integer.parseInt(endDate.substring(2, 4)) < 0) {
                Toast toast = Toast.makeText(this, "Invalid day", Toast.LENGTH_LONG);
                toast.show();
            } else if (Integer.parseInt(begDate.substring(4)) > 2022 ||  Integer.parseInt(begDate.substring(4)) < 1900) {
                Toast toast = Toast.makeText(this, "Invalid year", Toast.LENGTH_LONG);
                toast.show();
            } else if (Integer.parseInt(endDate.substring(4)) > 2022 ||  Integer.parseInt(endDate.substring(4)) < 1900) {
                Toast toast = Toast.makeText(this, "Invalid year", Toast.LENGTH_LONG);
                toast.show();
            } else {
                for (int i = 0; i < events.size(); i++) {
                    if (!events.get(i).getDate().validDate(makeDate(begDate), makeDate(endDate))) {
                        events.remove(events.get(i));
                    }
                }
            }

        }
        return events;
    }


    public void rerouteToResults(View v) {
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
            public void onCallback(ArrayList<Event> eventList) {
                ArrayList<Event> matches = sortByCategories(eventList, cat);
                matches = checkEventsDates(matches);
                matches = search(matches);
                Intent i = new Intent(ExploreActivity.this, ExploreResultsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) matches);
                i.putExtra("BUNDLE", bundle);
                bundle.putString("username", username);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }


}

