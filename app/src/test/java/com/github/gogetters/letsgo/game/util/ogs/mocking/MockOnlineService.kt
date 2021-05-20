package com.github.gogetters.letsgo.game.util.ogs.mocking

import com.github.gogetters.letsgo.game.util.ogs.OnlineService

class MockOnlineService : OnlineService {
    private val sleep_time = 5000L

    override fun post(url: String, body: String): OnlineService.ResponseListener {
        TODO("Not yet implemented")
    }

    override fun get(url: String): OnlineService.ResponseListener {
        TODO("Not yet implemented")
    }

    override fun delete(url: String): OnlineService.ResponseListener {
        TODO("Not yet implemented")
    }

    private fun callOnResponseAfterSomeTime(
        responseListener: OnlineService.ResponseListener,
        response: String
    ) {
        sleep()
        responseListener.onResponse(response)
    }

    private fun sleep() {
        try {
            Thread.sleep(sleep_time)
        } catch (e: InterruptedException) {
            throw RuntimeException("Cannot execute Thread.sleep()")
        }
    }
}