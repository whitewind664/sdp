package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.IOException

abstract class VolleyOnlineService(context: Context) : OnlineService<JSONObject> {
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    //TODO: where the do I put the body for the post request???
    override fun post(url: String, body: JSONObject): ResponseListener<JSONObject> {
        val responseListener = VolleyResponseListener<JSONObject>()

        val jsonRequest = JsonObjectRequest(url, body,
                { response -> responseListener.onResponse(response) },
                { throw IOException("Could not send request to url $url") })

        queue.add(jsonRequest)
        return responseListener
    }

    override fun get(url: String): ResponseListener<JSONObject> {
        val responseListener = VolleyResponseListener<JSONObject>()

        val jsonRequest = JsonObjectRequest(url, null,
                { response -> responseListener.onResponse(response) },
                { throw IOException("Could not send request to url $url") })

        queue.add(jsonRequest)
        return responseListener
    }

    override fun delete(url: String): ResponseListener<JSONObject> {
        val responseListener = VolleyResponseListener<JSONObject>()

        val stringRequest = StringRequest(Request.Method.DELETE, url,
            { response -> responseListener.onResponse(JSONObject(response)) },
            { throw IOException("Could not send request to url $url") })

        queue.add(stringRequest)
        return responseListener
    }


}