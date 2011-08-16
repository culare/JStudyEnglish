package org.iusenko.jsubtitles;

import android.os.Handler;
import android.widget.TextView;

public class FlingController {

    private TextView textView;
    private TextView timestampView;
    private PhraseManager phraseManager;
    private Handler uiHandler = new Handler();

    public FlingController(TextView textView, TextView timestampView) {
        this.textView = textView;
        this.timestampView = timestampView;
    }

    public void left2right() {
        if (phraseManager != null) {
            updateUI(phraseManager.prev());
        }
    }

    public void right2left() {
        if (phraseManager != null) {
            updateUI(phraseManager.next());
        }
    }

    private void updateUI(Phrase phrase) {
        textView.setText(phrase.getText());
        timestampView.setText("%s - %s".format(phrase.getFromTime(), phrase.getToTime()));
    }

    public PhraseManager getPhraseManager() {
        return phraseManager;
    }

    public void setPhraseManager(final PhraseManager phraseManager) {
        this.phraseManager = phraseManager;
        uiHandler.post(new Runnable() {

            @Override
            public void run() {
                Phrase current = phraseManager.current();
                updateUI(current);
            }
        });
    }
}
