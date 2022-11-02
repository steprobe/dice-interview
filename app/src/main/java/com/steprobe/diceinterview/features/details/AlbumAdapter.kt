package com.steprobe.diceinterview.features.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.steprobe.diceinterview.R

class AlbumViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val title: TextView = root.findViewById(R.id.albumTitle)
    val releaseYear: TextView = root.findViewById(R.id.albumReleaseDate)
    val albumCover: ImageView = root.findViewById(R.id.albumCover)
}

class AlbumAdapter : ListAdapter<AlbumDisplayModel, AlbumViewHolder>(
    AlbumDisplayModelDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.releaseYear.text = item.date

        if (item.image != null) {
            Glide.with(holder.albumCover.context)
                .load(item.image)
                .placeholder(R.drawable.ic_album_placeholder)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.albumCover)
        } else {
            holder.albumCover.setImageResource(R.drawable.ic_album_placeholder)
        }
    }
}

class AlbumDisplayModelDiffCallback : DiffUtil.ItemCallback<AlbumDisplayModel>() {
    override fun areContentsTheSame(
        oldItem: AlbumDisplayModel,
        newItem: AlbumDisplayModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: AlbumDisplayModel,
        newItem: AlbumDisplayModel
    ): Boolean {
        return oldItem.id == newItem.id
    }
}
