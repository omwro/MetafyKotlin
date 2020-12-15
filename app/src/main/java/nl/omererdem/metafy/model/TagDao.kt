package nl.omererdem.metafy.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TagDao {
    @Query("SELECT * FROM tagTable")
    fun getAllTags(): LiveData<List<Tag>>

    @Insert
    fun insertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)
}