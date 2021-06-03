package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.util.ogs.*
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import kotlin.random.Random

class MockOnlineService : OnlineService<JSONObject> {
    var hasAuthenticated = false
    lateinit var username: String
    lateinit var password: String
    val challengeList = mutableListOf<String>()
    var currentGames = mutableListOf<String>()
    var madeMove = false
    lateinit var lastMove: Point


    private val base = "https://online-go.com"
    private val auth = "/oauth2/token/"
    private val login = "/api/v0/login"
    private val myChallenges = "/v1/me/challenges/"
    //private val challenges = "/v1/challenges"
    private val challenges = "/api/v1/players/800307/challenge/"
    private val myGames = "/v1/me/games/"
    private val games = "/v1/games"


    override fun get(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            else -> throw IllegalArgumentException("should never send a get request??")
        }
    }

    override fun delete(url: String, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        val response = JSONObject()
        response.put("success", "Challenge removed")
        listener.onResponse(response)
        return listener
    }

    override fun post(url: String, body: String, headers: JSONObject): ResponseListener<JSONObject> {
        val listener = ResponseListener<JSONObject>()
        when {
            url.startsWith("$base$login") -> {
                val bodyJSON = urlDecode(body)
                username = bodyJSON.getString("username")
                password = bodyJSON.getString("password")
                val response = JSONObject()
                val user = JSONObject()
                user.put("username", username)
                user.put("id", Random.nextInt(0, 65535).toString())
                response.put("user", user)


                val cookie = HttpCookie("Cookie", "sessionid=asffvnaso9nvf83vn2vna; csrftoken=djfna9sdufcas89fc9as")
                val manager = CookieManager()
                manager.cookieStore.add(URI(base), cookie)
                CookieManager.setDefault(manager)
                val test = CookieManager.getDefault().get(URI(base), mapOf())

                listener.onResponse(response)
            }

            url.startsWith("$base$auth") -> {

                hasAuthenticated = true
                val bodyJSON = urlDecode(body)
                username = bodyJSON.getString("username")
                password = bodyJSON.getString("password")
                val response = JSONObject()
                response.put("access_token", "asdfszdv7sfv9asvnfasnvas7af7ds")
                response.put("refresh_token", "as9d8fsdfjasdcnsdcnsa98a9sg8nasg")
                listener.onResponse(response)
            }
            url.startsWith("$base$challenges") -> {
                val challenge = JSONObject(body)
                val game = challenge.getJSONObject("game")
                val response = JSONObject()
                response.put("status", "ok")
                response.put("challenge", Random.nextInt(0, 65535).toString())
                response.put("game", Random.nextInt(0, 65535).toString())


                challengeList.add(response.getString("challenge"))
                currentGames.add(response.getString("game"))

                listener.onResponse(response)
            }
            url.startsWith("$base$games") -> {
                val response = JSONObject()
                val move = Point.fromSGF(JSONObject(body).getString("move"))
                lastMove = move
                response.put("success", "Move Accepted")
                listener.onResponse(response)
            }
            else -> throw IllegalArgumentException("invalid API request, url does not match: $url")
        }

        return listener
    }

    private fun urlDecode(string: String): JSONObject {
        val params = JSONObject()

        val pairs = string.split("&")
        for (pair in pairs) {
            val middle = pair.indexOf('=')
            val key = pair.subSequence(0, middle)
            val value = pair.subSequence(middle + 1, pair.length)

            params.put(key.toString(), value.toString())
        }

        return params
    }
}