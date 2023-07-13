package com.example.trp.data

import android.util.Base64
import org.json.JSONObject
import java.nio.charset.Charset

class JWTDecoder {
    fun decodeToken(token: String): JSONObject? {
        val parts = token.split('.')
        if (parts.size == 3) {
            val encodedPayload = parts[1]
            val payloadJson = decodeBase64(encodedPayload)
            return JSONObject(payloadJson)
        }
        return null
    }

    private fun decodeBase64(data: String): String {
        val decodedBytes = Base64.decode(data, Base64.DEFAULT)
        return decodedBytes.toString(Charset.defaultCharset())
    }
}

