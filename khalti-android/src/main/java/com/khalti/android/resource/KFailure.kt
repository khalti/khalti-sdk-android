/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.resource

abstract class KFailure(failureMessage: String, throwable: Throwable? = null, val code: Number? = null) :
    Exception(failureMessage, throwable) {

    class NoNetwork(message: String, cause: Throwable? = null) : KFailure(message, cause)

    class ServerUnreachable(message: String, cause: Throwable? = null) :
        KFailure(message, cause)

    class HttpCall(message: String, cause: Throwable? = null, code: Number?) :
        KFailure(message, cause, code)

    class Payment(message: String, cause: Throwable? = null, code: Number?) : KFailure(message, cause, code)

    class Generic(message: String, cause: Throwable? = null) : KFailure(message, cause)
}