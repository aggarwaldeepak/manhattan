package com.agarwal.vinod.govindkigali.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {

    RecyclerView rvFav;
    SongAdapter adapter;
    DatabaseReference favReference = FirebaseDatabase.getInstance().getReference("fav");
    private ArrayList<Song> favList = new ArrayList<>();
    public static final String TAG = "FAV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        rvFav = findViewById(R.id.rv_fav);

        favReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {
                    Song song = favSnapshot.getValue(Song.class);
                    favList.add(song);
                }
                adapter.updateTracks(favList);
                Log.d(TAG, "onDataChange: :)   :)   :)");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        SongAdapter.PlayerInterface playerInterface = new SongAdapter.PlayerInterface() {
            @Override
            public void playSong(Integer value) {

//                PlayerFragment fragment = new PlayerFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("Value", value);
//                fragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fg_main, fragment)
//                        .commit();
                Toast.makeText(FavActivity.this, "Under Process: : :", Toast.LENGTH_SHORT).show();
            }
        };

        rvFav.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongAdapter(this, playerInterface);
        rvFav.setAdapter(adapter);

    }

}
