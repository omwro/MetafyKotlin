package nl.omererdem.metafy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_song.view.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.navController

class LocalSongAdapter(private val songs: ArrayList<Song>) :
    RecyclerView.Adapter<LocalSongAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(song: Song) {
            itemView.tvItemTitle.text = song.name
            itemView.tvItemArtist.text = song.artists.firstOrNull()
            itemView.setOnClickListener {
                navController.navigate(R.id.songFragment, bundleOf("songId" to song.id))
            }
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