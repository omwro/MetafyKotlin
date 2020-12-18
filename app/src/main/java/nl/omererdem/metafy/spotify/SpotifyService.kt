package nl.omererdem.metafy.spotify

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.spotifyClientPkceApi

class SpotifyService(token: String) {
    var api: SpotifyClientApi = spotifyClientPkceApi(
        SpotifyAuthenticator().CLIENT_ID,
        SpotifyAuthenticator().REDIRECTED_URI,
        token,
        SpotifyAuthenticator().CODE_VERIFIER
    ).build()

    fun getUserPlaylists(): List<SimplePlaylist> {
        return api.playlists.getClientPlaylists().getAllItems().complete() as List<SimplePlaylist>
    }

    fun getUserPlaylist(playlistId: String): SimplePlaylist {
        return api.playlists.getClientPlaylist(playlistId).complete() as SimplePlaylist
    }

    fun getPlaylistTracks(playlistId: String): List<PlaylistTrack> {
        return api.playlists.getPlaylistTracks(playlistId).getAllItems().complete() as List<PlaylistTrack>
    }

    fun getSong(songId: String): Track {
        return api.tracks.getTrack(songId).complete() as Track
    }

    fun createPlaylist(name: String, songIdStrings: String): SimplePlaylist? {
//        val playlist = api.playlists.createClientPlaylist(
//            name,
//            "Playlist created by MetaFy",
//            false
//        ).complete()
        val playlist: SimplePlaylist? = api.playlists.getClientPlaylist("554D71ve3fC1P2gl1t2u8i").complete()
        api.playlists.addTracksToClientPlaylist(playlist?.id.toString(), songIdStrings).complete()
        return playlist
    }
}