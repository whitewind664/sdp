package com.github.gogetters.letsgo.game.util.ogs

import com.android.volley.Response

/**
 * Listens to respond to http request
 */
class ResponseListener<T> : Response.Listener<T> {
    private var action: (T) -> Unit = {}
    private var response: T? = null

    fun setOnResponse(action: (T) -> Unit): Response.Listener<T> {
        this.action = action
        if (response != null) {
            action(response!!)
        }
        return this
    }

    override fun onResponse(response: T) {
        this.response = response
        action(response)
    }
}