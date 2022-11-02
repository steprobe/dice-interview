package com.steprobe.diceinterview.features.artistsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.steprobe.diceinterview.R

class ArtistViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val title: TextView = root.findViewById(R.id.artistTitle)
    val origin: TextView = root.findViewById(R.id.artistOrigin)
    val tags: ChipGroup = root.findViewById(R.id.artistTags)
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
        holder.origin.text = item.origin
        holder.tags.visibility = if (item.tags?.isNotEmpty() == true) View.VISIBLE else View.GONE

        val context = holder.tags.context
        val inflater = LayoutInflater.from(context)

        holder.tags.removeAllViews()
        item.tags?.take(3)?.forEach { tag ->
            val chip = inflater.inflate(R.layout.chip_tag, holder.tags, false) as Chip
            chip.text = tag
            holder.tags.addView(chip)
        }

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
