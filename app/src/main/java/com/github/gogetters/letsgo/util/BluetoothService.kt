package com.github.gogetters.letsgo.util

import android.bluetooth.BluetoothSocket
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.user.FirebaseUserBundle

interface BluetoothService {
    var handleInput: (ByteArray) -> Boolean

    var receivedPing: Boolean

    fun write(bytes: ByteArray)

    fun connect(socket: BluetoothSocket)

    fun close()

    fun ping() {
        write(PING.toByteArray())
    }

    fun sendNick(){
        val user = Authentication.getCurrentUser()
        if(user != null){
            val letsGoUser = FirebaseUserBundle(user).getUser()
            val task = letsGoUser.downloadUserData()

            while(!task.isComplete){}

            if (letsGoUser.nick != null && letsGoUser.nick!!.isNotEmpty())
                write(letsGoUser.nick!!.toByteArray())
            else
                write("Let's Go Player".toByteArray())
        } else
            write("Let's Go Player".toByteArray())
    }

    companion object {
        internal const val MESSAGE_READ: Int = 0
        internal const val MESSAGE_WRITE: Int = 1
        internal const val MESSAGE_TOAST: Int = 2
        internal const val PING = "PING"
        internal const val TAG = "Bluetooth Service"
    }

}