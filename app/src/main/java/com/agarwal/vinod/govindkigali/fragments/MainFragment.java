package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.api.SongService;
import com.agarwal.vinod.govindkigali.models.Result;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lapism.searchview.SearchView.Version.MENU_ITEM;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public RecyclerView rvPlayList1, rvPlayList2, rvPlayList3;
    TextView tv1, tv2, tv3;
    public static final String TAG = "MAIN";
    private static ArrayList<Song> songlist = new ArrayList<>();
    SongAdapter adapter1, adapter2, adapter3;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        //setUpSearch();

        rvPlayList1 = mainFragment.findViewById(R.id.rv_playlist_1);
        rvPlayList2 = mainFragment.findViewById(R.id.rv_playlist_2);
        rvPlayList3 = mainFragment.findViewById(R.id.rv_playlist_3);
        tv1 = mainFragment.findViewById(R.id.tv_see_all_1);
        tv2 = mainFragment.findViewById(R.id.tv_see_all_2);
        tv3 = mainFragment.findViewById(R.id.tv_see_all_3);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPlayList1.setLayoutManager(layoutManager1);
        rvPlayList2.setLayoutManager(layoutManager2);
        rvPlayList3.setLayoutManager(layoutManager3);
        adapter1 = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity(), SongAdapter.HORIZONTAL);
        adapter2 = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity(), SongAdapter.HORIZONTAL);
        adapter3 = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity(), SongAdapter.HORIZONTAL);
        rvPlayList1.setAdapter(adapter1);
        rvPlayList2.setAdapter(adapter2);
        rvPlayList3.setAdapter(adapter3);
//        rvPlayList1.setItemAnimator(new DefaultItemAnimator() {
//            @Override
//            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
//
//            }
//        });

        if (songlist.size() == 0) {
            Log.d(TAG, "onCreateView: ======== Empty");
            SongService.getSongApi().getTracks().enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Result result = response.body();
                    songlist = result.getSongs();
                    Log.d(TAG, "onResponse: COMPLETED");
                    setTracksToAdapter(songlist);
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                    Log.d(TAG, "onFailure: FAILED");
                }
            });
        } else {
            setTracksToAdapter(songlist);
        }

        return mainFragment;
    }

    /*private void setUpSearch() {
        com.lapism.searchview.SearchView searchView = getActivity().findViewById(R.id.searchView);
        if(searchView == null)return;
        searchView.setVersion(MENU_ITEM);
        searchView.open(true);
    }*/

    private void setTracksToAdapter(ArrayList<Song> body) {

        Log.d(TAG, "setTracksToAdapter: " + body);
        ArrayList<Song> sankirtan = new ArrayList<>();
        ArrayList<Song> recent = new ArrayList<>();
        ArrayList<Song> popular = new ArrayList<>();
//
//        int j = 0;
//        int cnt = body.size() / 3;
//        for (int i = 0; i < 2; ++i) {
//            while (j < cnt) {
//                if (i == 0) {
//                    sankirtan.add(body.get(j));
//                } else {
//                    recent.add(body.get(j));
//                }
//                ++j;
//            }
//            cnt += cnt;
//        }
//        while (j < body.size()) {
//            popular.add(body.get(j));
//            ++j;
//        }

        sankirtan = body;
        recent = body;
        popular = body;

        adapter1.updateTracks(sankirtan);
        adapter2.updateTracks(recent);
        adapter3.updateTracks(popular);
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setUpSearch(body);
        }
    }

//    public void setSongAdapterFilter(String text){
//        adapter.filter(text);
//    }
}
