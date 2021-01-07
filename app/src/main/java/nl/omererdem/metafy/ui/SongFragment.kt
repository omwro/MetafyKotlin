package nl.omererdem.metafy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.fragment_song.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.*
import nl.omererdem.metafy.navController
import nl.omererdem.metafy.spotifyService
import nl.omererdem.metafy.utils.TagAdapter

class SongFragment : Fragment() {
    private lateinit var song: Song
    private lateinit var songId: String
    private var defaultTags = arrayListOf<Tag>()
    private var songTags: ArrayList<Tag> = arrayListOf()
    private val songTagAdapter = TagAdapter(songTags, true)
    private val tagViewModel: TagViewModel by viewModels()
    private val songViewModel: SongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            if (it.getString("songId") == null) {
                navController.popBackStack()
                return
            } else {
                songId = it.getString("songId")!!
            }
        }
        initView()
        getLocalSong()
        getDefaultTags()
    }

    private fun initView() {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.alignItems = AlignItems.CENTER
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        rvTagsSong.layoutManager = flexboxLayoutManager
        rvTagsSong.adapter = songTagAdapter
        deleteTag().attachToRecyclerView(rvTagsSong)
    }

    private fun getLocalSong() {
        pbLoadingSong.visibility = ProgressBar.VISIBLE
        songViewModel.getSongById(songId).observe(viewLifecycleOwner, { localSong ->
            if (localSong != null) {
                song = localSong
                songTags.clear()
                song.tags?.let { songTags.addAll(it) }
                songTags.sortBy { it.name }
                songTagAdapter.notifyDataSetChanged()
            } else {
                val spotifySong: Track? = spotifyService?.getSong(songId)
                if (spotifySong != null) {
                    song = Song.createFromTrack(spotifySong)
                    songViewModel.insertSong(song)
                } else {
                    navController.popBackStack()
                }
            }
            pbLoadingSong.visibility = ProgressBar.INVISIBLE
            tvSongName.text = song.name
            tvSongArtists.text = getArtistsString(song.artists)
            tvSongDuration.text = song.duration.longString()
        })
    }

    private fun getArtistsString(artists: ArrayList<String>): String {
        var string = ""
        for (artist in artists) {
            if (string.isNotBlank()) {
                string += ", "
            }
            string += artist
        }
        return string
    }

    private fun getDefaultTags() {
        tagViewModel.tags.observe(viewLifecycleOwner, { savedDefaultTags ->
            defaultTags = savedDefaultTags as ArrayList<Tag>
            menuTags.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.item_spinner,
                    defaultTags
                )
            )
            menuTags.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    saveTag(parent?.getItemAtPosition(position) as Tag)
                }
        })
    }

    private fun saveTag(tag: Tag) {
        if (songTags.any { songTag -> songTag.equals(tag)}) return
        if (song.tags == null) song.tags = arrayListOf(tag)
        else song.tags?.add(tag)
        songTags.add(tag)
        songViewModel.updateSong(song)
    }

    private fun deleteTag(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tag: Tag = songTags[viewHolder.adapterPosition]
                song.tags?.remove(tag)
                songTags.remove(tag)
                getDefaultTags()
                songViewModel.updateSong(song)
            }
        }
        return ItemTouchHelper(callback)
    }
}