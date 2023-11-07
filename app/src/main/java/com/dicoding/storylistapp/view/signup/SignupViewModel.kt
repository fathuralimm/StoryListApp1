package com.dicoding.storylistapp.view.signup

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.response.RegisterResponse
import com.dicoding.storylistapp.data.retrofit.Result

class SignupViewModel(private val repository: UserRepository): ViewModel() {

    private val _registerResponse = MediatorLiveData<Result<RegisterResponse>>()
    val registerResponse: LiveData<Result<RegisterResponse>> = _registerResponse

    fun register(name: String, email: String, password: String){
        val liveData = repository.register(name, email, password)
        _registerResponse.addSource(liveData){ result ->
            _registerResponse.value = result
        }
    }
}