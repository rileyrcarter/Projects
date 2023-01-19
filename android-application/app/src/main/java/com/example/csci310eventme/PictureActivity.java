package com.example.csci310eventme;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.UUID;

public class PictureActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    Button upload;
    Button default1;
    ImageView pic;
    DBAgent agent = new DBAgent();
    String username;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        pic = (ImageView) findViewById(R.id.imageView);
        upload = (Button) findViewById(R.id.upload);
        default1 = (Button) findViewById(R.id.default1);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

        pic.setOnClickListener(this);
        upload.setOnClickListener(this);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri selectedImage = data.getData();
                            pic.setImageURI(selectedImage);
                            agent.addUserPic(username, selectedImage.toString());
                            Intent i = new Intent(PictureActivity.this, ExploreActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);
                            i.putExtras(bundle);
                            startActivity(i);

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        someActivityResultLauncher.launch(galleryIntent);
    }

    public void useDefault(View v) {
        if (username != "") {
            Uri selectedImage = Uri.parse("android.resource://your.package.here/drawable/village");
            pic.setImageURI(selectedImage);
            agent.addUserPic(username, selectedImage.toString());
        }
        Intent i = new Intent(PictureActivity.this, ExploreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        i.putExtras(bundle);
        startActivity(i);


    }

}

