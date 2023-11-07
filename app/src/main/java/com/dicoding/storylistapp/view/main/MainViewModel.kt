package com.dicoding.storylistapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.pref.UserModel
import com.dicoding.storylistapp.data.response.ListStoryItem
import kotlinx.coroutines.launch
import com.dicoding.storylistapp.data.retrofit.Result

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    private val _storyListItem = MediatorLiveData<Result<List<ListStoryItem>>>()
    val StoryListItem: LiveData<Result<List<ListStoryItem>>> = _storyListItem

    fun getStories(token: String) {
        val liveData = repository.getStories(token)
        _storyListItem.addSource(liveData) { result ->
            _storyListItem.value = result
        }
    }
}
