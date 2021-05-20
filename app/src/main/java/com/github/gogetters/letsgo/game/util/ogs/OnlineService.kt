package com.github.gogetters.letsgo.game.util.ogs

/**
 * Represents an online service that provides basic communication
 */
interface OnlineService {

    /**
     * Sends the given POST request to the service
     */
    fun post(url: String, body: String)

    /**
     * Sends the given GET request to the service
     */
    fun get(url: String)

    /**
     * Sends the given DELETE request to the service
     */
    fun delete(url: String)

    /**
     * Defines the behavior to handle responses
     */
    var responseListener: ResponseListener

    fun interface ResponseListener {
        /**
         * Function that is called when a response is received
         */
        fun onResponse(response: String)
    }
}