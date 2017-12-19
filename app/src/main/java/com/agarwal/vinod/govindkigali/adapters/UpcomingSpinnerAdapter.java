package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by anirudh on 20/12/17.
 */

public class UpcomingSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    public static final ArrayList<String> months = new ArrayList<>(Arrays.asList("January" , "February" , "March" , "April", "May",
            "June", "July", "August", "September", "October",
            "November", "December"));

    public UpcomingSpinnerAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return months.size();
    }

    @Override
    public Object getItem(int i) {
        return months.get(i);
    }

    @Override
    public long getItemId(int i) {
        return months.indexOf(months.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = new TextView(context);
        }

        TextView tv = (TextView) view;
        tv.setText(months.get(i));
        tv.setPadding(70,50,70,50);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setTextColor(context.getResources().getColor(android.R.color.white));
        tv.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        return tv;
    }
}
