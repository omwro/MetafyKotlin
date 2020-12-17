package nl.omererdem.metafy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songTable")
class Song(
    var songId: String,

    var tags: ArrayList<Tag>?,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Song(songId='$songId', tags=$tags, id=$id)"
    }
}