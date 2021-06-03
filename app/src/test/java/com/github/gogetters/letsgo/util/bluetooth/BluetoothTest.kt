package com.github.gogetters.letsgo.util.bluetooth

import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.util.BluetoothGTPService
import com.github.gogetters.letsgo.util.bluetooth.mocking.MockBluetoothService
import org.junit.Assert.assertTrue
import org.junit.Test

class BluetoothTest {
    @Test
    fun pingCommandSetsReceivedPing() {

        val underlying = MockBluetoothService()
        val service = BluetoothGTPService(underlying)
        underlying.receivePing()
        assertTrue(service.receivedPing)
    }

    @Test
    fun pingCommandSendsPing() {
        val underlying = MockBluetoothService()
        val service = BluetoothGTPService(underlying)
        service.ping()

        assertTrue(underlying.sentPing)
    }

    @Test
    fun receivingPlayCommandUpdatesDelegate() {
        val underlying = MockBluetoothService()
        val service = BluetoothGTPService(underlying)

        val delegate = InputDelegate()
        service.inputDelegate = delegate

        underlying.receivePlay()
        assertTrue(delegate.hasInput())
    }

}