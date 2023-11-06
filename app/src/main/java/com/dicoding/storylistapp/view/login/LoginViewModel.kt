package com.dicoding.storylistapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.pref.UserModel
import com.dicoding.storylistapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginViewModel = MediatorLiveData<Result<LoginResponse>>()
    val loginViewModel: LiveData<Result<LoginResponse>> = _loginViewModel

    fun login(email: String, password: String) {
        val liveData = repository.login(email, password)
        _loginViewModel.addSource(liveData) { result ->
            _loginViewModel.value = result
        }
    }
}

