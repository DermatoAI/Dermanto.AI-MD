package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.R
import com.dermatoai.databinding.FragmentDiscussionDetailBinding
import com.dermatoai.model.DiscussionDetailViewModel
import com.dermatoai.ui.adapter.CommentAdapter
import com.dermatoai.ui.adapter.ImageListAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscussionDetailFragment : Fragment() {

    private val viewModel: DiscussionDetailViewModel by viewModels()
    private var _binding: FragmentDiscussionDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscussionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discussionId = arguments?.getInt("discussionId") ?: return

        viewModel.fetchDiscussionDetail(discussionId)

        val adapter = CommentAdapter { commentId ->
            viewModel.deleteComment(commentId)
        }
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.discussionDetail.collect { detail ->
                detail?.let {
                    binding.tvDiscussionTitle.text = it.judul
                    binding.post.tvUsername.text = it.pengguna.username
                    binding.post.tvDate.text = it.timestamp
                    binding.post.tvDescription.text = it.isi
                    binding.post.ivDelete.visibility = if (it.pengguna.id == FirebaseAuth.getInstance().uid.orEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                    binding.post.ivFavorite.setImageDrawable(
                        if (it.isFavorite) {
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
                        } else {
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_unfavorite)
                        }
                    )
                    val imageAdapter = ImageListAdapter(requireContext(), it.images)
                    binding.post.rvImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.post.rvImages.adapter = imageAdapter
                    adapter.submitList(listOf(detail))
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.post.ivDelete.setOnClickListener {
            viewModel.deleteDiscussion(discussionId)
        }

        binding.ivSend.setOnClickListener {
            val comment = binding.etMessage.text.toString()
            if (comment.isNotEmpty()) {
                viewModel.addComment(
                    discussionId,
                    comment,
                    FirebaseAuth.getInstance().uid.orEmpty()
                )
                binding.etMessage.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
