package com.example.cameraov

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cameraov.databinding.PhotoScreenBinding

class PhotoScreenFragment: Fragment() {

    private lateinit var binding: PhotoScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = PhotoScreenBinding.inflate(layoutInflater)
    }
}
