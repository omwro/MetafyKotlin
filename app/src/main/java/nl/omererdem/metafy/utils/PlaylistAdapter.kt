package nl.omererdem.metafy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_playlist.view.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Playlist
import nl.omererdem.metafy.navController

class PlaylistAdapter(private val playlists: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(playlist: Playlist) {
            itemView.tvItemTitle.text = playlist.name
            itemView.tvItemAmount.text =
                itemView.resources.getString(R.string.amount_songs, playlist.totalSongs)
            itemView.setOnClickListener {
                navController.navigate(
                    R.id.action_libraryFragment_to_playlistFragment,
                    bundleOf("playlistId" to playlist.songId)
                )
            }
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