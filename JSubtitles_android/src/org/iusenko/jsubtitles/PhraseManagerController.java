package org.iusenko.jsubtitles;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class PhraseManagerController extends Thread {
    private String file;
    private Activity activity;
    private ILoaderNotifier loaderNotifier;
    private PhraseManager phraseManager;

    public PhraseManagerController(Activity activity, ILoaderNotifier loaderNotifier) {
        this.activity = activity;
        this.loaderNotifier = loaderNotifier;

    }

    public void load(String file) {
        if (file.equals(this.file)) {
            return;
        }
        this.file = file;
        // if (isSerialized(file + ".ser")) {
        // phraseManager = deserialize();
        // }

        phraseManager = loadPhraseManagerForFile(file);
        if (phraseManager == null) {
            Log.e(PhraseManager.class.getName(), "manager wasn't loaded for: " + file);
        }
        loaderNotifier.done(phraseManager);
    }

    /*
     * private boolean isSerialized(String fileName){ activity.getAssets(). }
     */

    private PhraseManager loadPhraseManagerForFile(String file) {
        PhraseManager pm = new PhraseManager();
        try {
            InputStream in = activity.getAssets().open(file);
            SubRipParser loader = new SubRipParser(in);
            pm.setPhrases(loader.getPhrases());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pm;
    }

    public interface ILoaderNotifier {
        void done(PhraseManager phraseManager);
    }

    private boolean isSerialized(String fileName) {
        try {
            String[] list = activity.getAssets().list("");
            for (int i = 0; list != null && i < list.length; i++) {
                if (file.equals(list[i])) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private PhraseManager deserialize() {
        try {
            ObjectInputStream in = new ObjectInputStream(activity.getAssets().open(file + ".ser"));
            PhraseManager pm = (PhraseManager) in.readObject();
            in.close();
            return pm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void persist() {
        if (phraseManager == null) {
            return;
        }
        try {
            ObjectOutputStream os = new ObjectOutputStream(activity.openFileOutput(file + ".ser", Context.MODE_PRIVATE));
            os.writeObject(phraseManager);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFile() {
        return file;
    }
}
