package com.dermatoai.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.R
import com.dermatoai.api.Diskusi

class DiscussionAdapter(
    private val onDeleteClick: (Int) -> Unit,
    private val onItemClick: (Diskusi) -> Unit
) : ListAdapter<Diskusi, DiscussionAdapter.DiscussionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discussion, parent, false)
        return DiscussionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick, onItemClick)
    }

    class DiscussionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvUsername)
        private val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete)

        fun bind(item: Diskusi, onDeleteClick: (Int) -> Unit, onItemClick: (Diskusi) -> Unit) {
            usernameTextView.text = item.pengguna.username
            dateTextView.text = item.timestamp
            descriptionTextView.text = item.isi
            deleteImageView.setOnClickListener { onDeleteClick(item.id) }
            itemView.rootView.setOnClickListener {
                onItemClick(item)
            }
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