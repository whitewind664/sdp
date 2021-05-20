package com.github.gogetters.letsgo.game.util.ogs

/**
 * Represents an online service that provides basic communication
 */
interface OnlineService {

    /**
     * Sends the given request to the service
     */
    fun sendRequest(url: String, body: String)


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