package com.android.plantpal.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.plantpal.data.remote.response.ListItemDiscussions

@Dao
interface DiscussionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscussion(discussion: List<ListItemDiscussions>)

    @Query("SELECT * FROM discussion ORDER BY id DESC")
    fun getAllDiscussion(): PagingSource<Int, ListItemDiscussions>

    @Query("DELETE FROM discussion")
    suspend fun deleteAll()
}