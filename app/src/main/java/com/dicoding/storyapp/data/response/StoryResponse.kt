package com.dicoding.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponse (

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val story: List<ListStory>
)

data class ListStory (

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoURL: String,

    @field:SerializedName("createdAt")
    val createdAt: String
)