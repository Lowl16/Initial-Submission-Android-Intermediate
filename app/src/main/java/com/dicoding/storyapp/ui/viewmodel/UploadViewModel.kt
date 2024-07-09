package com.dicoding.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.repository.UploadRepository
import java.io.File

class UploadViewModel(private val uploadRepository: UploadRepository) : ViewModel() {

    fun uploadStories(file: File, description: String) = uploadRepository.uploadStories(file, description)
}