package com.github.gogetters.letsgo.game.util.ogs

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

abstract class VolleyOnlineService(context: Context) : OnlineService {
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    //TODO: where the do I put the body for the post request???
    override fun post(url: String, body: String): OnlineService.ResponseListener {
        val responseListener: OnlineService.ResponseListener = VolleyResponseListener()

        val stringRequest = StringRequest(Request.Method.POST, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url") })

        queue.add(stringRequest)
        return responseListener
    }

    override fun get(url: String): OnlineService.ResponseListener {
        val responseListener: OnlineService.ResponseListener = VolleyResponseListener()

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url") })

        queue.add(stringRequest)
        return responseListener
    }

    override fun delete(url: String): OnlineService.ResponseListener {
        val responseListener: OnlineService.ResponseListener = VolleyResponseListener()

        val stringRequest = StringRequest(Request.Method.DELETE, url,
            { response -> responseListener.onResponse(response) },
            { throw IOException("Could not send request to url $url") })

        queue.add(stringRequest)
        return responseListener
    }

    /**
     * Listens to respond to http request
     */
    class VolleyResponseListener : OnlineService.ResponseListener {
        private var action: (String) -> Unit = {}
        override fun setOnResponse(action: (String) -> Unit) {
            this.action = action
        }

        override fun onResponse(response: String) {
            action(response)
        }
    }
}