package nl.omererdem.metafy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.android.synthetic.main.fragment_library.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Playlist
import nl.omererdem.metafy.spotifyService
import nl.omererdem.metafy.utils.PlaylistAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LibraryFragment : Fragment() {

    // List of the games in the history
    private var playlists = arrayListOf<Playlist>()

    // Adapter for the game object
    private val playlistAdapter = PlaylistAdapter(playlists)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getPlaylists()
    }

    private fun initViews() {
        rvLibrary.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvLibrary.adapter = playlistAdapter
    }

    private fun getPlaylists() {
        playlists.clear()
        playlists.add(
            Playlist(
                "metafy:mylikedsongs",
                "My Liked Songs",
                spotifyService?.getUserLikedSongs()?.size ?: 0
            )
        )
        spotifyService?.getUserPlaylists()?.map {
            playlists.add(Playlist(it.id, it.name, it.tracks.total))
        }
        playlistAdapter.notifyDataSetChanged()
    }
}
