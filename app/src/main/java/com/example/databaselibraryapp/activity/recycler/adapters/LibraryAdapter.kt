package com.example.databaselibraryapp.activity.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.databaselibraryapp.R
import com.example.databaselibraryapp.activity.recycler.Item
import com.example.databaselibraryapp.activity.recycler.adapters.utils.ItemDiffUtil
import com.example.databaselibraryapp.activity.recycler.adapters.vh.BookViewHolder
import com.example.databaselibraryapp.activity.recycler.adapters.vh.DiskViewHolder
import com.example.databaselibraryapp.activity.recycler.adapters.vh.NewspaperViewHolder

class LibraryAdapter(
    val items: MutableList<Item>, private val onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateList(newItems: List<Item>) {
        val diffUtil = ItemDiffUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.BOOK.value, ViewType.NEWSPAPER.value, ViewType.DISK.value -> {
                val view = inflater.inflate(R.layout.layout_library_item, parent, false)
                when (viewType) {
                    ViewType.BOOK.value -> BookViewHolder(view, onItemClickListener)
                    ViewType.NEWSPAPER.value -> NewspaperViewHolder(view, onItemClickListener)
                    ViewType.DISK.value -> DiskViewHolder(view, onItemClickListener)
                    else -> throw IllegalArgumentException("Invalid view type")
                }
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.Book -> ViewType.BOOK.value
            is Item.Newspaper -> ViewType.NEWSPAPER.value
            is Item.Disk -> ViewType.DISK.value
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Item.Book -> (holder as BookViewHolder).bind(item)
            is Item.Newspaper -> (holder as NewspaperViewHolder).bind(item)
            is Item.Disk -> (holder as DiskViewHolder).bind(item)
        }
    }

    override fun getItemCount() = items.size
}




