package com.dicoding.storylistapp.di

import android.content.Context
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.pref.UserPreference
import com.dicoding.storylistapp.data.pref.dataStore
import com.dicoding.storylistapp.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val serviceApi = ApiConfig.apiService()
        return UserRepository.getInstance(pref, serviceApi)
    }
}