package com.github.gogetters.letsgo.game.util.ogs

/**
 * Listens to respond to http request
 */
class VolleyResponseListener<T> : ResponseListener<T> {
    private var action: (T) -> Unit = {}
    private var response: T? = null

    override fun setOnResponse(action: (T) -> Unit) {
        this.action = action
        if (response != null) {
            action(response!!)
        }
    }

    override fun onResponse(response: T) {
        this.response = response
        action(response)
    }
}