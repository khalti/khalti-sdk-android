/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

@Suppress("UNCHECKED_CAST")
class Store private constructor() {

    companion object {
        @Volatile
        private var instance: Store? = null

        fun instance(): Store {
            return instance ?: synchronized(this) {
                instance ?: Store().also { instance = it }
            }
        }
    }

    private val cache = HashMap<String, Any>()

    fun put(key: String, value: Any) {
        cache[key] = value
    }

    fun <T> get(key: String): T? {
        return cache[key] as T?
    }
}