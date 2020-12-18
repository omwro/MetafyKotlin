package nl.omererdem.metafy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.SimplePlaylist
import kotlinx.android.synthetic.main.fragment_create_playlist.*
import kotlinx.coroutines.runBlocking
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongViewModel
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagViewModel
import nl.omererdem.metafy.navController
import nl.omererdem.metafy.spotifyService
import nl.omererdem.metafy.utils.Combination
import nl.omererdem.metafy.utils.LocalSongAdapter

class CreatePlaylistFragment : Fragment() {

    private var previewSongs = arrayListOf<Song>()
    private val previewSongAdapter = LocalSongAdapter(previewSongs)

    private var defaultTags = arrayListOf<Tag>()
    private val tagViewModel: TagViewModel by viewModels()

    private val songViewModel: SongViewModel by viewModels()

    private val combination = Combination(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getDefaultTags()
    }

    private fun initViews() {
        rvCreatePlaylist.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvCreatePlaylist.adapter = previewSongAdapter
        btnPlusCreate.setOnClickListener {
            addCombination("+")
        }
        btnMinusCreate.setOnClickListener {
            addCombination("-")
        }
        btnIsCreate.setOnClickListener {
            addCombination("=")
        }
        btnCreatePlaylist.setOnClickListener {
            createPlaylist(previewSongs)
        }
    }

    private fun getDefaultTags() {
        tagViewModel.tags.observe(viewLifecycleOwner, { savedDefaultTags ->
            defaultTags = savedDefaultTags as ArrayList<Tag>
            menuTagsCreate.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.item_spinner,
                    defaultTags
                )
            )
            menuTagsCreate.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    addCombination((parent?.getItemAtPosition(position) as Tag).name)
                }
        })
    }

    private fun addCombination(string: String) {
        combination.list.add(string)
        etCombination.setText(combination.getCombinationString())
        runBlocking {
            val playlist = combination.getPlaylist(tagViewModel, songViewModel)
            if (playlist != null) {
                previewSongs.clear()
                previewSongs.addAll(playlist)
                previewSongAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun createPlaylist(localSongs: List<Song>) {
        val songIdStrings = getSongIdStringFromList(localSongs)
        val playlist: SimplePlaylist? =
            spotifyService?.createPlaylist(etPlaylistName.text.toString(), songIdStrings)
        if (playlist != null) {
            Log.e("NEW PLAYLIST", playlist.toString())
            navController.navigate(R.id.playlistFragment, bundleOf("playlistId" to playlist.id))
        }
    }

    private fun getSongIdStringFromList(songs: List<Song>): String {
        val IDENTIFIER = "spotify:track:"
//        val list: ArrayList<String> = arrayListOf()
//        for (song in songs) {
//            list.add(song.songId)
//        }
//        return list
        var string = ""
        if (songs.size > 1) {
//            string += "uris="
        }
        for (song in songs) {
            if (string.contains(IDENTIFIER)) {
                string += ","
            }
            string += IDENTIFIER+song.songId
        }
        Log.e("songidstring", string)
        return string
    }
}