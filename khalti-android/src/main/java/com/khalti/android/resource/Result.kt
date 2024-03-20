/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.resource

sealed class Result<T : Any, E : KFailure>(private val t: T?, private val e: E?) {

    val isOk: Boolean by lazy {
        this is Ok
    }

    fun match(ok: (T) -> Unit, err: (E) -> Unit) {
        if (isOk) {
            ok(t!!)
        } else {
            err(e!!)
        }
    }
}

class Ok<T : Any, E : KFailure>(t: T) : Result<T, E>(t, null)

class Err<T : Any, E : KFailure>(e: E) : Result<T, E>(null, e)
