package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException

abstract class VolleyOnlineService(context: Context): OnlineService {
    val queue: RequestQueue = Volley.newRequestQueue(context)

    override fun sendRequest(url: String, body: String) {
        val stringRequest = StringRequest(Request.Method.POST, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url")})

        queue.add(stringRequest)
    }
}