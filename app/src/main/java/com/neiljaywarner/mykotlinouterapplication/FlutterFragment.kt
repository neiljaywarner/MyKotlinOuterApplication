package com.neiljaywarner.mykotlinouterapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * A placeholder for the Flutter fragment.
 * This fragment currently shows a placeholder message.
 * Full Flutter integration with Pigeon is pending.
 */
class FlutterFragment : Fragment() {

    companion object {
        const val ARG_IMAGE_URI = "image_uri"
        private const val TAG = "FlutterFragment"
        
        fun newInstance(imageUri: String?): FlutterFragment {
            val fragment = FlutterFragment()
            val args = Bundle()
            if (imageUri != null) {
                args.putString(ARG_IMAGE_URI, imageUri)
            }
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the image URI from arguments
        imageUri = arguments?.getString(ARG_IMAGE_URI)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flutter, container, false)

        // Get the image URI from arguments if passed
        val imageUri = arguments?.getString(ARG_IMAGE_URI)

        // Show a placeholder message
        val textView = TextView(requireContext()).apply {
            text = if (imageUri != null) {
                "Flutter Module Placeholder\nImage URI: $imageUri\n(Pigeon integration pending)"
            } else {
                "Flutter Module Placeholder\n(Pigeon integration pending)"
            }
            textSize = 18f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(32, 32, 32, 32)
        }
        
        val flutterContainer = view.findViewById<ViewGroup>(R.id.flutter_container)
        flutterContainer.removeAllViews() // Clear previous views if any
        flutterContainer.addView(textView)

        return view
    }
}