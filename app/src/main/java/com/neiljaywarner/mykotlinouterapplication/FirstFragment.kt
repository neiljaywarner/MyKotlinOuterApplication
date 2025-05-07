package com.neiljaywarner.mykotlinouterapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.neiljaywarner.mykotlinouterapplication.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonOpenPhotos.setOnClickListener {
            openGooglePhotos()
        }
    }

    private fun openGooglePhotos() {
        val googlePhotosPackage = "com.google.android.apps.photos"
        val context = requireContext()

        try {
            val googlePhotosIntent =
                context.packageManager.getLaunchIntentForPackage(googlePhotosPackage)
            if (googlePhotosIntent != null) {
                startActivity(googlePhotosIntent)
                return
            }

            val galleryIntent = Intent(Intent.ACTION_VIEW).apply {
                type = "image/*"
            }

            if (galleryIntent.resolveActivity(context.packageManager) != null) {
                startActivity(galleryIntent)
                return
            }

            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$googlePhotosPackage")
                setPackage("com.android.vending")
            }

            try {
                startActivity(playStoreIntent)
            } catch (e: ActivityNotFoundException) {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$googlePhotosPackage")
                )
                startActivity(webIntent)
            }

        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Could not open Photos. Please install Google Photos from Play Store.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}