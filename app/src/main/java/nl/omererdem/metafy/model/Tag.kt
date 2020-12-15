package nl.omererdem.metafy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tagTable")
class Tag(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Tag(name='$name', id=$id)"
    }
}