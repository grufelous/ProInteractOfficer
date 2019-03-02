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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class OfficerSetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference rtDB;
    private FirebaseStorage storage;
    EditText nameInput, aboutTextInputField, titleTextInputField, phoneNumberInputField;
    TextView setupIntroText;
    Spinner departmentSpinner;
    Button updateProfileBtn;
    //Switch availabilitySwitch;
    /*Circle*/ ImageView circularProfileImageView;

    protected void updateOfficerInfo(/*View view*/) {
        user = mAuth.getCurrentUser();
        String displayName = nameInput.getText().toString();
        UserProfileChangeRequest changeNameRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
        user.updateProfile(changeNameRequest);/*.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setupIntroText.setText("Hi, " + user.getDisplayName());
            }
        });*/
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
        nameInput = findViewById(R.id.nameInputField);
        phoneNumberInputField = findViewById(R.id.phoneNumberInputField);
        setupIntroText = findViewById(R.id.setupIntroText);
        circularProfileImageView = findViewById(R.id.profile_image);
        aboutTextInputField = findViewById(R.id.aboutTextInputField);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        titleTextInputField = findViewById(R.id.titleTextInputField);
        updateProfileBtn = findViewById(R.id.updateProfileBtn);
        //availabilitySwitch = findViewById(R.id.availableSwitch);
        //ArrayList<String> skillsList = new ArrayList<String>();
        //skillListAdapter = new ArrayAdapter<String>(this, R.layout.skill_row, skillsList);

        //Intent userUidIntent = getIntent();
        //Log.d("UIDUser", "onCreate: " + userUidIntent.getStringExtra("uid"));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        rtDB = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        circularProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        if(user.getPhotoUrl() != null) {
            StorageReference s = storage.getReference().child("images/" + user.getUid() + ".jpg");
            Log.d("UPIC", "onCreate: User logged in has picture: " + user.getPhotoUrl());
            Glide.with(this).load(s).into(circularProfileImageView);
            //Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.centerCropTransform()).into(circularProfileImageView);
            //circularProfileImageView.setImageURI(user.getPhotoUrl());
        } else {
            Log.d("UPIC", "onCreate: missing pic");
        }
        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(this, R.array.aicte_department_array, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);
        departmentSpinner.setOnItemSelectedListener(this);

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOfficerInfo();
            }
        });


        /*availabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rtDB.child("officer").child(user.getUid()).child("available").setValue(isChecked);
            }
        });*/
    }
    //image upload result
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

                        InputStream imageStream = getContentResolver().openInputStream(profileImageUri);

                        StorageReference s = storage.getReference();

                        StorageReference profileRef = s.child("images/" + user.getUid() + ".jpg");
                        final String TAG = "IMGUPD";
                        UploadTask uTask = profileRef.putStream(imageStream);
                        uTask.addOnCanceledListener(
                                new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        Log.d(TAG, "onCanceled: Image upload cancelled");
                                        Toast.makeText(OfficerSetupActivity.this, "Image upload cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Upload failed");
                                        Toast.makeText(OfficerSetupActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d(TAG, "onSuccess: Image successfully uploaded");
                                        Toast.makeText(OfficerSetupActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(profileImageUri).build();
                        user.updateProfile(profileChangeRequest)/*.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        })*/;
                    } catch (FileNotFoundException f) {
                        Toast.makeText(this, "File not found!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    //spinner item
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
