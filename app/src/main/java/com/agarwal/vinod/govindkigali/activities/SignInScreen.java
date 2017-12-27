package com.agarwal.vinod.govindkigali.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.agarwal.vinod.govindkigali.R;

public class SignInScreen extends AppCompatActivity {


    private com.google.android.gms.common.SignInButton signInButton;
    Button mobile_registration_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
        mobile_registration_btn = findViewById(R.id.MobileRegistration_Button);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this,AuthenticationScreen.class);
                startActivity(intent);
            }
        });

        mobile_registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInScreen.this,MobileAuthenticationScreen.class);
                startActivity(intent);
            }
        });
    }
}
