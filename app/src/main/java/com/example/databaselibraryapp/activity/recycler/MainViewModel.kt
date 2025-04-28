package com.example.databaselibraryapp.activity.recycler

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.databaselibraryapp.activity.AppDatabase
import com.example.databaselibraryapp.activity.ItemEntity
import com.example.databaselibraryapp.activity.MainActivity.Companion.SORT_BY_NAME
import com.example.databaselibraryapp.activity.recycler.LibraryFragment.Companion.BOOK
import com.example.databaselibraryapp.activity.recycler.LibraryFragment.Companion.DISK
import com.example.databaselibraryapp.activity.recycler.LibraryFragment.Companion.NEWSPAPER
import com.example.databaselibraryapp.activity.recycler.adapters.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).itemDao()

    private val _scrollToPosition = MutableSharedFlow<Int?>(replay = 1)
    val scrollToPosition: SharedFlow<Int?> = _scrollToPosition.asSharedFlow()
    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private var currentOffset = 0
    private var currentLimit = 24
    private var currentSort = SORT_BY_NAME
    private var totalItems = 0

    fun resetScrollPosition() {
        viewModelScope.launch {
            _scrollToPosition.emit(null)
        }
    }

    fun updateSort(sortType: String) {
        viewModelScope.launch {
            currentSort = sortType
            currentOffset = 0
            loadInitialData()
        }
    }

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _state.value = State.Loading

            totalItems = dao.getCount()
            val entities = dao.getItems(0, currentLimit, currentSort)
            _state.value = State.Content(entities.mapToItems())

        }
    }

    fun loadMore(forward: Boolean) {
        viewModelScope.launch {
            if (forward && currentOffset + currentLimit >= totalItems) return@launch

            _state.value = State.LoadingMore

            val newOffset = if (forward) {
                currentOffset + currentLimit
            } else {
                max(0, currentOffset - currentLimit / 2)
            }

            val entities = dao.getItems(newOffset, currentLimit, currentSort)
            val currentItems = (_state.value as? State.Content)?.items ?: emptyList()

            val newItems = if (forward) {
                currentItems.drop(currentLimit / 2) + entities.mapToItems()
            } else {
                entities.mapToItems() + currentItems.dropLast(currentLimit / 2)
            }

            currentOffset = newOffset
            _state.value = State.Content(newItems)

        }
    }

    private fun List<ItemEntity>.mapToItems(): List<Item> = this.map { entity ->
        when (entity.type) {
            BOOK -> Item.Book(entity.name, entity.id, entity.info)
            NEWSPAPER -> Item.Newspaper(entity.name, entity.id, entity.info)
            DISK -> Item.Disk(entity.name, entity.id, entity.info)
            else -> throw IllegalArgumentException("Unknown type")
        }
    }

    fun createNewItem(type: String, name: String, info: String) {
        viewModelScope.launch {
            val itemEntity = ItemEntity(type = type, name = name, info = info)
            dao.insert(itemEntity)
        }
    }

    fun removeItem(position: Int) {
        viewModelScope.launch {
            val currentItems = (state.value as? State.Content)?.items ?: return@launch
            val entity = when (val itemToDelete = currentItems[position]) {
                is Item.Book -> ItemEntity(
                    itemToDelete.id,
                    BOOK,
                    itemToDelete.name,
                    itemToDelete.info
                )

                is Item.Newspaper -> ItemEntity(
                    itemToDelete.id,
                    NEWSPAPER,
                    itemToDelete.name,
                    itemToDelete.info
                )

                is Item.Disk -> ItemEntity(
                    itemToDelete.id,
                    DISK,
                    itemToDelete.name,
                    itemToDelete.info
                )
            }
            dao.delete(entity)
            val updatedList = currentItems.toMutableList().apply { removeAt(position) }
            _state.value = State.Content(updatedList)
        }
    }
}






