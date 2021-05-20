package com.github.gogetters.letsgo.game.util.ogs

/**
 * Represents an online service that provides basic communication
 */
interface OnlineService {

    /**
     * Sends the given POST request to the service
     */
    fun post(url: String, body: String): ResponseListener

    /**
     * Sends the given GET request to the service
     */
    fun get(url: String): ResponseListener

    /**
     * Sends the given DELETE request to the service
     */
    fun delete(url: String): ResponseListener


    interface ResponseListener {
        /**
         * Sets the function to call on response
         */
        fun setOnResponse(action: (String) -> Unit)

        /**
         * Function that is called when a response is received
         */
        fun onResponse(response: String)
    }
}