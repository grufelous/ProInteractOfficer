package com.orion.prointeractofficer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfficerSetupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference rtdbSetup;
    EditText firstNameInput, secondNameInput, aboutTextInputField;
    TextView setupIntroText;
    Spinner departmentSpinner;

    CircleImageView circularProfileImageView;

    protected void updateOfficerInfo(View view) {
        user = mAuth.getCurrentUser();
        String displayName = firstNameInput.getText().toString() + " " + secondNameInput.getText().toString();
        UserProfileChangeRequest changeNameRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
        user.updateProfile(changeNameRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setupIntroText.setText("Hi, " + user.getDisplayName());
            }
        });
        rtdbSetup.child("officer").child(user.getUid()).child("about").setValue(setupIntroText.getText().toString());

    }

    private final int SELECT_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_setup_layout);
        firstNameInput = findViewById(R.id.firstNameInputField);
        secondNameInput = findViewById(R.id.secondNameInputField);
        setupIntroText = findViewById(R.id.setupIntroText);
        circularProfileImageView = findViewById(R.id.profile_image);
        aboutTextInputField = findViewById(R.id.aboutTextInputField);
        //departmentSpinner = findViewById(R.id.departmentSpinner);
        rtdbSetup = FirebaseDatabase.getInstance().getReference();

        //Intent userUidIntent = getIntent();
        //Log.d("UIDUser", "onCreate: " + userUidIntent.getStringExtra("uid"));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        circularProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        if(user.getPhotoUrl() != null) {
            Log.d("UPIC", "onCreate: User logged in has picture: " + user.getPhotoUrl());
            //circularProfileImageView.setImageURI(user.getPhotoUrl());
        }
        /*ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(this, R.array.aicte_department_array, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);*/


    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, responseCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(responseCode == RESULT_OK) {
                    Uri profileImageUri;
                    UserProfileChangeRequest profileChangeRequest;
                    try {
                        profileImageUri = imageReturnedIntent.getData();
                        circularProfileImageView.setImageURI(profileImageUri);
                        profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(profileImageUri).build();
                        user.updateProfile(profileChangeRequest)/*.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        })*/;
                    } catch (Exception e) {
                        Toast.makeText(this, "Image updation error", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}
