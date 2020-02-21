package com.khalti.signal

import com.khalti.utils.EmptyUtil

class CompositeSignal {

    private val compositeSignals = ArrayList<Signal<*>>()

    public fun add(signal: Signal<*>) {
        compositeSignals.add(signal)
    }

    public fun clear() {
        if (EmptyUtil.isNotEmpty(compositeSignals)) {
            for (signal: Signal<*> in compositeSignals) {
                if (EmptyUtil.isNotNull(signal)) {
                    signal.disconnect()
                }
            }
        }
    }
}