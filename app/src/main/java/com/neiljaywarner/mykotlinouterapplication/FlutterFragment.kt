package com.neiljaywarner.mykotlinouterapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * A fragment that embeds a Flutter module.
 */
class FlutterFragment : Fragment() {
    // Using Any type to avoid compile errors when Flutter embedding library isn't available
    private var flutterFragment: Any? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flutter, container, false)
        
        try {
            // Note: The following code requires the Flutter embedding library at runtime.
            // This will be automatically included by the include_flutter.groovy script.

            // Get the FlutterFragment class via reflection to avoid compile-time dependency
            val flutterFragmentClass = Class.forName("io.flutter.embedding.android.FlutterFragment")

            // Create Flutter Fragment using the createDefault() static method
            val createDefaultMethod = flutterFragmentClass.getMethod("createDefault")
            flutterFragment = createDefaultMethod.invoke(null)

            if (flutterFragment != null && flutterFragment is Fragment) {
                // Add the Flutter Fragment to the container
                childFragmentManager.beginTransaction()
                    .replace(R.id.flutter_container, flutterFragment as Fragment)
                    .commit()
            } else {
                showError(view, "Flutter fragment creation failed")
            }
        } catch (e: Exception) {
            // Show error if Flutter integration fails
            showError(view, "Flutter module not ready: ${e.message}")
        }
        
        return view
    }

    private fun showError(view: View, errorMessage: String) {
        val flutterContainer = view.findViewById<ViewGroup>(R.id.flutter_container)
        flutterContainer.removeAllViews()  // Clear any existing views

        val textView = TextView(requireContext()).apply {
            text = errorMessage
            textSize = 16f
            setPadding(32, 32, 32, 32)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        flutterContainer.addView(textView)
    }

    // Forward lifecycle events to the Flutter Fragment when available
    override fun onResume() {
        super.onResume()
        try {
            if (flutterFragment != null) {
                val onResumeMethod = flutterFragment!!.javaClass.getMethod("onResume")
                onResumeMethod.invoke(flutterFragment)
            }
        } catch (e: Exception) {
            // Ignore lifecycle method errors
        }
    }
    
    override fun onPause() {
        super.onPause()
        try {
            if (flutterFragment != null) {
                val onPauseMethod = flutterFragment!!.javaClass.getMethod("onPause")
                onPauseMethod.invoke(flutterFragment)
            }
        } catch (e: Exception) {
            // Ignore lifecycle method errors
        }
    }
    
    override fun onStart() {
        super.onStart()
        try {
            if (flutterFragment != null) {
                val onStartMethod = flutterFragment!!.javaClass.getMethod("onStart")
                onStartMethod.invoke(flutterFragment)
            }
        } catch (e: Exception) {
            // Ignore lifecycle method errors
        }
    }
    
    override fun onStop() {
        super.onStop()
        try {
            if (flutterFragment != null) {
                val onStopMethod = flutterFragment!!.javaClass.getMethod("onStop")
                onStopMethod.invoke(flutterFragment)
            }
        } catch (e: Exception) {
            // Ignore lifecycle method errors
        }
    }
}