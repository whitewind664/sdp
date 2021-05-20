package com.github.gogetters.letsgo.game.util.ogs

import org.json.JSONObject

/**
 * Represents an online service that provides basic communication
 */
interface OnlineService<T> {

    /**
     * Sends the given POST request to the service
     */
    fun post(url: String, body: JSONObject, headers: JSONObject): ResponseListener<T>

    /**
     * Sends the given GET request to the service
     */
    fun get(url: String, headers: JSONObject): ResponseListener<T>

    /**
     * Sends the given DELETE request to the service
     */
    fun delete(url: String, headers: JSONObject): ResponseListener<T>
}