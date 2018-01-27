package com.agarwal.vinod.govindkigali.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.DialogListAdapter;
import com.agarwal.vinod.govindkigali.adapters.PopListAdapter;
import com.agarwal.vinod.govindkigali.adapters.SongImageAdapter;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by darsh on 15/12/17.
 */

public class CustomDialogClass extends Dialog implements View.OnClickListener {

    private DialogListAdapter adapter;
    private RecyclerView rvPopPlayList;
    LinearLayout llList;
    private Context context;
    private EditText etListName;
    private TextView btCreate, btCancel, tvNewPlaylist;
    private LinearLayout llCustomDialogBox;
    private Song song;
    private ScreenResolution screenRes;
    private FragmentActivity activity;
    private DatabaseReference popReference = FirebaseDatabase.getInstance().getReference("pop");
    private ArrayList<String> popupList = new ArrayList<>();
    public static final String TAG = "POP";
    public CustomDialogClass(@NonNull Context context, Song song, FragmentActivity activity) {
        super(context);
        this.context = context;
        this.song = song;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);

        rvPopPlayList = findViewById(R.id.rv_popPlaylist);
        llList = findViewById(R.id.ll_createPlaylist);
        tvNewPlaylist = findViewById(R.id.tv_new_playlist);
        etListName = findViewById(R.id.et_list_name);
        btCreate = findViewById(R.id.bt_create);
        btCancel = findViewById(R.id.bt_cancel);
        llCustomDialogBox = findViewById(R.id.ll_custom_dialog_box);

        screenRes = deviceDimensions();
        llCustomDialogBox.getLayoutParams().height = screenRes.height / 2;
        llCustomDialogBox.getLayoutParams().width = screenRes.width - 200;

        etListName.setVisibility(View.GONE);
        btCreate.setVisibility(View.GONE);
        btCancel.setVisibility(View.GONE);
        tvNewPlaylist.setVisibility(View.GONE);

        llList.setOnClickListener(this);
        btCreate.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        popReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                popupList.clear();
                for (DataSnapshot popSnapshot : dataSnapshot.getChildren()) {
                    String name = popSnapshot.getKey();
                    popupList.add(name);
                    Log.d(TAG, "onDataChange: " + popSnapshot.getKey());
                }
                adapter.updateList(popupList);
                Log.d(TAG, "onDataChange: :)   :)   :)" + dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvPopPlayList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DialogListAdapter(context, song, true, this);
        rvPopPlayList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        switch (view.getId()) {
            case R.id.ll_createPlaylist:
                etListName.setVisibility(View.VISIBLE);
                btCreate.setVisibility(View.VISIBLE);
                btCancel.setVisibility(View.VISIBLE);
                rvPopPlayList.setVisibility(View.GONE);
                llList.setVisibility(View.GONE);
                tvNewPlaylist.setVisibility(View.VISIBLE);
                llCustomDialogBox.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                llCustomDialogBox.getLayoutParams().width = screenRes.width - 200;
                break;
            case R.id.bt_create:
                final String id = etListName.getText().toString();
                popReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(id).exists()) {
                            Toast.makeText(context, "PLay List Already Exits", Toast.LENGTH_SHORT).show();
                        } else {
                            popReference.child(etListName.getText().toString()).child(song.getAlbum().getId()).setValue(song);
                            Toast.makeText(context, "Song Added to " + etListName.getText().toString(), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.bt_cancel:
                etListName.setVisibility(View.GONE);
                btCreate.setVisibility(View.GONE);
                btCancel.setVisibility(View.GONE);
                rvPopPlayList.setVisibility(View.VISIBLE);
                llList.setVisibility(View.VISIBLE);
                tvNewPlaylist.setVisibility(View.GONE);
                llCustomDialogBox.getLayoutParams().height = screenRes.height / 2;
                llCustomDialogBox.getLayoutParams().width = screenRes.width - 200;
                break;
        }
    }

    private class ScreenResolution {
        int width;
        int height;
        private ScreenResolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    CustomDialogClass.ScreenResolution deviceDimensions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // getsize() is available from API 13
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new CustomDialogClass.ScreenResolution(size.x, size.y);
        }
        else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            // getWidth() & getHeight() are deprecated
            return new CustomDialogClass.ScreenResolution(display.getWidth(), display.getHeight());
        }
    }
}
