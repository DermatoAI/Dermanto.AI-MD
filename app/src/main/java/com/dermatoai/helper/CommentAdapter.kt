package com.dermatoai.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.api.Diskusi
import com.dermatoai.databinding.ItemCommentBinding

class CommentAdapter(private val onDeleteClick: (String) -> Unit) :
    ListAdapter<Diskusi, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Diskusi) {
            binding.tvUsername.text = comment.authorId
            binding.tvDate.text = comment.timestamp
            binding.tvDescription.text = comment.isi
            binding.ivDelete.setOnClickListener {
                onDeleteClick(comment.id)
            }
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Diskusi>() {
        override fun areItemsTheSame(oldItem: Diskusi, newItem: Diskusi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diskusi, newItem: Diskusi): Boolean {
            return oldItem == newItem
        }
    }
}
