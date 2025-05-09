package com.neiljaywarner.mykotlinouterapplication

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntentHandlingTest {

    @Test
    fun testImageUriIsProcessed() {
        // Create an intent with image URI
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"

        // Use a test image that exists on the device
        // This is a generic Android resource URI that should exist on any device
        val testUri =
            Uri.parse("android.resource://${context.packageName}/${R.drawable.ic_launcher_foreground}")
        intent.putExtra(Intent.EXTRA_STREAM, testUri)

        // Launch the activity with our intent
        ActivityScenario.launch<MainActivity>(intent).use {
            // Wait for the app to initialize
            Thread.sleep(1000)

            // Verify that we're in the Flutter fragment (check for flutter_container)
            onView(withId(R.id.flutter_container)).check(matches(isDisplayed()))

            // Note: We can't easily check the Flutter UI content since it's rendered by Flutter,
            // but at least we can verify the FlutterFragment is displayed which means our
            // navigation in MainActivity worked correctly
        }
    }
}