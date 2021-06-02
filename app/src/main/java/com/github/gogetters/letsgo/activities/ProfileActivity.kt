package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.cache.Cache
import com.github.gogetters.letsgo.database.ImageStorageService
import com.github.gogetters.letsgo.database.ImageStorageService.Companion.PROFILE_PICTURE_PREFIX_CLOUD
import com.github.gogetters.letsgo.database.user.UserBundle
import com.github.gogetters.letsgo.database.user.UserBundleProvider


class ProfileActivity : BaseActivity() {

    companion object {
        public fun combineTwoTextFields(one: String?, two: String?, separator: String): String {
            return if (one != null && two != null) {
                "$one$separator$two"
            } else two ?: (one ?: "")
        }
    }

    private lateinit var userBundleProvider: UserBundleProvider

    private lateinit var editButton: Button

    private lateinit var profileImage: ImageView
    private lateinit var nick: TextView
    private lateinit var firstLast: TextView
    private lateinit var emailText: TextView
    private lateinit var cityCountyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userBundleProvider = intent.getSerializableExtra("UserBundleProvider") as UserBundleProvider

        editButton = findViewById(R.id.profile_button_edit)
        editButton.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            intent.putExtra("UserBundleProvider", userBundleProvider)
            startActivity(intent)
        }

        profileImage = findViewById(R.id.profile_imageView_image)
        nick = findViewById(R.id.profile_textView_nick)
        firstLast = findViewById(R.id.profile_textView_firstLast)
        emailText = findViewById(R.id.profile_textView_email)
        cityCountyText = findViewById(R.id.profile_textView_cityCountry)

        // Open friend list with button!
        val friendListButton = findViewById<Button>(R.id.profile_show_friend_list_button)
        friendListButton.setOnClickListener {
            if (userBundleProvider.getUserBundle() != null) {
                startActivity(Intent(this, FriendListActivity::class.java))
            }
        }

        val searchUsersButton = findViewById<Button>(R.id.profile_search_users_button)
        searchUsersButton.setOnClickListener {
            if (userBundleProvider.getUserBundle() != null)
                startActivity(Intent(this, UserSearchActivity::class.java))
        }

        updateUI()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_profile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Profile", "PROFILE - onActivityResult $requestCode \t $resultCode")
        updateUI()
    }

    private fun updateUI() {
        val userBundle: UserBundle? = userBundleProvider.getUserBundle()

        if (userBundle == null) {
            // Don't let the user see this screen without having successfully completed sign-in.
            dispatchLoginIntent()
        } else {
            val user = userBundle.getUser()

            user.downloadUserData()
                .addOnFailureListener {
                    // If user is offline then get user from cache
                    val cachedUser = Cache.loadUserProfile(this)

                    if (cachedUser != null) {
                        user.nick = cachedUser.nick
                        user.first = cachedUser.first
                        user.last = cachedUser.last
                        user.country = cachedUser.country
                        user.city = cachedUser.city
                    }

                    displayUser(userBundle)
                }
                .addOnSuccessListener {
                    displayUser(userBundle)
                }
        }
    }

    private fun displayUser(userBundle : UserBundle) {
        val user = userBundle.getUser()

        if (user.nick != null && user.nick!!.isNotEmpty()) {
            nick.text = user.nick
        } else {
            nick.text = getString(R.string.profile_noNicknameHint)
        }

        firstLast.text = combineTwoTextFields(user.first, user.last, " ")

        cityCountyText.text = combineTwoTextFields(user.city, user.country, ", ")

        editButton.visibility = View.VISIBLE

        // cached by Firebase
        emailText.text = userBundle.getEmail()
        // not cached yet
        user.downloadUserData().addOnCompleteListener {
            ImageStorageService.getProfileImageFromCloud(
                PROFILE_PICTURE_PREFIX_CLOUD,
                user.profileImageRef,
                ImageStorageService.getOutputImageFile(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ),
                profileImage)
        }
    }

    private fun dispatchLoginIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}