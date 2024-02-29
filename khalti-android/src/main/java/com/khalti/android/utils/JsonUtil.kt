/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.utils

import com.google.gson.GsonBuilder
import com.khalti.android.utils.ErrorUtil.Companion.GENERIC_ERROR
import org.json.JSONException
import org.json.JSONObject


class JsonUtil {
    companion object {
        fun convertToJsonString(o: Any): String {
            val gson = GsonBuilder()
                .serializeNulls()
                .create()
            return gson.toJson(o)
        }


        fun parseJsonArray(key: String? = null, json: String): String {
            val stringBuilder = StringBuilder()
            try {
                val jsonObject = JSONObject(json)
                if (key != null) {
                    return parseArray(key, jsonObject)
                }
                for (k in jsonObject.keys()) {
                    stringBuilder.append(parseArray(k, jsonObject))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                return GENERIC_ERROR
            }
            return stringBuilder.toString()
        }

        private fun parseArray(key: String, jsonObject: JSONObject): String {
            val stringBuilder = StringBuilder()
            val jsonArray = jsonObject.getJSONArray(key)
            val responseArray = ArrayList<String>()
            val sep = "\n"

            for (i in 0 until jsonArray.length()) {
                val response = jsonArray.getString(i)
                responseArray.add(response)
            }

            for (i in responseArray.indices) {
                stringBuilder.append(responseArray[i]).append(sep)
            }

            return stringBuilder.toString()
        }
    }
}