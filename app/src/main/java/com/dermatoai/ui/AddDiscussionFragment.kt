package com.dermatoai.ui

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dermatoai.databinding.FragmentAddDiscussionBinding
import com.dermatoai.model.AddDiscussionViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel

@AndroidEntryPoint
class AddDiscussionFragment : Fragment() {

    private lateinit var binding: FragmentAddDiscussionBinding
    private val viewModel: AddDiscussionViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivSelectedImage.setImageURI(uri)
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDiscussionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {
            val judul = binding.etTitle.text.toString()
            val isi = binding.etDescription.text.toString()
            val kategori = binding.etCategory.text.toString()
            val idPengguna = FirebaseAuth.getInstance().uid.orEmpty()

            if (judul.isBlank() || isi.isBlank() || kategori.isBlank()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val judulPart = RequestBody.create("text/plain".toMediaTypeOrNull(), judul)
            val isiPart = RequestBody.create("text/plain".toMediaTypeOrNull(), isi)
            val kategoriPart = RequestBody.create("text/plain".toMediaTypeOrNull(), kategori)
            val idPenggunaPart = RequestBody.create("text/plain".toMediaTypeOrNull(), idPengguna)

            val filePart = selectedImageUri?.let {
                val file = getFileFromUri(it)
                file?.let { f ->
                    MultipartBody.Part.createFormData(
                        "file",
                        f.name,
                        RequestBody.create("image/*".toMediaTypeOrNull(), f)
                    )
                }
            }

            viewModel.addDiscussion(judulPart, isiPart, kategoriPart, idPenggunaPart, filePart)
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

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val fileName = getFileName(uri)

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.let { input ->
                val tempFile = File(requireContext().cacheDir, fileName)
                val outputStream = tempFile.outputStream()

                // Read data from inputStream and write it to outputStream
                val buffer = ByteArray(1024)
                var length: Int
                while (input.read(buffer).also { length = it } != -1) {
                    outputStream.write(buffer, 0, length)
                }

                // Close streams
                input.close()
                outputStream.close()

                return tempFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getFileName(uri: Uri): String {
        var fileName = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    fileName = it.getString(columnIndex)
                }
            }
            it.close()
        }
        return fileName
    }
}
