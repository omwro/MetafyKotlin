package nl.omererdem.metafy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.SavedTrack
import com.adamratzman.spotify.models.SimpleArtist
import com.adamratzman.spotify.models.Track

@Entity(tableName = "songTable")
class Song(
    var songId: String,
    var name: String,
    var artists: ArrayList<String>,
    var duration: SongDuration,
    var tags: ArrayList<Tag>?,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
) {
    override fun toString(): String {
        return "Song(songId='$songId', name='$artists', duration='$duration', tags=$tags, id=$id)"
    }

    companion object {
        fun createFromTrack(track: Track): Song {
            return Song(
                track.id,
                track.name,
                getSpotifyArtistsList(track.artists),
                SongDuration.createFromMilliseconds(track.durationMs),
                null
            )
        }

        fun createFromPlayListTrack(playlistTrack: PlaylistTrack): Song {
            return createFromTrack(playlistTrack.track as Track)
        }

        fun createFromSavedTrack(savedTrack: SavedTrack): Song {
            return createFromTrack(savedTrack.track)
        }

        private fun getSpotifyArtistsList(artists: List<SimpleArtist>?): ArrayList<String> {
            val list: ArrayList<String> = arrayListOf()
            if (artists != null) {
                for (artist in artists) {
                    list.add(artist.name)
                }
            }
            return list
        }
    }
}