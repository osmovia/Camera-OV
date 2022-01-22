package com.example.cameraov

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.cameraov.databinding.PhotoScreenBinding

class PhotoScreenFragment: Fragment() {


    private lateinit var binding: PhotoScreenBinding
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

        }
    }

//    private val resultLauncherPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//        when {
//            granted -> {
//
//            }
//            shouldShowRequestPermissionRationale(android.Manifest.permission.)
//        }
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PhotoScreenBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonSelectPhotoId.setOnClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

            }
            checkVersionSdkAndPermission()
        }

    }

    private fun checkVersionSdkAndPermission() {
        if (Build.VERSION.SDK_INT <= 29) {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(gallery)
        } else {

        }
    }
    private fun getPhotoFromAlbum() {

    }
}
