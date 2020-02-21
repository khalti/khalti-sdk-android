package com.khalti.signal

class Signal<T> {

    private val callbacks = mutableListOf<(T) -> Unit>()
    private var shouldEmit = true

    fun connect(slot: (T) -> Unit): Signal<T> {
        callbacks.add(slot)
        return this
    }

    fun disconnect() {
        shouldEmit = false
    }

    fun emit(value: T) {
        if (shouldEmit) {
            callbacks.forEach {
                it(value)
            }
        }
    }
}