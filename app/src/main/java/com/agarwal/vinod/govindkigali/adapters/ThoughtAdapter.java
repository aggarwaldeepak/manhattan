package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by anirudh on 27/12/17.
 */

public class ThoughtAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    DatabaseReference databaseReference;
    ArrayList<String> thoughtList;

    public ThoughtAdapter(Context context) {
        this.context = context;
        thoughtList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("thoughts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    Log.d("THOUGHT", "onDataChange: " + dataSnap.getValue(String.class));
                    thoughtList.add(dataSnap.getValue(String.class));
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getCount() {
        return thoughtList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_thought, container, false);
        TextView tvThoughtText = view.findViewById(R.id.tv_thought_text);
        tvThoughtText.setText(thoughtList.get(position));
        final LinearLayout linearLayout = view.findViewById(R.id.rootBottomLayout);

        FrameLayout frameLayout = view.findViewById(R.id.rootLayoutThought);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (linearLayout.getVisibility()){
                    case View.GONE:
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        linearLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
        container.addView(view);


        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
