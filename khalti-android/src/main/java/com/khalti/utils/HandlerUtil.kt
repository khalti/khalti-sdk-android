package com.khalti.utils

import android.os.Handler

object HandlerUtil {

    fun delayedTask(time: Long, task: () -> Unit) {
        Handler().postDelayed({ task() }, time)
    }
}