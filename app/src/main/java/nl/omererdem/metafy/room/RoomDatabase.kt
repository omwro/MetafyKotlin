package nl.omererdem.metafy.room

import android.content.Context
import androidx.room.*
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongDao
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagDao
import nl.omererdem.metafy.utils.PropertyConverter

@Database(entities = [Tag::class, Song::class], version = 1, exportSchema = false)
@TypeConverters(PropertyConverter::class)
abstract class RoomDatabase : androidx.room.RoomDatabase() {
    abstract fun tagDao(): TagDao
    abstract fun songDao(): SongDao

    companion object {
        private const val DATABASE_NAME = "METAFY_DATABASE"

        @Volatile
        private var roomDatabase: RoomDatabase? = null

        fun getDatabase(context: Context): RoomDatabase? {
            if (roomDatabase == null) {
                synchronized(RoomDatabase::class.java) {
                    if (roomDatabase == null) {
                        roomDatabase = Room.databaseBuilder(
                            context.applicationContext,
                            RoomDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return roomDatabase
        }
    }
}