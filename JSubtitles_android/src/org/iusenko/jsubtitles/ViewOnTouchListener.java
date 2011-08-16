package org.iusenko.jsubtitles;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ViewOnTouchListener implements OnTouchListener {

    private GestureDetector gdt = new GestureDetector(new GestureListener());
    private FlingController[] listeners;

    public ViewOnTouchListener(FlingController... listeners) {
        if (listeners == null) {
            throw new IllegalArgumentException();
        }
        this.listeners = listeners;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gdt.onTouchEvent(event);
        return true;
    }

    private class GestureListener extends SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                right2left();// Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                left2right();// Left to right
            }

            // if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE &&
            // Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            // return false; // Bottom to top
            // } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE &&
            // Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            // return false; // Top to bottom
            // }
            return false;
        }
    }

    private void right2left() {
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].right2left();
        }
    }

    private void left2right() {
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].left2right();
        }
    }

}
