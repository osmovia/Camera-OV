package com.example.cameraov


import android.app.Activity.NOTIFICATION_SERVICE
import android.app.Activity.RESULT_OK
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Environment.getExternalStorageState
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cameraov.databinding.CameraScreenBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraScreenFragment : Fragment(R.layout.camera_screen) {
    
    private lateinit var binding: CameraScreenBinding
    lateinit var currentPhotoPath: String
    
    private val launcherOpenCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                binding.imageView.setImageURI(Uri.parse(currentPhotoPath))
                galleryAddPic()
            }
        }
    
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                dispatchTakePictureIntent()
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(
                        requireContext(),
                        R.string.permission_photo,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.buttonSettingsPermissions.isVisible = true
                    binding.requiredPermissionText.setText(R.string.permission_settings)
                }
            }
        }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CameraScreenBinding.inflate(layoutInflater)
        return binding.root
    }
    
    override fun onResume() {
        super.onResume()
        viewState()
    }
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewState()
        
        binding.buttonPhoto.setOnClickListener {
            checkPermission()
        }
        
        binding.buttonSettingsPermissions.setOnClickListener {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${requireContext().packageName}")
            ).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.also { intent ->
                startActivity(intent)
            }
        }
    }
    
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        getExternalStorageState()
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.cameraov.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    launcherOpenCamera.launch(takePictureIntent)
                }
            }
        }
    }
    
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireContext().sendBroadcast(mediaScanIntent)
        }
    }
    
    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    
    private fun checkPermission() {
        if (haveStoragePermission()) {
            dispatchTakePictureIntent()
        } else {
            requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    
    private fun viewState() {
        binding.imageView.isVisible = haveStoragePermission()
        binding.lockedImage.isVisible = !haveStoragePermission()
        binding.requiredPermissionText.isVisible = !haveStoragePermission()
        if (haveStoragePermission()) {
            binding.buttonSettingsPermissions.isVisible = false
        }
    }
}
