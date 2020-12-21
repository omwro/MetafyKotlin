package nl.omererdem.metafy.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_tag_big.view.*
import nl.omererdem.metafy.R
import nl.omererdem.metafy.model.Tag

class TagAdapter(private val tags: ArrayList<Tag>?, private val bigSize: Boolean) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun databind(tag: Tag) {
            itemView.tvTagName.text = tag.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (bigSize) {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_tag_big, parent, false)
            )
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tag_small, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tags?.get(position)?.let { holder.databind(it) }
    }

    override fun getItemCount(): Int {
        return tags?.size ?: 0
    }
}