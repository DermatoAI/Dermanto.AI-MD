package com.dermatoai.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dermatoai.R
import com.dermatoai.api.Diskusi
import com.google.firebase.auth.FirebaseAuth

class DiscussionAdapter(
    private val onDeleteClick: (String) -> Unit,
    private val onItemClick: (Diskusi) -> Unit,
    private val onLiked: (Int, Diskusi) -> Unit
) : ListAdapter<Diskusi, DiscussionAdapter.DiscussionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discussion, parent, false)
        return DiscussionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick, onItemClick, onLiked)
    }

    class DiscussionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvUsername)
        private val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        private val rvImages: RecyclerView = itemView.findViewById(R.id.rvImages)

        fun bind(
            item: Diskusi,
            onDeleteClick: (String) -> Unit,
            onItemClick: (Diskusi) -> Unit,
            onLiked: (Int, Diskusi) -> Unit
        ) {
            Glide.with(itemView.context)
                .load(item.images)
                .into(ivProfile)
            titleTextView.text = item.judul
            usernameTextView.text = item.authorId
            dateTextView.text = item.timestamp
            descriptionTextView.text = item.isi
            deleteImageView.setOnClickListener { onDeleteClick(item.id) }
            deleteImageView.visibility =
                if (item.authorId == FirebaseAuth.getInstance().uid.orEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            ivFavorite.setImageDrawable(
                if (item.isFavorite) {
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_favorite)
                } else {
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_unfavorite)
                }
            )
            ivFavorite.setOnClickListener {
                onLiked(absoluteAdapterPosition, item)
            }
            itemView.rootView.setOnClickListener {
                onItemClick(item)
            }
            val adapter = ImageListAdapter(itemView.context, listOf(item.images))
            rvImages.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            rvImages.adapter = adapter
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Diskusi>() {
            override fun areItemsTheSame(oldItem: Diskusi, newItem: Diskusi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Diskusi, newItem: Diskusi): Boolean {
                return oldItem == newItem
            }
        }
    }
}