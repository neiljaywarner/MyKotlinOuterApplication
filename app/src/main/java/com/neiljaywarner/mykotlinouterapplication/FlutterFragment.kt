package com.neiljaywarner.mykotlinouterapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File

/**
 * A fragment that embeds a Flutter module.
 * This fragment can receive image URI and pass it to the Flutter side using Pigeon.
 * It uses reflection to access the generated Pigeon classes.
 */
class FlutterFragment : Fragment() {
    private var flutterFragment: Fragment? = null
    
    // The image URI that should be passed to Flutter
    private var imageUri: String? = null
    
    companion object {
        const val ARG_IMAGE_URI = "image_uri"
        private const val TAG = "FlutterFragment"
        
        // Pigeon related constants - Updated for receive_images_flutter_demo
        private const val PIGEON_PACKAGE = "com.example.receive_images_flutter_demo.GeneratedPigeon"
        private const val IMAGE_API_CLASS = "$PIGEON_PACKAGE.ImageApi"
        private const val IMAGE_API_SETUP_CLASS = "$PIGEON_PACKAGE.ImageApiSetup"
        private const val IMAGE_INFO_CLASS = "$PIGEON_PACKAGE.ImageInfo"
        
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
        
        try {
            // Create Flutter Fragment using reflection to avoid direct import issues
            val flutterFragmentClass = Class.forName("io.flutter.embedding.android.FlutterFragment")
            val createDefaultMethod = flutterFragmentClass.getMethod("createDefault")
            flutterFragment = createDefaultMethod.invoke(null) as Fragment
            
            if (flutterFragment != null) {
                // Add the Flutter Fragment to the container
                childFragmentManager.beginTransaction()
                    .replace(R.id.flutter_container, flutterFragment!!)
                    .commit()
                
                // Get the Flutter engine
                val flutterEngine = getFlutterEngine(flutterFragment!!)
                
                if (flutterEngine != null) {
                    // Set up Pigeon API after the Flutter fragment is created
                    setupPigeonApi(flutterEngine)
                } else {
                    showError(view, "Failed to get Flutter engine")
                }
            } else {
                showError(view, "Flutter fragment creation failed")
            }
        } catch (e: Exception) {
            // Show error if Flutter integration fails
            Log.e(TAG, "Error initializing Flutter: ${e.message}", e)
            showError(view, "Flutter module not ready: ${e.message}")
        }
        
        return view
    }

    private fun getFlutterEngine(flutterFragment: Fragment): Any? {
        return try {
            // Get FlutterEngine from the fragment using reflection
            val getFlutterEngineMethod = flutterFragment.javaClass.getMethod("getFlutterEngine")
            getFlutterEngineMethod.invoke(flutterFragment)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting Flutter engine: ${e.message}", e)
            null
        }
    }
    
    private fun setupPigeonApi(flutterEngine: Any) {
        try {
            // Get the binary messenger from Flutter engine
            val binaryMessenger = getBinaryMessenger(flutterEngine) ?: return
            
            // Get the ImageApiSetup class via reflection
            val imageApiSetupClass = Class.forName(IMAGE_API_SETUP_CLASS)
            val imageApiClass = Class.forName(IMAGE_API_CLASS)
            val imageInfoClass = Class.forName(IMAGE_INFO_CLASS)
            
            // Create an implementation of ImageApi
            val imageApiImpl = createImageApiImpl(imageApiClass, imageInfoClass)
            
            // Call ImageApiSetup.setup(BinaryMessenger, ImageApi)
            val setupMethod = imageApiSetupClass.getMethod("setup", 
                Class.forName("io.flutter.plugin.common.BinaryMessenger"), 
                imageApiClass)
            setupMethod.invoke(null, binaryMessenger, imageApiImpl)
            
            Log.d(TAG, "Pigeon API setup complete")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up Pigeon API: ${e.message}", e)
        }
    }
    
