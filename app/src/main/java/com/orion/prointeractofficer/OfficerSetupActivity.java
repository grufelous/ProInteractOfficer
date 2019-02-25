package com.orion.prointeractofficer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OfficerSetupActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    protected void updateOfficerInfo(View view) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_setup_layout);
        Intent userUidIntent = getIntent();
        Log.d("UIDUser", "onCreate: " + userUidIntent.getStringExtra("uid"));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d("UIDUser", "onCreate: User logged in has UID: " + user.getUid());

    }
}
