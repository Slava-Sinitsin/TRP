package com.example.trp.data

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.nio.charset.Charset

class JWTDecoder {
    fun decodeToken(token: String): Map<String, Any>? {
        val parts = token.split('.')
        if (parts.size == 3) {
            val encodedPayload = parts[1]
            val payloadJson = decodeBase64(encodedPayload)
            return parsePayload(payloadJson)
        }
        return null
    }

    private fun decodeBase64(data: String): String {
        val decodedBytes = Base64.decode(data, Base64.DEFAULT)
        return decodedBytes.toString(Charset.defaultCharset())
    }

    private fun parsePayload(payloadJson: String): Map<String, Any>? {
        try {
            val jsonObject = Gson().fromJson(payloadJson, JsonObject::class.java)
            val payloadMap = mutableMapOf<String, Any>()
            jsonObject.entrySet().forEach { entry ->
                payloadMap[entry.key] = entry.value
            }
            return payloadMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
