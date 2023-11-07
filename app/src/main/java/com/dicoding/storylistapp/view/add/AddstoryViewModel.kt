package com.dicoding.storylistapp.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.pref.UserModel
import com.dicoding.storylistapp.data.response.AddResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import com.dicoding.storylistapp.data.retrofit.Result

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {

    private val _addStoryResponse = MediatorLiveData<Result<AddResponse>>()
    val addStoryResponse: LiveData<Result<AddResponse>> = _addStoryResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) {
        val liveData = repository.addStory(token, file, description)
        _addStoryResponse.addSource(liveData) { result ->
            _addStoryResponse.value = result
        }
    }
}