package com.example.marblepuzzle;

import android.os.Handler;

public class Timer {
    private Handler handler = new Handler();
    private long startTime = 0L;
    private long pausedTime = 0L;
    private boolean isRunning = false;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);
        }
    };

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            handler.post(timerRunnable);
            isRunning = true;
        }
    }

    public void pause() {
        if (isRunning) {
            handler.removeCallbacks(timerRunnable);
            pausedTime = System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public void resume() {
        if (!isRunning && pausedTime > 0) {
            startTime = System.currentTimeMillis() - pausedTime;
            handler.post(timerRunnable);
            isRunning = true;
        }
    }

    public void reset() {
        handler.removeCallbacks(timerRunnable);
        isRunning = false;
        startTime = 0L;
        pausedTime = 0L;
    }

    public long getElapsedMillis() {
        if (isRunning) {
            return System.currentTimeMillis() - startTime;
        } else {
            return pausedTime;
        }
    }
}
