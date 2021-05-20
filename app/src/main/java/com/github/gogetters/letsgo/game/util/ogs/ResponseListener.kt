package com.github.gogetters.letsgo.game.util.ogs

interface ResponseListener<T> {
    /**
     * Sets the function to call on response
     */
    fun setOnResponse(action: (T) -> Unit)

    /**
     * Function that is called when a response is received
     */
    fun onResponse(response: T)
}