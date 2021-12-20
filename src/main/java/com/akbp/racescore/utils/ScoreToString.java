package com.akbp.racescore.utils;

public class ScoreToString {

    public static final String toString(Long scoreInMilis) {
        int minutes = (int) (scoreInMilis / 60000);
        int seconds = (int) (scoreInMilis - minutes * 60000) / 1000;
        int milis = (int) (scoreInMilis - minutes * 60000 - seconds * 1000) / 10;

        return minutes == 0 ? seconds + "." + milis : minutes + ":" + seconds + "." + milis;
    }
}
