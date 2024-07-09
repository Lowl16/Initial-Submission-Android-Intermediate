package com.dicoding.storyapp.data.retrofit.story

import com.dicoding.storyapp.data.response.StoryDetailResponse
import com.dicoding.storyapp.data.response.StoryResponse
import com.dicoding.storyapp.data.response.StoryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoryService {

    @Multipart
    @POST("stories")
    suspend fun uploadStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): StoryUploadResponse

    @GET("stories")
    suspend fun getStories(): Response<StoryResponse>

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): Response<StoryDetailResponse>
}