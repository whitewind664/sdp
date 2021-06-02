package com.github.gogetters.letsgo.game.util.ogs

import org.json.JSONObject
import java.util.*

/**
 * Represents an online service that provides basic communication
 */
interface OnlineService<T> {

    /**
     * Sends the given POST request to the service
     */
    fun post(url: String, body: String, headers: JSONObject = JSONObject()): ResponseListener<T>

    /**
     * Sends the given GET request to the service
     */
    fun get(url: String, headers: JSONObject = JSONObject()): ResponseListener<T>

    /**
     * Sends the given DELETE request to the service
     */
    fun delete(url: String, headers: JSONObject = JSONObject()): ResponseListener<T>


    /**
     * Url encodes parameters as ampersand-separated string
     */
    fun urlEncode(params: JSONObject): String {
        val bodyBuilder = StringJoiner("&")
        for (key in params.keys()) {
            bodyBuilder.add("$key=${params.getString(key)}")
        }
        return bodyBuilder.toString()
    }
}