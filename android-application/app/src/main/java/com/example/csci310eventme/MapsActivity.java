package com.example.csci310eventme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.internal.IUiSettingsDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String username;
    private DBAgent db = new DBAgent();
    public UiSettings settings;

    public boolean user_location = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set the content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        //CALLBACK TO PROCESS USER RESPONSE TO PERMISSION REQUEST
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result ->{
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if(fineLocationGranted != null && fineLocationGranted)
                    {
                        //precise location approved
                        user_location = true;
                    }
                    else if(coarseLocationGranted != null && coarseLocationGranted)
                    {
                        //approximate location approved
                        user_location = true;
                    }
                    else
                    {
                        //location access not granted
                        user_location = false;
                    }

                });

        //LAUNCH LOCATION PERMISSION REQUEST FROM USER
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        //mMap object
        //performs the following operations:
        //connect to google maps service
        //downloading map tiles
        //display tiles on the screen
        //display controls


        ListView listView = findViewById(R.id.list_view);
        listView.setVisibility(View.GONE);

//        TextView list_tab = findViewById(R.id.show_list);
//
//        //set VIEW LIST
//        list_tab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ListView listView = findViewById(R.id.list_view);
//                listView.setVisibility(View.VISIBLE);
//
//                //SET MAP LIST
//                db.getEventList(new Callback() {
//                    @Override
//                    public void onCallback(User value)
//                    {}
//                    @Override
//                    public void onCallback(boolean exists) {
//
//                    }
//                    @Override
//                    public void onCallback(ArrayList<String> userEventList, String nothing)
//                    {}
//                    @Override
//                    public void onCallback(Event value)
//                    {}
//                    @Override
//                    public void onCallback(ArrayList<Event> eventList)
//                    {
//                        sortListByDistance(eventList);
//                    }
//                });
//
//            }
//        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set map selected
        bottomNavigationView.setSelectedItemId(R.id.map);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.profile:

                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
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

        //access button
//        View buttonExplore = (View) findViewById(R.id.explore);
//        View buttonProfile = (View) findViewById(R.id.profile);
//
//        //add event listener
//        buttonExplore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //redirect
//                Intent i = new Intent(MapsActivity.this, ExploreActivity.class);
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
//                Intent i = new Intent(MapsActivity.this, ProfileActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                i.putExtras(bundle);
//                startActivity(i);
//
//            }
//        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //MAP API UI SETTINGS
        settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);

        //permissions requested on create
        //now check status of permissions

        //USER LOCATION, check permissions first
        //my location button only shows if permission is approved
        if (user_location) {

            mMap.setMyLocationEnabled(true);
            settings.setMyLocationButtonEnabled(true);

        }


        //ADDING EVENTS TO THE MAP
        // Add a marker in LA and move the camera
        LatLng la = new LatLng(34.0205, -118.2855);
        mMap.addMarker(new MarkerOptions().position(la).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(la));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));


        //get the list of events from the database
        //SET MAP MARKERS
        db.getEventList(new Callback() {
            @Override
            public void onCallback(User value)
            {}
            @Override
            public void onCallback(boolean exists) {

            }
            @Override
            public void onCallback(ArrayList<String> userEventList, String nothing)
            {}
            @Override
            public void onCallback(Event value)
            {}
            @Override
            public void onCallback(ArrayList<Event> eventList)
            {
                //set all
                //set event markers to map
                for(Event e : eventList)
                {
                    LatLng loc = new LatLng(e.getLatitude(), e.getLongitude());
                    Marker locMarker = mMap.addMarker(new MarkerOptions().position(loc).title(e.getEventName()));
                    locMarker.setTag(e);
                }

                //set on click of marker to open event box
                //set on swipe up to show list view
                //on swipe down return to map

                //sortListByDistance(eventList);
            }
        });

    }

    public void sortListByDistance(ArrayList<Event> events)
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

        //set sorted list to GUI
        setEventList(sortedEvents);
    }


    public void setWidget(Event event) {
        String eventText = event.setEventWidget();
        // inflates the event widget
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.event_widget, null);
        TextView eventView = popupView.findViewById(R.id.holder);

        eventView.setText(eventText);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

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

                //Add this
                Intent i = new Intent(MapsActivity.this, EventRegistration.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                String activity = "maps";
                bundle.putString("activity", activity);
                bundle.putString("eventID", String.valueOf(selectedEvent.getEventId()));
                i.putExtras(bundle);
                startActivity(i);
                //Ends here
            }
        });
    }

    //change weights of linear layout components to adjust GUI on button click
    //on click, show or hid list view in map
    public void showHideList(View v)
    {
        Button b = (Button)v;
        String current_status = b.getText().toString();

        ListView list_view = findViewById(R.id.list_view);
        View map_frag = findViewById(R.id.mapFragment);

        //make list visible
        if(current_status.equals("VIEW LIST"))
        {
            b.setText("HIDE LIST");
            list_view.setVisibility(View.VISIBLE);

            //change weights to 5 = list, 4= map
            LinearLayout.LayoutParams params_list = (LinearLayout.LayoutParams)list_view.getLayoutParams();
            params_list.weight = 5.0f;
            list_view.setLayoutParams(params_list);

            LinearLayout.LayoutParams params_map = (LinearLayout.LayoutParams)map_frag.getLayoutParams();
            params_map.weight = 4.0f;
            map_frag.setLayoutParams(params_map);


                //SET MAP LIST
                db.getEventList(new Callback() {
                    @Override
                    public void onCallback(User value)
                    {}
                    @Override
                    public void onCallback(boolean exists) {

                    }
                    @Override
                    public void onCallback(ArrayList<String> userEventList, String nothing)
                    {}
                    @Override
                    public void onCallback(Event value)
                    {}
                    @Override
                    public void onCallback(ArrayList<Event> eventList)
                    {
                        sortListByDistance(eventList);
                    }
                });

        }
        //hide list view
        else if(current_status.equals("HIDE LIST"))
        {
            b.setText("VIEW LIST");
            list_view.setVisibility(View.GONE);

            //change weights back to 9= map, 0 = list
            LinearLayout.LayoutParams params_list = (LinearLayout.LayoutParams)list_view.getLayoutParams();
            params_list.weight = 0.0f;
            list_view.setLayoutParams(params_list);

            LinearLayout.LayoutParams params_map = (LinearLayout.LayoutParams)map_frag.getLayoutParams();
            params_map.weight = 9.0f;
            map_frag.setLayoutParams(params_map);

        }

    }



    //SWIP UP SWIPE DOWN IMPLEMENTATION
//    View mapFrag = findViewById(R.id.mapFragment);
//    LinearLayout listView = findViewById(R.id.eventList);
//    mapFrag.setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if(event.getAction() == MotionEvent.ACTION_UP)
//            {
//                listView.setVisibility(View.VISIBLE);
//                setEventList(eventList);
//            }
//            else if(event.getAction() == MotionEvent.ACTION_DOWN)
//            {
//                listView.setVisibility(View.GONE);
//            }
//
//            return true;
//        }
//    });
}