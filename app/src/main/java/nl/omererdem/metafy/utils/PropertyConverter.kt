package nl.omererdem.metafy.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nl.omererdem.metafy.model.SongDuration
import nl.omererdem.metafy.model.Tag

class PropertyConverter {
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

    @TypeConverter
    fun artistsToString(artists: ArrayList<String>?): String {
        return gson.toJson(artists)
    }

    @TypeConverter
    fun stringToArtists(string: String): ArrayList<String>? {
        val artistType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, artistType)
    }

    @TypeConverter
    fun durationToInt(duration: SongDuration): Int {
        return duration.getTotalMilliseconds()
    }

    @TypeConverter
    fun intToDuration(milliseconds: Int): SongDuration {
        return SongDuration.createFromMilliseconds(milliseconds)
    }
}