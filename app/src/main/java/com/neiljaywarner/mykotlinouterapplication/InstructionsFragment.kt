package com.neiljaywarner.mykotlinouterapplication

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neiljaywarner.mykotlinouterapplication.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

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
            openGallery()
        }

        binding.buttonTempFlutter.setOnClickListener {
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

    private fun openGallery() {
        try {
            // Create an implicit intent to open the gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Could not open gallery. Please check if you have a gallery app installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}