package com.example.myappsample.chipAnimation;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {
    private static final String TAG = ProgressBarAnimation.class.getSimpleName();
    private static final boolean DEBUG = false;

    private final ProgressBar progressBar;
    private float from, to;

    public ProgressBarAnimation(ProgressBar progressBar) {
        super();
        Log.d(TAG, "ProgressBarAnimation() instance create called");
        this.progressBar = progressBar;
    }

    public void resetFromTo(float from, float to) {
        Log.d(TAG, "resetFromTo() from : " + from + ", to : " + to);
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        LogD("applyTransformation() value : " + value);
        progressBar.setProgress((int) value);
    }

    private static void LogD(String log) {
        if (DEBUG) {
            Log.d(TAG, log);
        }
    }
}
