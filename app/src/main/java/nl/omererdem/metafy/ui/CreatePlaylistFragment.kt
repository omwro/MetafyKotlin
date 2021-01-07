package nl.omererdem.metafy.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Playlist
import kotlinx.android.synthetic.main.fragment_create_playlist.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val previewSongAdapter = LocalSongAdapter(previewSongs, this)

    private var defaultTags = arrayListOf<Tag>()
    private val tagViewModel: TagViewModel by viewModels()

    val songViewModel: SongViewModel by viewModels()

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
        btnEqualCreate.setOnClickListener {
            addCombination("=")
        }
        btnCreatePlaylist.setOnClickListener {
            createPlaylist(previewSongs)
        }
        btnCombinationBackspace.setOnClickListener {
            if (combination.list.isNotEmpty()) {
                removeCombination()
            }
        }
    }

    private fun getDefaultTags() {
        // Adapter for the dropdown menu
        tagViewModel.tags.observe(viewLifecycleOwner, { savedDefaultTags ->
            defaultTags = savedDefaultTags as ArrayList<Tag>
            menuTagsCreate.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.item_spinner,
                    defaultTags
                )
            )

            // Add tag to combination on click
            menuTagsCreate.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    addCombination((parent?.getItemAtPosition(position) as Tag).name)
                }
        })
    }

    // Add a combination to the list and update the frontend
    private fun addCombination(string: String) {
        combination.list.add(string)
        runBlocking {
            // Checks if the combination is valid
            if (combination.isValidCombination(tagViewModel)) {
                tvCombination.setText(combination.getCombinationString())
                CoroutineScope(Dispatchers.IO).launch {
                    val playlist = combination.getPlaylist(tagViewModel, songViewModel)
                    if (playlist != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            previewSongs.clear()
                            previewSongs.addAll(playlist)
                            previewSongAdapter.notifyDataSetChanged()
                        }
                    }
                }
            } else {
                // If the combination is invalid, it removes the last entry and shows an error toast
                combination.list.removeLast()
                Toast.makeText(context, resources.getString(R.string.combination_not_allowed), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // remove the last entry in the combination list
    fun removeCombination() {
        combination.list.removeLast()
        tvCombination.setText(combination.getCombinationString())
        CoroutineScope(Dispatchers.IO).launch {
            val playlist = combination.getPlaylist(tagViewModel, songViewModel)
            CoroutineScope(Dispatchers.Main).launch {
                if (combination.list.isEmpty()) {
                    previewSongs.clear()
                    previewSongAdapter.notifyDataSetChanged()
                }
                else if (playlist != null) {
                    previewSongs.clear()
                    previewSongs.addAll(playlist)
                    previewSongAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    // Create the final playlist and upload it to spotify
    private fun createPlaylist(localSongs: List<Song>) {
        CoroutineScope(Dispatchers.IO).launch {
            val songIdStrings = getSongIdStringFromList(localSongs)
            val playlist: Playlist? =
                spotifyService?.createPlaylist(etPlaylistName.text.toString(), songIdStrings)
            if (playlist != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    navController.navigate(
                        R.id.playlistFragment,
                        bundleOf("playlistId" to playlist.id)
                    )
                }
            }
        }
    }

    // Create the song id query for inserting songs into a playlist
    private fun getSongIdStringFromList(songs: List<Song>): ArrayList<String> {
        val list: ArrayList<String> = arrayListOf()
        for (song in songs) {
            list.add(song.songId)
        }
        return list
    }
}