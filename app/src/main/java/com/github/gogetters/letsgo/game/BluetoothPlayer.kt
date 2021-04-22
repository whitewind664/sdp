package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.util.BluetoothDelegate

class BluetoothPlayer(bluetoothDelegate: BluetoothDelegate): Player by bluetoothDelegate