package nl.omererdem.metafy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_playlist.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongViewModel
import nl.omererdem.metafy.navController
import nl.omererdem.metafy.spotifyService
import nl.omererdem.metafy.utils.SpotifyPlaylistSongAdapter

val MY_LIKED_SONGS_PLAYLIST_ID = "metafy:mylikedsongs"

class PlaylistFragment : Fragment() {
    private var playlistId: String? = null

    private var songs = arrayListOf<Song>()

    private val songAdapter = SpotifyPlaylistSongAdapter(songs, this)

    val songViewModel: SongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            playlistId = it.getString("playlistId")
        }
        if (playlistId == null) {
            navController.popBackStack()
        }
        initViews()
        getPlaylist()
        getSongs()
    }

    private fun initViews() {
        rvPlaylist.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvPlaylist.adapter = songAdapter
    }

    private fun getPlaylist() {
        if (playlistId.toString() == MY_LIKED_SONGS_PLAYLIST_ID) {
            tvPlaylistTitle.text = resources.getString(R.string.my_liked_songs)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
            val playlistTitle = spotifyService?.getUserPlaylist(playlistId.toString())?.name
                CoroutineScope(Dispatchers.Main).launch {
                    tvPlaylistTitle.text = playlistTitle
                }
            }
        }
    }

    private fun getSongs() {
        songs.clear()
        pbLoadingPlaylist.visibility = ProgressBar.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            if (playlistId.toString() == MY_LIKED_SONGS_PLAYLIST_ID) {
                spotifyService?.getUserLikedSongs()?.map { Song.createFromSavedTrack(it) }?.let {
                    songs.addAll(it)
                }
            } else {
                spotifyService?.getPlaylistTracks(playlistId.toString())
                    ?.map { Song.createFromPlayListTrack(it) }?.let {
                        songs.addAll(it)
                    }
            }
            CoroutineScope(Dispatchers.Main).launch {
                songAdapter.notifyDataSetChanged()
                pbLoadingPlaylist.visibility = ProgressBar.INVISIBLE
            }
        }
    }
}