package com.github.gogetters.letsgo.game.util.ogs

/**
 * Represents an online service that provides basic communication
 */
interface OnlineService {

    /**
     * Sends the given request to the service
     */
    fun sendRequest(url: String, header: String, body: String)

    /**
     * Function that is called when a request is answered
     */
    fun onRequestResult(result: String)
}