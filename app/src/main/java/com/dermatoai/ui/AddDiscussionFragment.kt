package com.dermatoai.ui

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dermatoai.databinding.FragmentAddDiscussionBinding
import com.dermatoai.model.AddDiscussionViewModel
import com.dermatoai.oauth.GoogleAuthenticationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class AddDiscussionFragment : Fragment() {

    @Inject
    lateinit var oauthPreferences: GoogleAuthenticationRepository

    private lateinit var binding: FragmentAddDiscussionBinding
    private val viewModel: AddDiscussionViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    private lateinit var userid: String

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                binding.ivSelectedImage.visibility = VISIBLE
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
        viewLifecycleOwner.lifecycleScope.launch {
            oauthPreferences.getUserId().collect {
                if (it != null) {
                    userid = it.substringBefore("@")
                }
            }
        }

        binding.btnSelectImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            val judul = binding.etTitle.text.toString()
            val isi = binding.etDescription.text.toString()
            val kategori = binding.etCategory.text.toString()
            val idPengguna = if (::userid.isInitialized) {
                userid
            } else {
                Toast.makeText(requireContext(), "User ID not initialized", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (judul.isBlank() || isi.isBlank() || kategori.isBlank()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val judulPart = judul.toRequestBody("text/plain".toMediaTypeOrNull())
            val isiPart = isi.toRequestBody("text/plain".toMediaTypeOrNull())
            val kategoriPart = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
            val idPenggunaPart = idPengguna.toRequestBody("text/plain".toMediaTypeOrNull())

            val filePart = selectedImageUri?.let {
                val file = getFileFromUri(it)
                file?.let { f ->
                    MultipartBody.Part.createFormData(
                        "file",
                        f.name,
                        f.asRequestBody("image/*".toMediaTypeOrNull())
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
