package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dermatoai.databinding.FragmentForumBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumFragment : Fragment() {

//    private lateinit var binding: FragmentForumBinding

    // Register for activity result to pick an image
//    private val selectImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        uri?.let {
//            binding.imageViewUploaded.setImageURI(uri)
//            binding.imageViewUploaded.visibility = View.VISIBLE
//        }
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentForumBinding.inflate(inflater, container, false)
//
//        // Set up user information
//        binding.userName.text = "uswtunhsanah_"
//        binding.userDate.text = "11/10/2024"
//
//        // Set click listener for uploading an image
//        binding.btnUploadImage.setOnClickListener {
//            selectImageResult.launch("image/*")  // Launch the image picker
//        }
//
////        // Button to send data to a detail page or another activity
////        binding.btnGoToDetail.setOnClickListener {
////            // Get text from EditText and Image URI
////            val description = binding.editTextDescription.text.toString()
////            val imageUri = (binding.imageViewUploaded.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
////
////            // Create an Intent to pass data to another activity (DiscussionDetailActivity)
////            val intent = Intent(requireActivity(), DiscussionDetailActivity::class.java)
////            intent.putExtra("DESCRIPTION", description)
////
////            // Optionally convert image URI to string and pass as an extra
////            imageUri?.let {
////                intent.putExtra("IMAGE_URI", imageUri.toString())  // Or pass the URI directly
////            }
//
////            // Start the detail activity
////            startActivity(intent)
////        }
////
////        return binding.root
//        return TODO("Provide the return value")
//    }
}
