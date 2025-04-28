package com.example.databaselibraryapp.activity.recycler

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
sealed class Item : Parcelable {

    abstract fun isSameItem(other: Item): Boolean
    abstract val id: Int

    @Parcelize
    data class Book(

        val name: String, override val id: Int, val info: String
    ) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Book && id == other.id
    }

    @Parcelize
    data class Newspaper(
        val name: String, override val id: Int, val info: String
    ) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Newspaper && id == other.id
    }

    @Parcelize
    data class Disk(
        val name: String, override val id: Int, val info: String
    ) : Item() {
        override fun isSameItem(other: Item): Boolean = other is Disk && id == other.id
    }
}


