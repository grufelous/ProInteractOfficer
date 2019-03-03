package com.orion.prointeractofficer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class OfficerPanel extends AppCompatActivity {
    RelativeLayout officer_panel;
    TextView nameView;
    TextView availTextView;
    FirebaseUser user;
    DatabaseReference rtDB, userDirDB, availDB;
    FirebaseStorage storage;
    Button setAvlBtn, setUnavlbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rtDB = FirebaseDatabase.getInstance().getReference();
        userDirDB = rtDB.child("officer").child(user.getUid());
        availDB = userDirDB.child("available");

        setAvlBtn = findViewById(R.id.makeAvlBtn);
        setUnavlbtn = findViewById(R.id.makeUnavlBtn);

        setAvlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDirDB.child("available").setValue(true);

            }
        });

        setUnavlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDirDB.child("available").setValue(false);
            }
        });

        //Log.d("DEB", "onCreate: finding view for avail");
        //availTextView = findViewById(R.id.availView);
        //String avail;

        /*userDirDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dept = dataSnapshot.child("department").getValue().toString();
                Log.d("PANEL", "onDataChange: " + dept);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        /*availDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("DEB", "onDataChange: Old" + availTextView.getText());
                //String avail = dataSnapshot.getValue().toString();
                Log.d("DEB", "onDataChange: avail set as " + avail);
                availTextView.setText("lol");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
