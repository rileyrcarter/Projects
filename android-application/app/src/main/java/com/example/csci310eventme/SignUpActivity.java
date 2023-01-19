package com.example.csci310eventme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csci310eventme.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private DBAgent db = new DBAgent();
    private boolean unique_username = false;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        System.out.println("SignUpActivity: Testing read Event");
        signUpButton = (Button) findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {                     //sign in/login
                //redirect to login/signup page
                //do my check here!

                EditText passv = (EditText) findViewById(R.id.password);
                String pass = passv.getText().toString();
                EditText usernamev = (EditText) findViewById(R.id.username);
                String username = usernamev.getText().toString();
                EditText firstv = (EditText) findViewById(R.id.first_name);
                String first = firstv.getText().toString();
                EditText lastv = (EditText) findViewById(R.id.last_name);
                String last = lastv.getText().toString();
                EditText birthdayv = (EditText) findViewById(R.id.birthday);
                String birthday = birthdayv.getText().toString();

                db.getUser(new Callback() {
                    @Override
                    public void onCallback(User value) {
                        //username does not exist
                        if(value == null)
                        {
                            if(checkFields(pass, username, first, last, birthday)){
                                db.createUser(pass, username, first, last, birthday);
                                Intent i = new Intent(SignUpActivity.this, PictureActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username", username);
                                i.putExtras(bundle);
                                startActivity(i);
                                overridePendingTransition(0,0);
                            }
                        }
                        else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_LONG);
                            toast.show();
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
                }, username);

            }
        });


    }

    public void rerouteToExplore(View v){
        EditText passv = (EditText) findViewById(R.id.password);
        String pass = passv.getText().toString();
        EditText usernamev = (EditText) findViewById(R.id.username);
        String username = usernamev.getText().toString();
        EditText firstv = (EditText) findViewById(R.id.first_name);
        String first = firstv.getText().toString();
        EditText lastv = (EditText) findViewById(R.id.last_name);
        String last = lastv.getText().toString();
        EditText birthdayv = (EditText) findViewById(R.id.birthday);
        String birthday = birthdayv.getText().toString();
        // error messages
        // && unique_username
        if (checkFields(pass, username, first, last, birthday)) {
            db.createUser(pass, username, first, last, birthday);
            Intent i = new Intent(SignUpActivity.this, PictureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            i.putExtras(bundle);
            startActivity(i);
        }

    }

    public void rerouteToSignIn(View v){
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public Boolean checkFields(String pass, String user, String first, String last, String birth) {
        //checkUniqueUsername(user);

        if (pass.length() < 8) {
            Toast toast = Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_LONG);
            toast.show();
        } else if (user.length() == 0) {
            Toast toast = Toast.makeText(this, "Username can't be empty", Toast.LENGTH_LONG);
            toast.show();
        } else if (last.length() == 0) {
            Toast toast = Toast.makeText(this, "Last name can't be empty", Toast.LENGTH_LONG);
            toast.show();
        } else if (first.length() == 0) {
            Toast toast = Toast.makeText(this, "First name can't be empty", Toast.LENGTH_LONG);
            toast.show();
        } else if (birth.length() != 8) {
            Toast toast = Toast.makeText(this, "Birthday must be in MMDDYYYY format", Toast.LENGTH_LONG);
            toast.show();
        } else if (Integer.parseInt(birth.substring(0, 2)) > 12) {
            Toast toast = Toast.makeText(this, "Invalid month", Toast.LENGTH_LONG);
            toast.show();
        } else if (Integer.parseInt(birth.substring(2, 4)) > 31 ||  Integer.parseInt(birth.substring(2, 4)) < 0) {
            Toast toast = Toast.makeText(this, "Invalid day", Toast.LENGTH_LONG);
            toast.show();
        } else if (Integer.parseInt(birth.substring(4)) > 2022 ||  Integer.parseInt(birth.substring(4)) < 1900) {
            Toast toast = Toast.makeText(this, "Invalid year", Toast.LENGTH_LONG);
            toast.show();
        }
        else if(validateAge(pass, user, first, last, birth))
        {
            Toast toast = Toast.makeText(this, "Minimum Age to Sign Up is 10+", Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            return true;
        }
        return false;
    }

    public void checkUniqueUsername(String username)
    {
        db.getUser(new Callback() {
            @Override
            public void onCallback(User value) {
                if(value == null)
                {
                    updateUsernameBool(false);
                }
                else
                {
                    updateUsernameBool(true);
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
        }, username);
    }

    public void updateUsernameBool(boolean val)
    {
        this.unique_username = val;
    }

    public boolean validateAge(String pass, String user, String first, String last, String birth)
    {
        User tempUser = new User(pass,user, first, last, birth, "");
        int user_age = db.getAge(tempUser);
        if(user_age < 10)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

}