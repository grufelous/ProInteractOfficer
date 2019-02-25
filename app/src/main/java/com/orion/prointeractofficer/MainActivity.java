package com.orion.prointeractofficer;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void launchAuthActivity(View view) {
        Intent authIntent = new Intent(this, OfficerAuthActivity.class);
        Log.d(TAG, "launchAuthActivity: launching next activity");
        startActivity(authIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_layout = findViewById(R.id.main_layout);

        letStartBtn = findViewById(R.id.launchLoginBtn);

        /*if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Snackbar.make(main_layout, "Not logged in", Snackbar.LENGTH_SHORT).show();
            signIn();
        } else {
            String dispName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
            Snackbar.make(main_layout, "Signed in as " + dispName, Snackbar.LENGTH_SHORT).show();
        }*/

    }
}
