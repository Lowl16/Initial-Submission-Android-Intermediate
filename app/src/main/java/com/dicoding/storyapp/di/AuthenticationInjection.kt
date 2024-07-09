package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.retrofit.authentication.AuthenticationConfig
import com.dicoding.storyapp.repository.AuthenticationRepository

object AuthenticationInjection {
    fun provideRepository(context: Context): AuthenticationRepository {
        val apiService = AuthenticationConfig.getApiService()

        return AuthenticationRepository.getInstance(apiService)
    }
}