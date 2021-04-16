package com.github.gogetters.letsgo.database

import com.google.firebase.auth.FirebaseUser

class LetsGoUser(val fb: FirebaseUser) {
    var nick: String? = null
    var first: String? = null
    var last: String? = null
    var city: String? = null
    var country: String? = null

    override fun toString(): String {
        // TODO Maybe improve this?

        return "LetsGoUser : " + super.toString() + "\t" + fb.toString() + "\t" + nick + "\t" + first + "\t" + last + "\t" + city + "\t" + country
    }
}