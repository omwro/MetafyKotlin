package nl.omererdem.metafy.spotify

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.public.SearchApi
import com.adamratzman.spotify.models.*
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

    fun createPlaylist(name: String, songIdStrings: List<String>): Playlist {
        val playlist = api.playlists.createClientPlaylist(
            name,
            "Playlist created by MetaFy",
            false
        ).complete()
        api.playlists.addTracksToClientPlaylist(playlist.id, *songIdStrings.toTypedArray()).complete()
        return playlist
    }

    fun getSearch(keyword: String): List<Track>? {
        return api.search.search(keyword, SearchApi.SearchType.TRACK).complete().tracks?.items
    }

    fun getUserLikedSongs(): List<Track> {
        return api.library.getSavedTracks().getAllItems().complete() as List<Track>
    }
}