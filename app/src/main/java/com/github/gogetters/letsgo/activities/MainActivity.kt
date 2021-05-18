package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.game.Player
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapButton = findViewById<Button>(R.id.main_button_map)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<Button>(R.id.main_button_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("UserBundleProvider", FirebaseUserBundleProvider())
            startActivity(intent)
        }

        val btButton = findViewById<Button>(R.id.main_button_bt)
        btButton.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_main;
    }

}