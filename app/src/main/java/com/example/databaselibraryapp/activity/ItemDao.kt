package com.example.databaselibraryapp.activity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT COUNT(*) FROM items")
    suspend fun getCount(): Int

    @Query(
        """
        SELECT * FROM items 
        ORDER BY 
            CASE WHEN :sortBy = 'name' THEN name END COLLATE NOCASE ASC,
            CASE WHEN :sortBy = 'date' THEN createdAt END DESC 
        LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getItems(offset: Int, limit: Int, sortBy: String): List<ItemEntity>

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Insert
    suspend fun insert(item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)
}



