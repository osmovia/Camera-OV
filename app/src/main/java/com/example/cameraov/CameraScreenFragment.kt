package com.example.cameraov


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Environment.getExternalStorageState
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.cameraov.databinding.CameraScreenBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class CameraScreenFragment: Fragment(R.layout.camera_screen) {

    private lateinit var binding: CameraScreenBinding
    lateinit var currentPhotoPath: String

    private val resultLauncherOpenCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            galleryAddPic()
        }
    }
//    private val resultLauncherPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> when {
//        granted -> {
//
//        }
//        shouldShowRequestPermissionRationale(android.Manifest.permission.)
//    }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CameraScreenBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonPhoto.setOnClickListener {
            checkVersionSdk()
        }

        binding.buttonGolder.setOnClickListener {
            galleryAddPic()
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
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
                    resultLauncherOpenCamera.launch(takePictureIntent)
                }
            }
        }
    }

//    private fun saveBitmapToInternalStorage(filename: String, bitmap: Bitmap) {
//        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val wrapper = ContextWrapper(context)
//        var file = wrapper.getDir("images", MODE_PRIVATE)
//        file = File(file, "${filename}.jpg")
//        val file = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        try {
//            // Get the file output stream
//            val stream: OutputStream = FileOutputStream(file)
//
//            // Compress bitmap
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//
//            // Flush the stream
//            stream.flush()
//
//            // Close stream
//            stream.close()
//        } catch (e: IOException){ // Catch the exception
//            e.printStackTrace()
//        }
//
//         Return the saved image uri
//        return Uri.parse(file.absolutePath)
//    }
//        try {
//            requireContext().openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
//                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream)) {
//                    throw IOException("Unable to save bitmap")
//                }
//            }
//        } catch (exception:Exception){
//            exception.printStackTrace()
//        }
//    }

//    private fun capturePhoto() {
//        resultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
//    }

//    private fun loadFilesFromInternalStorage(nameFile: String) {
//        val files = requireContext().filesDir?.listFiles()
//        files?.forEach { file ->
//            if (file.name == nameFile) {
//                val uri = file.toUri()
//                binding.imageView.setImageURI(uri)
//                return
//            }
//        }
//    }
 private fun galleryAddPic() {
     Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
         val f = File(currentPhotoPath)
         mediaScanIntent.data = Uri.fromFile(f)
         requireContext().sendBroadcast(mediaScanIntent)
     }
 }
    private fun checkVersionSdk() {
        when (Build.VERSION.SDK_INT) {
            22 -> {
                versionCodes22()
            }

            in 23..28 -> {
                versionCodesM()
            }
            in 29..31 -> {}
        }
    }
    private fun versionCodesM() {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireActivity().requestPermissions(
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1
                    )
                }
            } else {
                dispatchTakePictureIntent()
            }
        }

    private fun versionCodes22() {
        dispatchTakePictureIntent()
    }
//    private void checkBTPermissions() {
//
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
//
//            int permissionCheck =
//
//            this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
//
//
//
//            permissionCheck+=this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//
//
//
//            if (permissionCheck != 0) {
//
//
//
//                this.requestPermissions(new String{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
//
//            }
//
//        }else{
//
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
//
//        }
//
//    }

}
