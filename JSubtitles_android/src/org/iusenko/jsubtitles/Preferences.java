package org.iusenko.jsubtitles;

import android.app.Activity;
import android.content.SharedPreferences;

public class Preferences {
    public static String TOP_FILENAME = "topfilename";
    public static String BOTTOM_FILENAME = "topfilename";
    private static final String PREFS_NAME = "JSubtitles";

    public static String getTopFilename(Activity activity) {
        return getString(activity, TOP_FILENAME, "snatch.ru.srt");
    }

    public static String getBottomFilename(Activity activity) {
        return getString(activity, BOTTOM_FILENAME, "snatch.en.srt");
    }

    public static void saveTopFileName(Activity activity, String value) {
        save(activity, TOP_FILENAME, value);
    }

    public static void saveBottomFileName(Activity activity, String value) {
        save(activity, BOTTOM_FILENAME, value);
    }

    private static void save(Activity activity, String key, String value) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
    }

    private static String getString(Activity activity, String key, String defaultValue) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(key, defaultValue);
    }
}
