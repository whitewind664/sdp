package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.net.CookieHandler
import java.net.CookieManager
import java.util.*

class VolleyOnlineService(context: Context) : OnlineService<JSONObject>  {
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    init {
        CookieHandler.setDefault(CookieManager())
    }

    override fun post(url: String, body: String, headers: JSONObject): ResponseListener<JSONObject> { return getOrPost(url, body, headers) }

    override fun get(url: String, headers: JSONObject): ResponseListener<JSONObject> { return getOrPost(url, null, headers) }

    private fun getOrPost(url: String, body: String?, headers: JSONObject): ResponseListener<JSONObject> {

        val responseListener = ResponseListener<JSONObject>()

        val method = if (body != null) Request.Method.POST  else Request.Method.GET

        val request = object: StringRequest(method, url,
                { responseListener.onResponse(JSONObject(it)) },
                {
                    if (it.networkResponse != null && it.networkResponse.data != null) {
                        val response = it.networkResponse.data.decodeToString()
                        Log.d("VOLLEY ERROR", response)
                        responseListener.onResponse(JSONObject().put("error", "error")) //TODO make more expressive

                    } else {
                        Log.d("VOLLEY ERROR", it.toString())
                    }
                })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val map = mutableMapOf<String, String>()
                for (key in headers.keys()) {
                    map[key] = headers.getString(key)
                }

                return map
            }

            override fun getBody(): ByteArray {
                if (body == null) return "".toByteArray()
                return body.toByteArray()
            }
        }

        queue.add(request)
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