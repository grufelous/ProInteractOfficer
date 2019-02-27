package com.orion.prointeractofficer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfficerSetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference rtDB;
    EditText firstNameInput, secondNameInput, aboutTextInputField, titleTextInputField, phoneNumberInputField;
    TextView setupIntroText;
    Spinner departmentSpinner;
    Switch interactivitySwitch, availabilitySwitch;
    /*Circle*/ ImageView circularProfileImageView;
    ListView skillListView;
    ArrayAdapter<String> skillListAdapter;

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
        rtDB.child("officer").child(user.getUid()).child("name").setValue(displayName);
        rtDB.child("officer").child(user.getUid()).child("contact").setValue(phoneNumberInputField.getText().toString());
        rtDB.child("officer").child(user.getUid()).child("title").setValue(titleTextInputField.getText().toString());
        rtDB.child("officer").child(user.getUid()).child("about").setValue(aboutTextInputField.getText().toString());
    }

    private final int SELECT_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_setup_layout);
        firstNameInput = findViewById(R.id.firstNameInputField);
        secondNameInput = findViewById(R.id.secondNameInputField);
        phoneNumberInputField = findViewById(R.id.phoneNumberInputField);
        setupIntroText = findViewById(R.id.setupIntroText);
        circularProfileImageView = findViewById(R.id.profile_image);
        aboutTextInputField = findViewById(R.id.aboutTextInputField);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        titleTextInputField = findViewById(R.id.titleTextInputField);
        interactivitySwitch = findViewById(R.id.interactionSwitch);
        availabilitySwitch = findViewById(R.id.availableSwitch);
        //ArrayList<String> skillsList = new ArrayList<String>();
        //skillListAdapter = new ArrayAdapter<String>(this, R.layout.skill_row, skillsList);

        //Intent userUidIntent = getIntent();
        //Log.d("UIDUser", "onCreate: " + userUidIntent.getStringExtra("uid"));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        rtDB = FirebaseDatabase.getInstance().getReference();

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
            //@TODO: loads an empty image. Fix the missing resource issue. Also ensure circle cropping.
            Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.centerCropTransform()).into(circularProfileImageView);
            //circularProfileImageView.setImageURI(user.getPhotoUrl());
        }
        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(this, R.array.aicte_department_array, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);
        departmentSpinner.setOnItemSelectedListener(this);
        interactivitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rtDB.child("officer").child(user.getUid()).child("interactivity").setValue(isChecked);
            }
        });
        availabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rtDB.child("officer").child(user.getUid()).child("available").setValue(isChecked);
            }
        });
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String dept = parent.getItemAtPosition(position).toString();
        Log.d("item", "onItemSelected: selected " + dept);
        rtDB.child("officer").child(user.getUid()).child("department").setValue(dept);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
