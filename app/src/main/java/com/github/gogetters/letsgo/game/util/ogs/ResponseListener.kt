package com.github.gogetters.letsgo.game.util.ogs

/**
 * Listens to respond to http request
 */
class ResponseListener<T> {
    private var action: (T) -> Unit = {}
    private var response: T? = null

    fun setOnResponse(action: (T) -> Unit) {
        this.action = action
        if (response != null) {
            action(response!!)
        }
    }

    fun onResponse(response: T) {
        this.response = response
        action(response)
    }
}