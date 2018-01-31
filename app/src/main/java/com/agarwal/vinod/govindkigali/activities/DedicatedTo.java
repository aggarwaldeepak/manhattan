package com.agarwal.vinod.govindkigali.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;

public class DedicatedTo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedicated_to);
        setTitle(getString(R.string.dedicated_to));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView)findViewById(R.id.id_GovindKiGali)).setText("~~" + getString(R.string.app_name) + "~~");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
