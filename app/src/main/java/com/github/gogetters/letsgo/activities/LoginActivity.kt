package com.github.gogetters.letsgo.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.github.gogetters.letsgo.database.user.FirebaseUserBundle
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            createSignInIntent()
        }
    }

    fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
//                AuthUI.IdpConfig.PhoneBuilder().build(),
//                AuthUI.IdpConfig.GoogleBuilder().build(),
//                AuthUI.IdpConfig.FacebookBuilder().build(),
//                AuthUI.IdpConfig.TwitterBuilder().build()
        )

        // Create and launch sign-in intent
        val intent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setAlwaysShowSignInMethodScreen(true)
            .build()
        Log.i("LOGIN", intent.toString())
        startActivityForResult(
                intent,
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in -> store it properly on firebase
                val user = FirebaseAuth.getInstance().currentUser
                val userBundle = FirebaseUserBundle(user)
                userBundle.getUser().uploadUserData()

                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("UserBundleProvider", FirebaseUserBundleProvider)
                startActivity(intent)
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private fun signOut(onComplete: () -> Unit) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    // ...
                    onComplete()
                }
    }

    private fun delete() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener {
                    // ...
                }
    }

    private fun themeAndLogo() {
        val providers = emptyList<AuthUI.IdpConfig>()

//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .setLogo(R.drawable.my_great_logo) // Set logo drawable
//                        .setTheme(R.style.MySuperAppTheme) // Set theme
//                        .build(),
//                RC_SIGN_IN)
    }

    private fun privacyAndTerms() {
        val providers = emptyList<AuthUI.IdpConfig>()
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN)
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}