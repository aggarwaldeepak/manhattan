package com.agarwal.vinod.govindkigali.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.PopListAdapter;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {

    RecyclerView rvPlayList;
    PopListAdapter adapter;
    DatabaseReference listReference = FirebaseDatabase.getInstance().getReference("pop");
    private ArrayList<String> List = new ArrayList<>();
    public static final String TAG = "FAV";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        rvPlayList = findViewById(R.id.rv_fav);

        listReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot popSnapshot : dataSnapshot.getChildren()) {
                    String name = popSnapshot.getKey();
                    List.add(name);
                    Log.d(TAG, "onDataChange: " + popSnapshot.getKey());
                }
                adapter.updateList(List);
                Log.d(TAG, "onDataChange: :)   :)   :)" + dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvPlayList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PopListAdapter(this, new Song(), false);
        rvPlayList.setAdapter(adapter);
    }
}
