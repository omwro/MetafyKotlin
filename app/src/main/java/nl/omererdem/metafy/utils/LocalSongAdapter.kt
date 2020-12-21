package nl.omererdem.metafy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.item_song.view.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.navController
import nl.omererdem.metafy.ui.CreatePlaylistFragment

class LocalSongAdapter(
    private val songs: ArrayList<Song>,
    private val createPlaylistFragment: CreatePlaylistFragment
) :
    RecyclerView.Adapter<LocalSongAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(song: Song) {
            itemView.tvItemTitle.text = song.name
            itemView.tvItemArtist.text = song.artists.firstOrNull()
            itemView.setOnClickListener {
                navController.navigate(R.id.songFragment, bundleOf("songId" to song.songId))
            }

            val fragment = this@LocalSongAdapter.createPlaylistFragment

            val flexboxLayoutManager =
                FlexboxLayoutManager(fragment.context)
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.alignItems = AlignItems.CENTER
            flexboxLayoutManager.flexWrap = FlexWrap.NOWRAP
            itemView.rvItemTag.layoutManager = flexboxLayoutManager
            val tags: ArrayList<Tag> = arrayListOf()
            val adapter = TagAdapter(tags, false)
            itemView.rvItemTag.adapter = adapter

            fragment.songViewModel.getSongById(song.songId)
                .observe(fragment.viewLifecycleOwner, { savedSong ->
                    if (savedSong != null) {
                        val savedSongtags: ArrayList<Tag>? = savedSong.tags
                        if (savedSongtags != null) {
                            tags.clear()
                            tags.addAll(savedSongtags)
                            adapter.notifyDataSetChanged()
                        }
                    }
                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(songs[position])
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}