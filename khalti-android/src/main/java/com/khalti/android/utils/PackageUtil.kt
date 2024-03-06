/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class PackageUtil {
    companion object {
        fun doesPackageExist(context: Context, packageName: String) {
            getPackageInfo(context, packageName) != null
        }

        fun getPackageInfo(context: Context, packageName: String): PackageInfo? {
            return try {
                context.packageManager.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                return null
            }
        }
    }
}