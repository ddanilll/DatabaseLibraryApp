package com.example.databaselibraryapp.activity.recycler.adapters.vh

import android.view.View
import com.example.databaselibraryapp.R
import com.example.databaselibraryapp.activity.recycler.Item
import com.example.databaselibraryapp.activity.recycler.adapters.LibraryAdapter


class NewspaperViewHolder(
    view: View,
    onItemClickListener: LibraryAdapter.OnItemClickListener?
) : BaseItemViewHolder(view, onItemClickListener) {

    override fun bind(item: Item) {
        this.item = item
        if (item !is Item.Newspaper) return

        nameView.text = item.name
        idView.text = itemView.context.getString(R.string.item_id_format, item.id)
        iconView.setImageResource(R.drawable.ic_newspaperimage)

    }
}
