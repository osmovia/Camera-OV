package com.example.cameraov

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.cameraov.databinding.PhotoScreenBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoScreenFragment : Fragment() {
    
    
    private lateinit var binding: PhotoScreenBinding
    lateinit var currentPhotoPath: String
    
    private val resultLauncherPickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val sourceUri = result?.data?.data ?: return@registerForActivityResult
                val destinationUri = Uri.fromFile(createImageFile())
                
                openUCropActivity(sourceUri, destinationUri)
            }
        }
    
    private val resultLauncherUCrop =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.imageView.setImageURI(Uri.parse(currentPhotoPath))
            }
            
        }
    
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
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncherPickImage.launch(gallery)
        }
        
    }
    
    private fun openUCropActivity(sourceUri: Uri, destinationUri: Uri) {
        val options = UCrop.Options()
        options.setCropGridColor(resources.getColor(R.color.transparent))
        
        val intent = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(16F, 16F)
            .withOptions(options)
            .getIntent(requireContext())
        
        resultLauncherUCrop.launch(intent)
    }
    
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        Environment.getExternalStorageState()
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}
