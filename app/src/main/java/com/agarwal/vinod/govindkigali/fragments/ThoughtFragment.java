package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.ThoughtAdapter;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.agarwal.vinod.govindkigali.utils.Util;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThoughtFragment extends Fragment {

    ViewPager viewPager;

    public ThoughtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_thought, container, false);

        try{
            getActivity().setTitle(
                    Util.getLocalizedResources(getContext(),
                            new Locale(new PrefManager(getContext()).getUserLanguage()))
                            .getString(R.string.thought_frag_title)
            );
        } catch (Exception e){
            getActivity().setTitle(getString(R.string.app_name));
        }

       viewPager = view.findViewById(R.id.viewPagerThought);
       viewPager.setAdapter(new ThoughtAdapter(getContext()));
       return view;
    }

}
