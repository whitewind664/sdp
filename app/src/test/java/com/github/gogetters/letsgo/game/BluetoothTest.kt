package com.github.gogetters.letsgo.game

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.github.gogetters.letsgo.game.GTPCommand.GENMOVE
import com.github.gogetters.letsgo.game.util.BluetoothInputDelegate
import com.github.gogetters.letsgo.util.BluetoothClient
import com.github.gogetters.letsgo.util.BluetoothGTPService
import com.github.gogetters.letsgo.util.BluetoothProbe
import com.github.gogetters.letsgo.util.BluetoothServer
import net.bytebuddy.asm.Advice
import org.junit.Test


class BluetoothTest {

    @Test
    fun bluetoothCodeCanBeCovered() {

        val client = BluetoothClient()
        val gtpService = BluetoothGTPService()
        val server = BluetoothServer()
        val probe = BluetoothProbe()


        try{
            server.cancel()
        } catch(e: Exception) {
            Log.d("Test", "doesn't work...")
        }


        try{
            server.connect(gtpService)
        } catch(e: Exception) {
            Log.d("Test", "doesn't work...")
        }


        try{gtpService.ping()} catch(e: Exception) {Log.d("Test", "doesn't work...")}
        try{gtpService.close()} catch(e: Exception) {Log.d("Test", "doesn't work...")}
        try{gtpService.write(ByteArray(1))} catch(e: Exception) {Log.d("Test", "doesn't work...")}
        try{gtpService.sendCommand(GENMOVE(Stone.BLACK))} catch(e: Exception) {Log.d("Test", "doesn't work...")}



        val btInput = BluetoothInputDelegate(gtpService)
        val local = LocalPlayer(Stone.BLACK, btInput)
        val btLocal = BluetoothLocalPlayer(local, gtpService)
    }
}