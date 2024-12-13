package com.dermatoai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dermatoai.R
import com.dermatoai.helper.PersonalInfoPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class PersonalInformationFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PersonalInfoPagerAdapter
    private lateinit var cvPhoto: CircleImageView
    private lateinit var tvUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        val user = FirebaseAuth.getInstance().currentUser
        cvPhoto = view.findViewById(R.id.cvPhoto)
        tvUsername = view.findViewById(R.id.tvUsername)
        Glide.with(requireContext())
            .load(user?.photoUrl)
            .into(cvPhoto)
        tvUsername.text = user?.displayName

        setupViewPager(viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("Posts"))
        tabLayout.addTab(tabLayout.newTab().setText("Likes"))

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Posts"
                else -> "Likes"
            }
        }.attach()
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        adapter = PersonalInfoPagerAdapter(this)
        viewPager.adapter = adapter
    }
}
