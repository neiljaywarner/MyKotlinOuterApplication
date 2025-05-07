package com.neiljaywarner.mykotlinouterapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.neiljaywarner.mykotlinouterapplication.databinding.FragmentPhotoViewerBinding

class PhotoViewerFragment : Fragment() {

    private var _binding: FragmentPhotoViewerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the fileName argument directly from arguments bundle
        val fileName = arguments?.getString("fileName") ?: ""
        binding.textviewFilename.text = fileName

        if (fileName.isNotEmpty()) {
            try {
                // Try to load the image using standard ImageView methods
                val imageUri = Uri.parse(fileName)
                binding.imageviewPhoto.setImageURI(imageUri)

                // Log success
                Log.d("PhotoViewerFragment", "Image URI loaded: $fileName")
            } catch (e: Exception) {
                // Handle any errors
                Log.e("PhotoViewerFragment", "Error loading image: ${e.message}")
                binding.imageviewPhoto.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            // No image URI provided
            binding.textviewFilename.text = "No image URI provided"
            binding.imageviewPhoto.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}