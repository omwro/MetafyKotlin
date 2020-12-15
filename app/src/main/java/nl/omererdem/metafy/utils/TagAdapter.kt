package nl.omererdem.metafy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track
import kotlinx.android.synthetic.main.item_song.view.*
import kotlinx.android.synthetic.main.item_tag.view.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Tag

class TagAdapter(private val tags: ArrayList<Tag>) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(tag: Tag) {
            itemView.tvTagName.text = tag.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(tags[position])
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}