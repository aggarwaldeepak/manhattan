package com.agarwal.vinod.govindkigali.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.agarwal.vinod.govindkigali.R;

public class OpenSourceLibraries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_libraries);
        setTitle("Open Source Libraries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
