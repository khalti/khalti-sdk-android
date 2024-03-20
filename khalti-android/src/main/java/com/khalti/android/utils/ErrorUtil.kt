/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.Locale


class ErrorUtil {
    companion object {
        const val GENERIC_ERROR = "An error occurred, please try again later"
        private const val UNAVAILABLE_ERROR = "Service temporarily unavailable"
        private const val GENERIC_JSON_ERROR =
            "{\"detail\":\"An error occurred, please try again later\"}"


        fun parseError(json: String, statusCode: String? = null): String {
            var errorJson = ""
            if (errorJson.isEmpty()) {
                errorJson = GENERIC_JSON_ERROR
            }
            val map = HashMap<String, String>()
            if (!statusCode.isNullOrEmpty() && statusCode == "503") {
                map["detail"] = UNAVAILABLE_ERROR
            } else {
                try {
                    val root = JSONObject(errorJson)
                    if (root.has("detail")) {
                        val type: Type = object : TypeToken<HashMap<String?, String?>?>() {}.type
                        if (root.has("meta") && root["meta"].toString() + "" != "{}") {
                            map.putAll(
                                Gson().fromJson(
                                    JsonUtil.convertToJsonString(root["meta"]),
                                    type
                                )
                            )
                            root.remove("meta")
                        }
                        root.remove("error_data") //Remove this if error_data is needed
                        root.remove("meta")
                        map.putAll(Gson().fromJson(root.toString() + "", type))
                    } else {
                        if (root.has("non_field_error")) {
                            map["detail"] =
                                JsonUtil.parseJsonArray(json = root.getString("non_field_error"))
                            root.remove("non_field_error")
                        }
                        if (root.has("error_key")) {
                            map["error_key"] = root.getString("error_key")
                        }
                        val keys: Iterator<*> = root.keys()
                        while (keys.hasNext()) {
                            val currentKey = keys.next() as String
                            if (!currentKey.lowercase(Locale.getDefault())
                                    .contains("status") && !currentKey.lowercase(
                                    Locale.getDefault()
                                ).contains("error_key")
                            ) {
                                map[currentKey] = JsonUtil.parseJsonArray(currentKey, errorJson)
                            }
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (map.isEmpty()) {
                    map["detail"] = Companion.GENERIC_ERROR
                }
            }
            if (!statusCode.isNullOrEmpty()) {
                map["status"] = statusCode
            }
            return JsonUtil.convertToJsonString(map)
        }

        fun parseThrowableError(throwable: String?, statusCode: String?): String {
            val t = (throwable ?: "").lowercase(Locale.getDefault())
            val map = HashMap<String, String?>()
            var error = GENERIC_ERROR

            if (t.contains("timed out")) {
                error =
                    "Looks like you have an unstable network at the moment, please try again when network stabilizes"
            } else if (t.contains("cannot connect") || t.contains("failed to connect")) {
                error =
                    "Looks like the server is taking too long to respond, please try again later"
            }

            if (statusCode != null) {
                map["status"] = statusCode
            }

            map["detail"] = error

            return JsonUtil.convertToJsonString(map)
        }

    }
}