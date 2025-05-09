package com.neiljaywarner.mykotlinouterapplication

import android.os.Bundle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
class FlutterFragmentTest {

    @Test
    fun `test fragment receives image URI from arguments`() {
        // Create a test URI
        val testImageUri = "content://test/image.jpg"
        
        // Create a bundle with the URI
        val args = Bundle().apply {
            putString(FlutterFragment.ARG_IMAGE_URI, testImageUri)
        }
        
        // Create the fragment and set arguments
        val fragment = FlutterFragment().apply {
            arguments = args
        }
        
        // Trigger onCreate to process arguments
        fragment.onCreate(Bundle())
        
        // Verify the fragment has stored the URI
        // This is a white-box test that directly accesses the private field using reflection
        val imageUriField = FlutterFragment::class.java.getDeclaredField("imageUri")
        imageUriField.isAccessible = true
        val actualUri = imageUriField.get(fragment) as String?
        
        assertEquals(testImageUri, actualUri)
    }
}