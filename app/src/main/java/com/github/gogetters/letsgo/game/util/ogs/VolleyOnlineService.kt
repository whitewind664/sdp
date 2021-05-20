package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.IOException

class VolleyOnlineService(context: Context) : OnlineService<JSONObject> {
    private val queue: RequestQueue = Volley.newRequestQueue(context)


    override fun post(url: String, body: JSONObject, headers: JSONObject): ResponseListener<JSONObject> {
        val responseListener = ResponseListener<JSONObject>()

        val jsonRequest = object : JsonObjectRequest(url, body,
                { response -> responseListener.onResponse(response) },
                { throw it }) {
            override fun getHeaders(): MutableMap<String, String> {
                val map = mutableMapOf<String, String>()
                for (key in headers.keys()) {
                    map[key] = headers.getString(key)
                }
                return map
            }
        }

        queue.add(jsonRequest)
        return responseListener
    }

    override fun get(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val responseListener = ResponseListener<JSONObject>()

        val jsonRequest = object: JsonObjectRequest(url, null,
                { response -> responseListener.onResponse(response) },
                { throw it }) {
            override fun getHeaders(): MutableMap<String, String> {
                val map = mutableMapOf<String, String>()
                for (key in headers.keys()) {
                    map[key] = headers.getString(key)
                }
                return map
            }
        }

        queue.add(jsonRequest)
        return responseListener
    }


    override fun delete(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val responseListener = ResponseListener<JSONObject>()

        val stringRequest = object: StringRequest(Method.DELETE, url,
                { response -> responseListener.onResponse(JSONObject(response)) },
                { throw it }) {
            override fun getHeaders(): MutableMap<String, String> {
                val map = mutableMapOf<String, String>()
                for (key in headers.keys()) {
                    map[key] = headers.getString(key)
                }
                return map
            }
        }

        queue.add(stringRequest)
        return responseListener
    }
}