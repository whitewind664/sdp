package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.game.Stone
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_base.*


abstract class BaseActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        configureToolbar()
    }

    abstract fun getLayoutResource(): Int

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    private fun configureToolbar() {
        toolbar = findViewById<View>(R.id.main_toolbar) as Toolbar?
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            setupBottomNavigationMenu()
            setupBottomNavigationLabelVisibilityAndHorizontalTranslation()
        }
    }

    private fun setupBottomNavigationMenu() {
        //R.id.bottomNavigation.inflateMenu(R.menu.menu_bottom_navigation_2)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item1 -> {
                    val intent = Intent(this, GameActivity::class.java).apply {
                        putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
                        putExtra(GameActivity.EXTRA_KOMI, 5.5)
                        putExtra(GameActivity.EXTRA_LOCAL_COLOR, Stone.BLACK)
                        putExtra(GameActivity.EXTRA_GAME_TYPE, "LOCAL")
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
                    val intent = Intent(this, ChatLastMessageActivity::class.java)
                    val a = Database // Force Database to get initalized
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