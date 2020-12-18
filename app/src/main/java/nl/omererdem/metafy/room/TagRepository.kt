package nl.omererdem.metafy.room

import android.content.Context
import androidx.lifecycle.LiveData
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagDao

class TagRepository(context: Context) {
    private var tagDao: TagDao

    init {
        val roomDatabase = RoomDatabase.getDatabase(context)
        tagDao = roomDatabase!!.tagDao()
    }

    fun getAllTags(): LiveData<List<Tag>> {
        return tagDao.getAllTags()
    }

    fun getTagByName(name: String): Tag? {
        return tagDao.getTagByName(name)
    }

    fun insertTag(tag: Tag) {
        tagDao.insertTag(tag)
    }

    suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(tag)
    }
}