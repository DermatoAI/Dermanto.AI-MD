package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentPostBinding
import com.dermatoai.helper.PostAdapter
import com.dermatoai.model.DiscussionViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private val viewModel: DiscussionViewModel by viewModels()
    private lateinit var adapter: PostAdapter
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PostAdapter(
            onDeleteClick = { discussionId ->
                viewModel.deleteDiscussion(discussionId, FirebaseAuth.getInstance().uid.orEmpty(), false)
            },
            onItemClick = {

            }
        )
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = adapter

        lifecycleScope.launch {
            viewModel.discussions.collect { discussions ->
                discussions?.data?.let {
                    adapter.submitList(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { loading ->
                loading?.let {
                    if (loading) {
                        binding.rvPosts.visibility = View.GONE
                        binding.loading.visibility = View.VISIBLE
                    } else {
                        binding.rvPosts.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.fetchDiscussionsByUserId(FirebaseAuth.getInstance().uid.orEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}