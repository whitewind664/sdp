package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.gogetters.letsgo.R
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar: Toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolBar)
        toolBar.setLogo(R.drawable.logo)
        setupBottomNavigationMenu()
        setupBottomNavigationLabelVisibilityAndHorizontalTranslation()

        val mapButton = findViewById<Button>(R.id.main_button_map)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<Button>(R.id.main_button_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    private fun setupBottomNavigationMenu() {
        //R.id.bottomNavigation.inflateMenu(R.menu.menu_bottom_navigation_2)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item1 -> {
                    val intent = Intent(this, GameActivity::class.java).apply {
                        putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
                        putExtra(GameActivity.EXTRA_KOMI, 5.5)
                    }
                    startActivity(intent)
                    true
                }
                R.id.item2 -> {
                    val intent = Intent(this, TutorialActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item3 -> {
                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Use this to programmatically select navigation items
        //bottomNavigation.selectedItemId = R.id.item1
    }

    private fun setupBottomNavigationLabelVisibilityAndHorizontalTranslation() {
        bottomNavigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_SELECTED
        //bottomNavigation.isItemHorizontalTranslationEnabled = true
    }
}