package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.util.InputDelegate

class LocalPlayer(inputDelegate: InputDelegate): Player by inputDelegate