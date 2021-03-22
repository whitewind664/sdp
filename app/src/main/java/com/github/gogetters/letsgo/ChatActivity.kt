package com.github.gogetters.letsgo

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class ChatActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Enables Always-on
        setAmbientEnabled()
    }
}