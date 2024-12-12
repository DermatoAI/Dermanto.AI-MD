package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dermatoai.api.TambahDiskusiRequest
import com.dermatoai.databinding.FragmentAddDiscussionBinding
import com.dermatoai.model.AddDiscussionViewModel
import com.google.firebase.auth.FirebaseAuth

class AddDiscussionFragment : Fragment() {

    private lateinit var binding: FragmentAddDiscussionBinding
    private val viewModel: AddDiscussionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDiscussionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val request = TambahDiskusiRequest(
                judul = binding.etTitle.text.toString(),
                isi = binding.etDescription.text.toString(),
                kategori = binding.etCategory.text.toString(),
                idPengguna = FirebaseAuth.getInstance().uid.orEmpty()
            )
            viewModel.addDiscussion(request)
        }

        viewModel.addDiscussionResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Add Discussion Success", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigateUp()
            }.onFailure {
                Toast.makeText(requireContext(), "Add Discussion Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
