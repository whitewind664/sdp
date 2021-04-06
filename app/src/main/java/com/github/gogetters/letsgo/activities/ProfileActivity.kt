package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.github.gogetters.letsgo.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : FirebaseUIActivity() {

    private var uploadImageText: TextView? = null
    private var profileImage: ImageView? = null
    private var nameText: TextView? = null
    private var emailText: TextView? = null
    private var cityCountyText: TextView? = null
    private var saveButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        uploadImageText = findViewById(R.id.profile_textView_uploadImageHint)
        profileImage = findViewById(R.id.profile_imageView_image)
        nameText = findViewById(R.id.profile_textView_name)
        emailText = findViewById(R.id.profile_textView_email)
        cityCountyText = findViewById(R.id.profile_textView_cityCountry)
        saveButton = findViewById(R.id.profile_imageButton_save)

        updateUI()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        updateUI()
    }

    private fun updateUI() {
        val authInstance = FirebaseAuth.getInstance().currentUser

        if (authInstance == null) {

        } else {
            nameText!!.text = authInstance.displayName
            emailText!!.text = authInstance.email
            // cityCountyText!!.text = authInstance.
        }
    }
}