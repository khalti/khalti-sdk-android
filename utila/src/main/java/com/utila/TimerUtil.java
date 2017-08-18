package com.utila;

import android.os.Handler;

public class TimerUtil {
    public static void delayedTask(Integer time, Task task) {
        new Handler().postDelayed(task::performTask, time);
    }

    public interface Task {
        void performTask();
    }
}
