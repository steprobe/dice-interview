package com.steprobe.diceinterview.features.artistsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steprobe.diceinterview.R

class ArtistViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val title: TextView = root.findViewById(R.id.artistTitle)
    val tags: TextView = root.findViewById(R.id.artistTags)
}

class ArtistsAdapter(val onItemClicked: (item: ArtistDisplayModel) -> Unit) :
    ListAdapter<ArtistDisplayModel, ArtistViewHolder>(
        ArtistDisplayModelDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {

        val item = getItem(position)

        holder.title.text = item.name
        holder.tags.text = item.tags?.take(3)?.joinToString()
        holder.root.setOnClickListener { onItemClicked(item) }
    }
}

class ArtistDisplayModelDiffCallback : DiffUtil.ItemCallback<ArtistDisplayModel>() {

    override fun areItemsTheSame(
        oldItem: ArtistDisplayModel,
        newItem: ArtistDisplayModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ArtistDisplayModel,
        newItem: ArtistDisplayModel
    ): Boolean {
        return oldItem == newItem
    }
}
