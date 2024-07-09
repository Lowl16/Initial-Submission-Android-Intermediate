package com.dicoding.storyapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.state.ResultState
import com.dicoding.storyapp.databinding.ActivityAddBinding
import com.dicoding.storyapp.ui.viewmodel.UploadViewModel
import com.dicoding.storyapp.ui.viewmodel.UploadViewModelFactory
import com.dicoding.storyapp.utils.getImageUri
import com.dicoding.storyapp.utils.reduceFileImage
import com.dicoding.storyapp.utils.uriToFile

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    private var imageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val uploadViewModelFactory: UploadViewModelFactory = UploadViewModelFactory.getInstance(this@AddActivity)
        val uploadViewModel: UploadViewModel by viewModels { uploadViewModelFactory }

        binding.btnCamera.setOnClickListener {
            imageUri = getImageUri(this)
            launchCamera.launch(imageUri)
        }

        binding.btnGallery.setOnClickListener {
            launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonAdd.setOnClickListener {
            imageUri?.let { uri ->
                val file = uriToFile(uri, this).reduceFileImage()
                val description = binding.edAddDescription.text.toString()

                uploadViewModel.uploadStories(file, description).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                showLoading(false)
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }

                            is ResultState.Error -> {
                                showLoading(false)
                                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } ?: Toast.makeText(this, getString(R.string.invalid_image), Toast.LENGTH_SHORT).show()
        }
    }

    private val launchCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            showImage()
        }
    }

    private val launchGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        imageUri?.let {
            binding.ivPhoto.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.pbAdd.visibility = if (isLoading) View.VISIBLE else View.GONE }
}