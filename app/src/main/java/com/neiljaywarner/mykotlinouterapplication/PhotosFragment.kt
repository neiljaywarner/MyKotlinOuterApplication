package com.neiljaywarner.mykotlinouterapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neiljaywarner.mykotlinouterapplication.databinding.FragmentPhotosBinding

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    // ActivityResultLauncher for picking an image
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Image selected, navigate to PhotoViewerFragment with the URI as a string
                Log.d("PhotosFragment", "Image selected: ${it.toString()}")

                // Create a bundle with the fileName argument
                val bundle = bundleOf("fileName" to it.toString())

                // Navigate to PhotoViewerFragment with the bundle
                findNavController().navigate(
                    R.id.action_PhotosFragment_to_PhotoViewerFragment,
                    bundle
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShareToSecondFragment.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}