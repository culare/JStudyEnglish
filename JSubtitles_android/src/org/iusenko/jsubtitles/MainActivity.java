package org.iusenko.jsubtitles;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {
    private FlingController topFlingController;
    private FlingController bottomFlingController;
    private ProgressDialog waitDialog;

    private String topFileName;
    private String bottomFileName;

    private PhraseManagerController topPhraseManagerController;
    private PhraseManagerController bottomPhraseManagerController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        topFileName = Preferences.getTopFilename(this);
        bottomFileName = Preferences.getBottomFilename(this);

        topFlingController = createTopFlingController();
        bottomFlingController = createBottomFlingController();

        topPhraseManagerController = createPhraseManagerController(topFlingController, topFileName);
        bottomPhraseManagerController = createPhraseManagerController(bottomFlingController, bottomFileName);

        setGlobalFlingListener();
        findViewById(R.id.top_load_icon).setOnClickListener(this);
        findViewById(R.id.bottom_load_icon).setOnClickListener(this);

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.lock_panels_button);
        toggleButton.toggle();
        toggleButton.setOnClickListener(this);

         load(topPhraseManagerController, topFileName);
         load(bottomPhraseManagerController, bottomFileName);
    }

    private void load(final PhraseManagerController controller, final String file) {
        if (waitDialog == null || !waitDialog.isShowing()) {
            waitDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        }

        new Thread() {
            @Override
            public void run() {
                controller.load(file);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lock_panels_button) {
            clickOnLock();
        }
        if (v.getId() == R.id.top_load_icon) {
            showDialog(topPhraseManagerController);
        }
        if (v.getId() == R.id.bottom_load_icon) {
            showDialog(bottomPhraseManagerController);
        }
    }

    private void clickOnLock() {
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.lock_panels_button);
        if (toggleButton.isChecked()) {
            setGlobalFlingListener();
        } else {
            setSeparateFlingListeners();
        }
    }

    private void setGlobalFlingListener() {
        ViewOnTouchListener onTouchListener = new ViewOnTouchListener(topFlingController, bottomFlingController);
        findViewById(R.id.main_layout).setOnTouchListener(onTouchListener);
        findViewById(R.id.top_layout).setOnTouchListener(null);
        findViewById(R.id.bottom_layout).setOnTouchListener(null);
    }

    private void setSeparateFlingListeners() {
        findViewById(R.id.main_layout).setOnTouchListener(null);
        findViewById(R.id.top_layout).setOnTouchListener(new ViewOnTouchListener(topFlingController));
        findViewById(R.id.bottom_layout).setOnTouchListener(new ViewOnTouchListener(bottomFlingController));
    }

    private FlingController createTopFlingController() {
        TextView topTextView = (TextView) findViewById(R.id.top_text_view);
        TextView topTimestampTextView = (TextView) findViewById(R.id.top_timestamp_view);
        return new FlingController(topTextView, topTimestampTextView);
    }

    private FlingController createBottomFlingController() {
        TextView bottomTextView = (TextView) findViewById(R.id.bottom_text_view);
        TextView bottomTimestampTextView = (TextView) findViewById(R.id.bottom_timestamp_view);
        return new FlingController(bottomTextView, bottomTimestampTextView);
    }

    private PhraseManagerController createPhraseManagerController(final FlingController controller, String filename) {
        return new PhraseManagerController(this, new PhraseManagerController.ILoaderNotifier() {

            @Override
            public void done(PhraseManager phraseManager) {
                controller.setPhraseManager(phraseManager);
                closeWaitDialog();
            }
        });
    }

    private void closeWaitDialog() {
        if (topFlingController.getPhraseManager() != null && bottomFlingController.getPhraseManager() != null) {
            waitDialog.cancel();
        }
    }

    @Override
    public void finish() {
        // topPhraseManagerController.persist();
        // bottomPhraseManagerController.persist();
        Preferences.saveTopFileName(this, topFileName);
        Preferences.saveBottomFileName(this, bottomFileName);
        super.finish();
    }

    private void showDialog(final PhraseManagerController controller) {
        final String[] items = getFileList();
        if (items.length == 0) {
            return;
        }
        int selected = getItemIndex(items, controller.getFile());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a file");
        builder.setIcon(getResources().getDrawable(R.drawable.open));
        builder.setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                load(controller, items[item].toString());
            }
        });        
        builder.create().show();
    }

    private int getItemIndex(String[] items, String item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    private String[] getFileList() {
        try {
            String[] items = getAssets().list("");
            Set<String> set = new TreeSet<String>();
            for (int i = 0; items != null && i < items.length; i++) {
                if (items[i].toLowerCase().endsWith(".srt")) {
                    set.add(items[i]);
                }
            }
            return set.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }
}