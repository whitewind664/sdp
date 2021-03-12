package com.github.gogetters.letsgo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity0 : AppCompatActivity() {

    private val TAG = "LoginActivity0"
    private val RC_SIGN_IN = 9001

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var statusTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login0)

        statusTextView = findViewById(R.id.signin_status)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(
                TAG,
                "signInResult:failed code=" + e.statusCode
            )
            updateUI(null)
        }
    }


    fun signIn(view: View) {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    fun signOut(view: View) {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                updateUI(null)
            }
    }


    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // statusTextView!!.text = getString(R.string.signed_in_fmt, account.displayName)
            statusTextView!!.text = "Signed in: " + account.email
            findViewById<View>(R.id.signin_button).visibility = View.GONE
            findViewById<View>(R.id.signout_button).visibility = View.VISIBLE
        } else {
            statusTextView!!.text = "Not signed in"
            findViewById<View>(R.id.signin_button).visibility = View.VISIBLE
            findViewById<View>(R.id.signout_button).visibility = View.GONE
        }
    }
}