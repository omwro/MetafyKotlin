package nl.omererdem.metafy.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nl.omererdem.metafy.model.Tag

class TagsConverter {
    private val gson = Gson()

    @TypeConverter
    fun tagsToString(tags: ArrayList<Tag>?): String {
        return gson.toJson(tags)
    }

    @TypeConverter
    fun stringToTags(string: String): ArrayList<Tag>? {
        val tagType = object : TypeToken<List<Tag>>() {}.type
        return gson.fromJson(string, tagType)
    }
}