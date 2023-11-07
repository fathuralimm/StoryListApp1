package com.dicoding.storylistapp.data

import android.media.session.MediaSession.Token
import android.view.PixelCopy.Request
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import com.dicoding.storylistapp.data.pref.UserModel
import com.dicoding.storylistapp.data.pref.UserPreference
import com.dicoding.storylistapp.data.response.AddResponse
import com.dicoding.storylistapp.data.response.ListStoryItem
import com.dicoding.storylistapp.data.response.LoginResponse
import com.dicoding.storylistapp.data.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import com.dicoding.storylistapp.data.retrofit.Result
import com.dicoding.storylistapp.data.retrofit.ServiceApi
import okhttp3.MultipartBody


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ServiceApi
)  {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        name: String,
        email: String,
        password: String
    ):LiveData<Result<RegisterResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Result.Success(response))
            }catch (e: Exception){
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(
        email: String,
        password: String
    ):LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                val token = response.loginResult.token
                saveSession(UserModel(email, token))
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData (Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getStories("Bearer $token")
                val stories = response.listStory
                emit(Result.Success(stories))
            } catch (e: Exception){
                emit(Result.Error(e.message.toString()))
            }
        }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<AddResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.postStories("Bearer $token", file, description)
                emit(Result.Success(response))
                } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            serviceApi: ServiceApi
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,serviceApi )
            }.also { instance = it }
    }
}