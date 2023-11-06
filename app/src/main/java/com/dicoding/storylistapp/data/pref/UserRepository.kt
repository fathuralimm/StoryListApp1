package com.dicoding.storylistapp.data

import android.media.session.MediaSession.Token
import android.view.PixelCopy.Request
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.storylistapp.data.pref.UserModel
import com.dicoding.storylistapp.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference
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
        LiveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService .register(name, email, password)
                emit(Result.Success(response))
            }catch (e: Exception){
                emit(Result.Error(e.message.toString()))
            }
        }

    fun Login(
        email: String,
        password: String
    ):LiveData<Result<LoginResponse>> =
        LiveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                val token = response.loginResult.token
                saveSession(UserModel(email, token))
                emkit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.to=String()))
            }
        }

    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData (Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getStories(("Bearer $token"))
                val stories = response.listStory
                emit(Result.Success(stories))
            } catch (e: Expection){
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
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}