package com.github.gogetters.letsgo.util

import android.os.Handler
import android.os.Looper


class BluetoothPingService: BluetoothService() {

    private val MESSAGE_READ: Int = 0
    private val MESSAGE_WRITE: Int = 1
    private val MESSAGE_TOAST: Int = 2

    private val TAG = "MY_APP_DEBUG_TAG"
    private val PING = "PING"

    var receivedPing = false

    override val handler = Handler (Looper.getMainLooper()) {
        receivedPing = String(it.obj as ByteArray, charset("utf-8")) == PING || receivedPing
        receivedPing
    }
}