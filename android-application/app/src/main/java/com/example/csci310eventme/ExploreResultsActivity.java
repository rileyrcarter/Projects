package com.example.csci310eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ExploreResultsActivity extends AppCompatActivity {
    String username;
    ArrayList<Event> events;
    DBAgent db = new DBAgent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "Entering EventResultsActivity.java");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_results);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        events = bundle.getParcelableArrayList("list");
        username = bundle.getString("username");
        ArrayList<Event> sortedEvents = sortByCost(events);
        setEventList(sortedEvents);

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
                //popupWindow.showAtLocation(findViewById(R.id.holder), Gravity.CENTER, 0, 0);
                Intent i = new Intent(ExploreResultsActivity.this, EventRegistration.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                String activity = "explore";
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

    public void setEventList(ArrayList<Event> eventList)
    {
        final ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this, R.layout.event_widget, R.id.eventWidget, eventList);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event selectedEvent = (Event)listView.getItemAtPosition(position);
                Intent i = new Intent(ExploreResultsActivity.this, EventRegistration.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                String activity = "explore";
                bundle.putString("activity", activity);
                bundle.putString("eventID", String.valueOf(selectedEvent.getEventId()));
                i.putExtras(bundle);
                startActivity(i);

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

    public ArrayList<Event> sortByCost(ArrayList<Event> eventList) {
        ArrayList<Integer> costs = new ArrayList<Integer>();
        ArrayList<Event> orderedEvents = new ArrayList<Event>();
        Map<Integer, ArrayList<Event>> costToEvents = new HashMap<Integer, ArrayList<Event>>();
        for (int i = 0; i < eventList.size(); i++) {
            costs.add(eventList.get(i).getCost());
            ArrayList<Event> eList = costToEvents.get(eventList.get(i).getCost());

            // if list does not exist create it
            if(eList == null) {
                eList = new ArrayList<Event>();
                eList.add(eventList.get(i));
                costToEvents.put(eventList.get(i).getCost(), eList);
            } else {
                // add if item is not already in list
                if(!eList.contains(eventList.get(i)))  {
                    eList.add(eventList.get(i));
                }
            }
        }

        Collections.sort(costs);
        for(Map.Entry<Integer, ArrayList<Event>> entry : costToEvents.entrySet()){
            for(Event event : entry.getValue())
            {
                orderedEvents.add(event);
            }
        }
        return orderedEvents;
    }

    public ArrayList<Event> sortListByDistance(ArrayList<Event> events)
    {
        //x coordinate
        double user_long = -118.2855;
        //y coordinate
        double user_lat = 34.0205;

        //distance list

        //pair object
        //key is eventId(index in array)
        //value is distance from user
        ArrayList<Pair<Integer, Double>> distanceList = new ArrayList<Pair<Integer, Double>>();

        //iterate events list
        //calculate distance for each
        //DISTANCE FORMULA = sqroot((lat-lat) ^2)+(long-long ^2)
        for(int i = 0; i<events.size(); i++)
        {
            Event e = events.get(i);
            double longitude = e.getLongitude();
            double latitude = e.getLatitude();

            double val_to_sqrt = ((user_long - longitude)*(user_long - longitude)) + ((user_lat-latitude)*(user_lat-latitude));

            double distance = Math.sqrt(val_to_sqrt);
            distanceList.add(new Pair<Integer, Double> (i, distance));
        }

        ArrayList<Event> sortedEvents = new ArrayList<>();

        //iterate distance arrayList
        //find shortest existing distance in distanceList
        while(distanceList.size() != 0)
        {
            //index corresponding to event in events list
            Integer closest_event_index = distanceList.get(0).first;
            //distance of event
            Double min_distance = distanceList.get(0).second;
            //distance list index, track for removal
            int min_distance_index = 0;

            //iterate to find min_distance
            for(int i = 0; i<distanceList.size(); i++)
            {
                if(min_distance >  distanceList.get(i).second)
                {
                    closest_event_index = distanceList.get(i).first;
                    min_distance = distanceList.get(i).second;
                    min_distance_index = i;
                }
            }

            //once entire list is searched, add event to sorted list
            sortedEvents.add(events.get(closest_event_index));
            //remove from distance list, no need to check again
            distanceList.remove(min_distance_index);
        }

        return sortedEvents;
    }


    public ArrayList<Event> sortByAlphabetical(ArrayList<Event> eventList) {
        String eventNames[] = new String[eventList.size()];
        Map<String, Event> nameToEvent = new HashMap<String, Event>();
        for (int i = 0; i < eventList.size(); i++) {
            eventNames[i] = eventList.get(i).getEventName();
            nameToEvent.put(eventList.get(i).getEventName(), eventList.get(i));
        }
        Arrays.sort(eventNames);
        ArrayList<Event> orderedEvents = new ArrayList<Event>();
        for (int i = 0; i < eventNames.length; i++) {
            orderedEvents.add(nameToEvent.get(eventNames[i]));
        }
        return orderedEvents;

    }

    public ArrayList<Event> sortByDate(ArrayList<Event> eventList) {
        ArrayList<Date> dates = new ArrayList<Date>();
        Map<Date, Event> dateToEvents = new HashMap<Date, Event>();
        for (int i = 0; i < eventList.size(); i++) {
            dates.add(eventList.get(i).getDate());
            dateToEvents.put(eventList.get(i).getDate(), eventList.get(i));
        }
        Collections.sort(dates, new SortByDate());
        ArrayList<Event> orderedEvents = new ArrayList<Event>();
        for (int i = 0; i < dates.size(); i++) {
            orderedEvents.add(dateToEvents.get(dates.get(i)));
        }
        return orderedEvents;
    }

    public void dateSort(View v) {
        ArrayList<Event> orderedEvents = sortByDate(events);
        setEventList(orderedEvents);
    }

    public void costSort(View v) {
        ArrayList<Event> orderedEvents = sortByCost(events);
        setEventList(orderedEvents);
    }

    public void alphabetSort(View v) {
        ArrayList<Event> orderedEvents = sortByAlphabetical(events);
        setEventList(orderedEvents);
    }

    public void distanceSort(View v) {
        ArrayList<Event> orderedEvents = sortListByDistance(events);
        setEventList(orderedEvents);
    }


    public void rerouteBack(View v) {
        Intent i = new Intent(ExploreResultsActivity.this, ExploreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        i.putExtras(bundle);
        startActivity(i);
    }

}