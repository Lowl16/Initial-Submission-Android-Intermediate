package com.dicoding.storyapp.repository

import com.dicoding.storyapp.data.retrofit.story.StoryService

class StoryRepository private constructor(private val storyService: StoryService) {

    suspend fun getStories() = storyService.getStories()

    suspend fun getDetailStories(id: String) = storyService.getDetailStories(id)

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            storyService: StoryService
        ): StoryRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(storyService)
            }.also { INSTANCE = it }
    }
}