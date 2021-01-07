package nl.omererdem.metafy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tagTable")
class Tag(
    var name: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
) {
    override fun toString(): String {
        return name
    }

    fun equals(other: Tag): Boolean {
        return this.id == other.id
    }
}