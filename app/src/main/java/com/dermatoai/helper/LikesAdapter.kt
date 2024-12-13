package com.dermatoai.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.R
import com.dermatoai.room.LikesEntity

class LikesAdapter(
    private val onDeleteClick: (LikesEntity) -> Unit,
    private val onItemClick: (LikesEntity) -> Unit,
    private val onLiked: (LikesEntity) -> Unit
) : ListAdapter<LikesEntity, LikesAdapter.LikesViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discussion, parent, false)
        return LikesViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikesViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick, onItemClick, onLiked)
    }

    class LikesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvUsername)
        private val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        fun bind(
            item: LikesEntity,
            onDeleteClick: (LikesEntity) -> Unit,
            onItemClick: (LikesEntity) -> Unit,
            onLiked: (LikesEntity) -> Unit
        ) {
            usernameTextView.text = item.username
            dateTextView.text = item.timestamp
            descriptionTextView.text = item.isi
            deleteImageView.setOnClickListener { onDeleteClick(item) }
            ivFavorite.setOnClickListener { onLiked(item) }
            itemView.rootView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LikesEntity>() {
            override fun areItemsTheSame(oldItem: LikesEntity, newItem: LikesEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LikesEntity, newItem: LikesEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}