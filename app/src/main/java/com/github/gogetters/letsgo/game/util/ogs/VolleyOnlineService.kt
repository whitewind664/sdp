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


    override fun post(url: String, body: Map<String, String>, headers: Map<String, String>): ResponseListener<String> {
        return sendRequest(Request.Method.POST, url, body, headers)
    }

    override fun get(url: String, headers: Map<String, String>): ResponseListener<String> {
        return sendRequest(Request.Method.GET, url, mapOf(), headers)
    }

    override fun delete(url: String, headers: Map<String, String>): ResponseListener<String> {
        return sendRequest(Request.Method.DELETE, url, mapOf(), headers)
    }

    private fun sendRequest(type: Int, url: String, body: Map<String, String>, headers: Map<String, String>): ResponseListener<String> {
        val listener = ResponseListener<String>()
        val stringRequest = object: StringRequest (type, url, {listener.onResponse(it)}, {throw it}) {
            override fun getParams(): MutableMap<String, String> {
                return body.toMutableMap()
            }

            override fun getHeaders(): MutableMap<String, String> {
                return headers.toMutableMap()
            }
        }

        queue.add(stringRequest)
        return listener
    }
}