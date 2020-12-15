package nl.omererdem.metafy.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track
import kotlinx.android.synthetic.main.item_playlist.view.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.spotifyService
import kotlin.math.floor

class PlaylistAdapter(private val playlists: ArrayList<SimplePlaylist>) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(playlist: SimplePlaylist) {
            itemView.tvItemTitle.text = playlist.name
            itemView.tvItemAmount.text =
                itemView.resources.getString(R.string.amount_songs, playlist.tracks.total)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}