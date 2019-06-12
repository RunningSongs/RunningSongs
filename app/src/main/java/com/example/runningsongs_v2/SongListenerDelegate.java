package com.example.runningsongs_v2;

/**  Interfejs definiujący metodę wywoływaną przy odebraniu utworu
 *
 */

public interface SongListenerDelegate {
    void onSongReceived(Song song);
}
