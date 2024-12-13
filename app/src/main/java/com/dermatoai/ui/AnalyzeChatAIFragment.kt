package com.dermatoai.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.databinding.FragmentAnalyzeChatBinding
import com.dermatoai.helper.ChatData
import com.dermatoai.model.ChatAiViewModel
import com.dermatoai.model.ChatListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class AnalyzeChatAIFragment : Fragment() {
    private val viewModel: ChatAiViewModel by activityViewModels()

    private lateinit var binding: FragmentAnalyzeChatBinding

    private val adapter = ChatListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyzeChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val chatDataList = listOf(
            ChatData(
                message = "Hi, how are you?",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:00:00")!!
            ),
            ChatData(
                message = "I'm good, thanks! And you?",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:01:00")!!
            ),
            ChatData(
                message = "I'm doing great. Did you finish the report?",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:02:00")!!
            ),
            ChatData(
                message = "Yes, I submitted it this morning.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:03:00")!!
            ),
            ChatData(
                message = "Awesome! Let's discuss the next steps.",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:05:00")!!
            ),
            ChatData(
                message = "Sure, when are you free to talk?",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:06:00")!!
            ),
            ChatData(
                message = "I can call you after 3 PM.",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:07:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Sounds good. I'll be ready.",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:08:00")!!
            ),
            ChatData(
                message = "Great, talk to you then!",
                isSender = true,
                time = dateFormat.parse("2024-12-10 10:09:00")!!
            ),
            ChatData(
                message = "Bye for now!",
                isSender = false,
                time = dateFormat.parse("2024-12-10 10:10:00")!!
            )
        )
        uiBind()
        observeSection()
    }

    private fun observeSection() {
        viewModel.chatMessages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.chatContainerList.smoothScrollToPosition(it.size - 1)
        }
    }

    private fun uiBind() {
        binding.chatContainerList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.sendIconButton.setOnClickListener {
            hideKeyboard()
            val message = binding.messageTextInput.text.toString()
            viewModel.addMessage(ChatData(message, true, Date()))
            print(message)
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.requestChatbot(message)
            }
            binding.messageTextInput.text?.clear()
        }
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}