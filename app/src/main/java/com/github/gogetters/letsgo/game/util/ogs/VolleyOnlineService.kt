package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VolleyOnlineService(context: Context) : OnlineService<JSONObject>  {
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    override fun post(url: String, body: JSONObject, headers: JSONObject): ResponseListener<JSONObject> { return getOrPost(url, body, headers) }

    override fun get(url: String, headers: JSONObject): ResponseListener<JSONObject> { return getOrPost(url, null, headers) }

    private fun getOrPost(url: String, jsonRequest: JSONObject?, headers: JSONObject): ResponseListener<JSONObject> {

        val responseListener = ResponseListener<JSONObject>()

        val jsonRequest = object: JsonObjectRequest(url, jsonRequest, { response -> responseListener.onResponse(response) }, { throw it }) {
            override fun getHeaders(): MutableMap<String, String> { return jsonToMap(headers) }
        }

        queue.add(jsonRequest)
        return responseListener
    }


    override fun delete(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val responseListener = ResponseListener<JSONObject>()

        val stringRequest = object: StringRequest(Method.DELETE, url, { response -> responseListener.onResponse(JSONObject(response)) }, { throw it }) {
            override fun getHeaders(): MutableMap<String, String> { return jsonToMap(headers) }
        }

        queue.add(stringRequest)
        return responseListener
    }

    private fun jsonToMap(json: JSONObject): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        for (key in json.keys()) {
            map[key] = json.getString(key)
        }
        return map
    }
}