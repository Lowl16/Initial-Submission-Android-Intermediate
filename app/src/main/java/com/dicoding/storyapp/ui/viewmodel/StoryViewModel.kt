package com.dicoding.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.response.ListStory
import com.dicoding.storyapp.data.response.Story
import com.dicoding.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStory>>()
    val stories: LiveData<List<ListStory>> = _stories

    private val _detailStories = MutableLiveData<Story>()
    val detailStories: LiveData<Story> = _detailStories

    private val _responseCode = MutableLiveData<Int>()
    val responseCode: LiveData<Int> = _responseCode

    fun getStories() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStories()

                if (response.isSuccessful) _stories.value = response.body()?.story else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getDetailStories(id: String) {
        viewModelScope.launch {
            try {
                val response = storyRepository.getDetailStories(id)

                if (response.isSuccessful) _detailStories.value = response.body()?.story else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }
}