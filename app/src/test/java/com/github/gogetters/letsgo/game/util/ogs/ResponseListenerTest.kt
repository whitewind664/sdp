package com.github.gogetters.letsgo.game.util.ogs

import junit.framework.Assert.assertEquals
import org.junit.Test

class ResponseListenerTest {

    @Test
    fun setFunctionIsExecuted() {
        var i = 0
        val r = ResponseListener<Int>()
        r.setOnResponse { a -> i += a }
        r.onResponse(1)
        assertEquals(1, i)
    }

    @Test
    fun worksIfFunctionIsSetLate() {
        var i = 0
        val r = ResponseListener<Int>()
        r.onResponse(1)
        r.setOnResponse { a -> i += a }
        assertEquals(1, i)
    }
}