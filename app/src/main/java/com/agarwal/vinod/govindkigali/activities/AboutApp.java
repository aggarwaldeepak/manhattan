package com.agarwal.vinod.govindkigali.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.agarwal.vinod.govindkigali.R;

public class AboutApp extends AppCompatActivity {


    CardView privacy_policy, terms_n_conditions, open_source_libraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        setTitle("About App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        privacy_policy = findViewById(R.id.id_PrivacyPolicy);
        terms_n_conditions = findViewById(R.id.id_TermsnConditions);
        open_source_libraries = findViewById(R.id.open_source_libraries);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void OnClickPrivacyPolicy(View view) {
        startActivity(new Intent(AboutApp.this, PrivacyPolicy.class));
    }

    public void OnClickOpenSourceLibraries(View view) {
        startActivity(new Intent(AboutApp.this, OpenSourceLibraries.class));
    }

    public void OnClickTermsNConditions(View view) {
        startActivity(new Intent(AboutApp.this, TermsNConditions.class));
    }
}
