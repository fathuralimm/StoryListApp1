package com.dicoding.storylistapp.view.add

import com.dicoding.storylistapp.data.pref.UserModel

class AddstoryViewModel(private val repository: UserRepository): ViewModel() {

    private val _addStoryResponse = MediatorLiveData<Result<AddResponse>>()
    val addStoryResponse: LiveData<Result<AddResponse>> = _addStoryResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addStory(
        token: String,
        file: MultiplayerBody.Part,
        description: RequestBody
    ) {
        val liveData = repository.addStory(token, file, description)
        _addStoryResponse.addSource(liveData) { result ->
            _addStoryResponse.value = result
        }
    }
}