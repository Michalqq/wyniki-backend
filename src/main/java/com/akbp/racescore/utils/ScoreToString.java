package com.akbp.racescore.utils;

public class ScoreToString {

    public static final String toString(Long scoreInMilis) {
        if (scoreInMilis==null) return null;

        int minutes = (int) (scoreInMilis / 60000);

        return minutes == 0 ? getSeconds(scoreInMilis) + "." + getMilis(scoreInMilis)
                :
                minutes + ":" + getSeconds(scoreInMilis) + "." + getMilis(scoreInMilis);
    }

    public static final String getMinutes(Long scoreInMilis) {
        return String.valueOf(scoreInMilis / 60000);
    }

    public static final String getSeconds(Long scoreInMilis) {
        int minutes = (int) (scoreInMilis / 60000);
        int seconds = (int) (scoreInMilis - minutes * 60000) / 1000;
        String sec = String.valueOf(seconds);
        return "00".substring(sec.length(), 2) + sec;
    }

    public static final String getMilis(Long scoreInMilis) {
        int minutes = (int) (scoreInMilis / 60000);
        int seconds = (int) (scoreInMilis - minutes * 60000) / 1000;
        int milis = (int) (scoreInMilis - minutes * 60000 - seconds * 1000) / 10;
        String milisString = String.valueOf(milis);
        return "00".substring(milisString.length(), 2) + milisString;
    }
}
