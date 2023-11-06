package com.dicoding.storylistapp.di

import android.content.Context
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.pref.UserPreference
import com.dicoding.storylistapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}