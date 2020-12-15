package nl.omererdem.metafy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.Track
import kotlinx.android.synthetic.main.item_song.view.*
import nl.omererdem.metafy.R

class SongAdapter(private val songs: ArrayList<PlaylistTrack>) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(tempSong: PlaylistTrack) {
            val song: Track = tempSong.track as Track
            itemView.tvItemTitle.text = song.name
            itemView.tvItemArtist.text = song.artists.firstOrNull()?.name
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