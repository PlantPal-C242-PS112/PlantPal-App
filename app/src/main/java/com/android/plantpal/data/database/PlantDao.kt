package com.android.plantpal.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.plantpal.data.remote.response.Plant

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(user: List<Plant>)

    @Query("DELETE FROM plant")
    suspend fun deleteAll()
}