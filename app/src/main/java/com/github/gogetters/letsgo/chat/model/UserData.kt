package com.github.gogetters.letsgo.chat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * The UserData class represents the users registered and fetched from the database
 * It implements the Parcelable interface to be passed as object between activities
 */
@Parcelize
data class UserData(
    var id: String? = "",
    var nick: String? = "",
    var first: String? = "",
    var last: String? = "",
    var city: String? = "",
    var country: String? = ""
) : Parcelable {

    /**
     * Empty constructor is required to parse the object
     */
    constructor() : this("", "", "", "", "", "")

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "nick" to nick,
            "first" to first,
            "last" to last,
            "city" to city,
            "country" to country
        )
    }

}
