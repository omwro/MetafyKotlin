package nl.omererdem.metafy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import kotlinx.android.synthetic.main.fragment_playlist.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.navController
import nl.omererdem.metafy.spotifyService
import nl.omererdem.metafy.utils.SpotifyPlaylistSongAdapter
import java.util.ArrayList

class PlaylistFragment : Fragment() {
    private var playlistId: String? = null

    private var songs = arrayListOf<PlaylistTrack>()

    private val songAdapter = SpotifyPlaylistSongAdapter(songs)

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
        val playlist = spotifyService?.getUserPlaylist(playlistId.toString())
        tvPlaylistTitle.text = playlist?.name
    }

    private fun getSongs() {
        songs.clear()
        songs.addAll(spotifyService?.getPlaylistTracks(playlistId.toString()) as ArrayList<PlaylistTrack>)
        songAdapter.notifyDataSetChanged()
    }
}