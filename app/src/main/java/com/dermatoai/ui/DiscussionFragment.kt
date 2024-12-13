package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentForumBinding
import com.dermatoai.helper.DiscussionAdapter
import com.dermatoai.model.DiscussionViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscussionFragment : Fragment() {

    private val viewModel: DiscussionViewModel by viewModels()
    private lateinit var adapter: DiscussionAdapter
    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DiscussionAdapter(
            onDeleteClick = { discussionId ->
                viewModel.deleteDiscussion(
                    discussionId,
                    FirebaseAuth.getInstance().uid.orEmpty(),
                    true
                )
            },
            onItemClick = {
                val action = DiscussionFragmentDirections.actionForumFragmentToForumDetailMenu(
                    discussionId = it.id
                )
                findNavController().navigate(action)
            },
            onLiked = { index, diskusi ->
                viewModel.like(index, diskusi, FirebaseAuth.getInstance().uid.orEmpty())
            }
        )
        binding.rvDiscussions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDiscussions.adapter = adapter
        binding.fab.setOnClickListener {
            val action = DiscussionFragmentDirections.actionForumFragmentToForumAddMenu()
            findNavController().navigate(action)
        }

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
                        binding.rvDiscussions.visibility = View.GONE
                        binding.loading.visibility = View.VISIBLE
                    } else {
                        binding.rvDiscussions.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.fetchDiscussions(FirebaseAuth.getInstance().uid.orEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
