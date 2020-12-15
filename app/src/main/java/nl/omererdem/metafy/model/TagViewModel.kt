package nl.omererdem.metafy.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.omererdem.metafy.room.TagRepository

class TagViewModel(application: Application): AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val repository = TagRepository(application.applicationContext)

    val tags: LiveData<List<Tag>> = repository.getAllTags()

    fun insertTag(tag: Tag) {
        ioScope.launch { repository.insertTag(tag) }
    }

    fun deleteTag(tag: Tag) {
        ioScope.launch { repository.deleteTag(tag) }
    }
}