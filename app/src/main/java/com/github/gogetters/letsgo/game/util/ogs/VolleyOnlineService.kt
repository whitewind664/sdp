package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VolleyOnlineService(context: Context) : OnlineService<String> {
    private val queue: RequestQueue = Volley.newRequestQueue(context)


    override fun post(url: String, body: String, headers: MutableMap<String, String>): ResponseListener<String> {
        sendRequest(Request.Method.POST, url, body, headers)
    }

    override fun get(url: String, headers: MutableMap<String, String>): ResponseListener<String> {
        TODO("Not yet implemented")
    }

    override fun delete(url: String, headers: MutableMap<String, String>): ResponseListener<String> {
        TODO("Not yet implemented")
    }

    fun sendRequest(type: Int, url: String, body: String, headers: MutableMap<String, String>): ResponseListener<String> {
        val listener = ResponseListener<String>()
        val stringRequest = object: StringRequest (type, url, {listener.onResponse(it)}, {throw it}) {
            override fun getBody(): ByteArray {
                return super.getBody()
            }

            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }

    }

    private fun jsonToMap(json: JSONObject): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        for (key in json.keys()) {
            map[key] = json.getString(key)
        }
        return map
    }
}