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
import com.dermatoai.databinding.FragmentLikesBinding
import com.dermatoai.model.LikesViewModel
import com.dermatoai.ui.adapter.LikesAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikesFragment : Fragment() {

    private val viewModel: LikesViewModel by viewModels()
    private lateinit var adapter: LikesAdapter
    private var _binding: FragmentLikesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LikesAdapter(
            onDeleteClick = { likesEntity ->
                viewModel.deleteLikes(likesEntity, FirebaseAuth.getInstance().uid.orEmpty())
            },
            onItemClick = {
//                <fragment
//                android:id="@+id/discussionDetailFragment"
//                android:name="DiscussionDetailFragment"
//                android:label="Discussion Detail"
//                tools:layout="@layout/fragment_discussion_detail">
//                <argument
//                android:name="discussionId"
//                app:argType="integer" />
//                </fragment>
//                val action = DiscussionFragmentDirections.actionDiscussionFragmentToDiscussionDetailFragment(discussionId)
//                findNavController().navigate(action)
            },
            onLiked = {
                viewModel.deleteLikes(it, FirebaseAuth.getInstance().uid.orEmpty())
            }
        )
        binding.rvLikes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLikes.adapter = adapter

        lifecycleScope.launch {
            viewModel.likes.collect { discussions ->
                discussions?.let {
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
                        binding.rvLikes.visibility = View.GONE
                        binding.loading.visibility = View.VISIBLE
                    } else {
                        binding.rvLikes.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.fetchLikesByUserId(FirebaseAuth.getInstance().uid.orEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}