    private fun createImageApiImpl(imageApiClass: Class<*>, imageInfoClass: Class<*>): Any {
        // Create a proxy for the ImageApi interface
        return java.lang.reflect.Proxy.newProxyInstance(
            imageApiClass.classLoader,
            arrayOf(imageApiClass)
        ) { _, method, args ->
            when (method.name) {
                "sendImage" -> {
                    val imageUri = args?.getOrNull(0) as? String
                    val callback = args?.getOrNull(1) as? Function1<*, *>
                    
                    Log.d(TAG, "sendImage called with URI: $imageUri")
                    
                    @Suppress("UNCHECKED_CAST")
                    (callback as? Function1<Result<Any?>, Unit>)?.invoke(Result.success(null))
                    null
                }
                "getImage" -> {
                    val callback = args?.getOrNull(0) as? Function1<*, *>
                    
                    // Check if we have an image URI to send
                    if (this.imageUri != null) { // Use this.imageUri to access the fragment's property
                        val uri = this.imageUri!!
                        Log.d(TAG, "Sending image URI to Flutter: $uri")
                        
                        // Extract file name from URI, if possible
                        val fileName = try {
                            val file = File(uri)
                            file.name
                        } catch (e: Exception) {
                            // If we can't get a name from the URI, use the URI itself
                            uri.substringAfterLast('/')
                        }
                        
                        // Create ImageInfo object using reflection
                        val imageInfo = imageInfoClass.getDeclaredConstructor().newInstance()
                        
                        // Set uri and fileName fields
                        val setUriMethod = imageInfoClass.getMethod("setUri", String::class.java)
                        val setFileNameMethod = imageInfoClass.getMethod("setFileName", String::class.java)
                        
                        setUriMethod.invoke(imageInfo, uri)
                        setFileNameMethod.invoke(imageInfo, fileName)
                        
                        @Suppress("UNCHECKED_CAST")
                        (callback as? Function1<Result<Any>, Unit>)?.invoke(Result.success(imageInfo))
                    } else {
                        // No image to send
                        Log.d(TAG, "No image URI available")
                        
                        @Suppress("UNCHECKED_CAST")
                        (callback as? Function1<Result<Any>, Unit>)?.invoke(
                            Result.failure(Exception("No image URI available"))
                        )
                    }
                    
                    null
                }
                else -> {
                    throw UnsupportedOperationException("Unknown method: ${method.name}")
                }
            }
        }
    }
    
    private fun getBinaryMessenger(flutterEngine: Any): Any? {
        return try {
            // Get DartExecutor
            val getDartExecutorMethod = flutterEngine.javaClass.getMethod("getDartExecutor")
            val dartExecutor = getDartExecutorMethod.invoke(flutterEngine) ?: return null
            
            // Get BinaryMessenger
            val getBinaryMessengerMethod = dartExecutor.javaClass.getMethod("getBinaryMessenger")
            getBinaryMessengerMethod.invoke(dartExecutor)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting binary messenger: ${e.message}", e)
            null
        }
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
        
        // Log the error
        Log.e(TAG, errorMessage)
    }
    
    // Forward lifecycle events to the Flutter Fragment
    override fun onResume() {
        super.onResume()
        forwardLifecycleEvent(flutterFragment, "onResume")
    }
    
    override fun onPause() {
        super.onPause()
        forwardLifecycleEvent(flutterFragment, "onPause")
    }
    
    override fun onStart() {
        super.onStart()
        forwardLifecycleEvent(flutterFragment, "onStart")
    }
    
    override fun onStop() {
        super.onStop()
        forwardLifecycleEvent(flutterFragment, "onStop")
    }
    
    private fun forwardLifecycleEvent(fragment: Fragment?, methodName: String) {
        try {
            if (fragment != null) {
                val method = fragment.javaClass.getMethod(methodName)
                method.invoke(fragment)
            }
        } catch (e: Exception) {
            // Ignore lifecycle method errors
            Log.e(TAG, "Error forwarding $methodName to Flutter: ${e.message}")
        }
    }
}