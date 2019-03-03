package com.orion.prointeractofficer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout main_layout;
    private static final int RC_SIGN_IN = 9000;
    /*private void signIn() {
        List<String> whitelistedCountries = new ArrayList<String>();
        whitelistedCountries.add("+91");
        AuthUI.IdpConfig phoneBuilderIndia = new AuthUI.IdpConfig.PhoneBuilder()
                .setWhitelistedCountries(whitelistedCountries)
                .build();
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                phoneBuilderIndia,
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                //move to another activity file
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN
        );
    }*/
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("RESULT", "onActivityResult: called");    //not called? Why?
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("SIGNIN", "onActivityResult: Signed in");
                Snackbar.make(main_layout, "Signed in!!!", Snackbar.LENGTH_INDEFINITE);
            } else {
                Snackbar.make(main_layout, "Sign in cancelled", Snackbar.LENGTH_INDEFINITE);
            }
        }
    }*/
    private static String TAG = "Main";
    Button letStartBtn;
    TextView listOfUsers;
    public void launchAuthActivity(View view) {
        Intent authIntent = new Intent(this, OfficerAuthActivity.class);
        Log.d(TAG, "launchAuthActivity: launching next activity");
        startActivity(authIntent);
    }
    public void launchAuthActivity() {
        Intent authIntent = new Intent(this, OfficerAuthActivity.class);
        Log.d(TAG, "launchAuthActivity: launching next activity");
        startActivity(authIntent);
    }
    private FirebaseAuth mAuth;
    private DatabaseReference db;

    private void readUserList(Map<String, Object> users) {
        for(Map.Entry<String, Object> e: users.entrySet()) {
            Map user = (Map) e.getValue();
            Log.d("USER", "readUserList: " + user.get("about").toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent geoFenceIntent = new Intent(this, GeofenceActivity.class);
        //Log.d(TAG, "launchSetup: launching geof activity");
        startActivity(geoFenceIntent);

        /*main_layout = findViewById(R.id.main_layout);

        letStartBtn = findViewById(R.id.launchLoginBtn);
        listOfUsers = findViewById(R.id.listOfUsers);

        db = FirebaseDatabase.getInstance().getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("READ", "onDataChange: " + dataSnapshot.child("officer").getValue());
                //readUserList((Map<String, Object>) dataSnapshot.child("officer").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("FBDB", "onCreate: " + db.child("officer").toString());
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            //Snackbar.make(main_layout, "Not logged in", Snackbar.LENGTH_SHORT).show();
            //signIn();
            launchAuthActivity();

        } else {
            //String dispName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
            //Snackbar.make(main_layout, "Signed in as " + dispName, Snackbar.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "onCreate: " + user.getDisplayName());
            Intent authIntent = new Intent(this, OfficerSetupActivity.class);
            Log.d(TAG, "launchSetup: launching next activity");

            startActivity(authIntent);
        }*/

    }
}
