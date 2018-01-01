package com.agarwal.vinod.govindkigali.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInScreen extends AppCompatActivity implements View.OnClickListener {


    private com.google.android.gms.common.SignInButton signInButton;
    Button mobile_registration_btn, logOut_btn, signOut_btn;
    FirebaseAuth auth;
    String TAG = "SignInScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
        mobile_registration_btn = findViewById(R.id.MobileRegistration_Button);
        logOut_btn = findViewById(R.id.log_out_btn);
        signOut_btn = findViewById(R.id.sign_out_button);

        auth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(this);
        mobile_registration_btn.setOnClickListener(this);
        logOut_btn.setOnClickListener(this);
        signOut_btn.setOnClickListener(this);


        PrefManager prefManager = new PrefManager(this);

        if (prefManager.isLoggedInViaGmail()) {
            signInButton.setVisibility(View.INVISIBLE);
            signOut_btn.setVisibility(View.VISIBLE);
        }
        if (prefManager.isRegisteredMobileNumber()) {
            mobile_registration_btn.setVisibility(View.INVISIBLE);
            logOut_btn.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        Log.d(TAG, "onStart:currentUser " + currentUser);

//        if (currentUser == null){
//        signInButton.setOnClickListener(this);
//        mobile_registration_btn.setOnClickListener(this);
//        finish();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        PrefManager prefManager = new PrefManager(this);
        if (id == R.id.sign_in_button) {
            Intent intent = new Intent(SignInScreen.this, AuthenticationScreen.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.MobileRegistration_Button) {
            Intent intent = new Intent(SignInScreen.this, MobileAuthenticationScreen.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.log_out_btn) {
//            auth.signOut();
            prefManager.setMobileNumberRegistered(false);
            logOut_btn.setVisibility(View.INVISIBLE);
            mobile_registration_btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.sign_out_button) {
            Log.d(TAG, "onClick: "+auth.getCurrentUser().getDisplayName());
            Toast.makeText(this, auth.getCurrentUser().getDisplayName()+" Successfully Logged Out", Toast.LENGTH_SHORT).show();
            auth.signOut();
            prefManager.setLoggedInViagmail(false);
            signOut_btn.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }
}
