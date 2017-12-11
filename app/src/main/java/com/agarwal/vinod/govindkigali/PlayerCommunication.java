package com.agarwal.vinod.govindkigali;


import com.agarwal.vinod.govindkigali.models.Song;

/**
 * Created by Anirudh Gupta on 12/11/2017.
 */

public interface PlayerCommunication {
    void onClosePlayerFragment();
    void onOpenPlayerFragment();
    void playSong(Song song);
}
