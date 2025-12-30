package com.example.databaselibraryapp.activity.recycler.adapters

import com.example.databaselibraryapp.activity.recycler.Item

sealed interface State {
    object Loading : State
    object LoadingMore : State
    data class Error(val message: String) : State
    data class Content(val items: List<Item>) : State
}

