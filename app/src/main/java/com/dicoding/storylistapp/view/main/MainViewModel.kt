package com.dicoding.storylistapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storylistapp.data.UserRepository
import com.dicoding.storylistapp.data.pref.UserModel
import kotlinx.coroutines.launch

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
    val StoryListItem: LiveData<Result<List<listStoryItem>>> = _storyListItem

    fun getStories(token: String) {
        val liveData = repository.getStories(token)
        _storyListItem.addSource(liveData) { result ->
            _storyListItem.value = result
        }
    }
}
