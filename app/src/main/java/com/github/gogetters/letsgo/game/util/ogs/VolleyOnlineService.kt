package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException

abstract class VolleyOnlineService(context: Context): OnlineService {
    val queue: RequestQueue = Volley.newRequestQueue(context)

    override var responseListener = OnlineService.ResponseListener { }

    //TODO: where the do I put the body for the post request???
    override fun post(url: String, body: String) {
        val stringRequest = StringRequest(Request.Method.POST, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url")})

        queue.add(stringRequest)
    }

    override fun get(url: String) {
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url")})

        queue.add(stringRequest)
    }

    override fun delete(url: String) {
        val stringRequest = StringRequest(Request.Method.DELETE, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url")})

        queue.add(stringRequest)
    }
}