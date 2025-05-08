package com.neiljaywarner.mykotlinouterapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neiljaywarner.mykotlinouterapplication.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    // ActivityResultLauncher for picking an image
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Navigate to the FlutterFragment with the selected image URI
                Log.d("InstructionsFragment", "Image selected: ${it.toString()}")

                // Create arguments bundle with the image URI
                val args = bundleOf(FlutterFragment.ARG_IMAGE_URI to it.toString())

                // Navigate to FlutterFragment with the image URI
                findNavController().navigate(
                    R.id.action_InstructionsFragment_to_FlutterFragment,
                    args
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonOpenGallery.setOnClickListener {
            // Launch the image picker using ActivityResultLauncher
            pickImageLauncher.launch("image/*")
        }

        binding.buttonTempFlutter.setOnClickListener {
            // Just navigate to the FlutterFragment without an image
            navigateToFlutterFragment()
        }
    }

    private fun navigateToFlutterFragment() {
        try {
            findNavController().navigate(R.id.action_InstructionsFragment_to_FlutterFragment)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Flutter module not available. Please check instructions in add_to_app.md",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}