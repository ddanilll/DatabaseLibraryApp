package com.example.databaselibraryapp.activity.recycler.adapters.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.databaselibraryapp.activity.recycler.Item

class ItemDiffUtil(
    private val oldList: List<Item>, private val newList: List<Item>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos].id == newList[newPos].id
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}

