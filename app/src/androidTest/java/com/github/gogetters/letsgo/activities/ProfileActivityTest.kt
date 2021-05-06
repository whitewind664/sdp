package com.github.gogetters.letsgo.activities

import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView as onView
import androidx.test.espresso.matcher.ViewMatchers.withText as withText

@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(ProfileActivity::class.java)



    @Test
    fun testProfileOpens() {}

    // @Test
    // Do not test uses firebase
    fun testEmailLogin() {

        val testEmail = ""
        val testPassword = ""

        onView(withText("Sign in with email"))!!.perform(ViewActions.click())

        val emailField = onView(ViewMatchers.withHint("Email"))
        emailField!!.perform(ViewActions.typeText(testEmail), ViewActions.closeSoftKeyboard())

        onView(withText("Next"))!!.perform(ViewActions.click())


        val passwordField = onView(ViewMatchers.withHint("Password"))
        passwordField!!.perform(ViewActions.typeText(testPassword), ViewActions.closeSoftKeyboard())

        onView(withText("Sign in"))!!.perform(ViewActions.click())
    }

}