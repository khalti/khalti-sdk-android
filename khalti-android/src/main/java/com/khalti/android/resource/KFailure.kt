/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.resource

abstract class KFailure(message: String?, cause: Throwable?, val code: Number? = null) :
    Exception(message, cause) {

    class NoNetwork(message: String? = null, cause: Throwable? = null) : KFailure(message, cause)

    class ServerUnreachable(message: String? = null, cause: Throwable? = null) :
        KFailure(message, cause)

    class HttpCall(message: String? = null, cause: Throwable? = null, code: Number?) :
        KFailure(message, cause, code)

    class Payment(message: String? = null, cause: Throwable? = null) : KFailure(message, cause)

    class Generic(message: String? = null, cause: Throwable? = null) : KFailure(message, cause)
}