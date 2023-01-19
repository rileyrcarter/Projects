package com.example.csci310eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private DBAgent db = new DBAgent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void rerouteToExplore(View v){
        EditText usernamev = (EditText) findViewById(R.id.username);
        String username = usernamev.getText().toString();

        Intent i = new Intent(LoginActivity.this, ExploreActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("username", username);
        i.putExtras(bundle);
        startActivity(i);


        //sharedPrefences for session tracking
    }

    public void authenticateSignIn(View v){
        EditText userv = (EditText) findViewById(R.id.username);
        String user = userv.getText().toString();
        EditText passv = (EditText) findViewById(R.id.password);
        String pass = passv.getText().toString();

        if(checkFields(pass, user)){
            db.getUser(new Callback() {
                @Override
                public void onCallback(User value) {
                    System.out.println("Entered onCallback()");
                    if(value == null){
                        System.out.println("User not found");
                        Toast toast = Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        System.out.println("line 52: " + value.getUsername());
                        System.out.println("line 53: " + value.getPassword());
                        System.out.println("line 56: " + user);
                        System.out.println("line 57: " + pass);

                        if(value.getUsername().equals(user)){
                            if(value.getPassword().equals(pass)){
                                rerouteToExplore(v);
                            }else{
                                Toast toast = Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_LONG);
                                toast.show();
                                System.out.println("Wrong pass");}
                        }else{
                            Toast toast = Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_LONG);
                            toast.show();
                            System.out.println("Wrong user");}

                    }

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
            }, user);
        }

        //access DB here with the information
        //if cannot find, print error message that username/password is in error

    }

    public void rerouteToSignUp(View v){
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    public void continueAsGuest(View v){
        Intent i = new Intent(LoginActivity.this, ExploreActivity.class);
        Bundle bundle = new Bundle();
        String user = "";
        bundle.putString("username", user);
        i.putExtras(bundle);
        startActivity(i);
    }

    public Boolean checkFields(String pass, String user) {
        if (pass.length() < 8) {
            Toast toast = Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_LONG);
            toast.show();
        } else if (user.length() == 0) {
            Toast toast = Toast.makeText(this, "Username can't be empty", Toast.LENGTH_LONG);
            toast.show();
        } else {
            return true;
        }
        return false;
    }
}