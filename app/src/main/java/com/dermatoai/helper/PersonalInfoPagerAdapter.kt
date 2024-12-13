package com.dermatoai.helper

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dermatoai.ui.LikesFragment
import com.dermatoai.ui.PostsFragment

class PersonalInfoPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostsFragment()
            else -> LikesFragment()
        }
    }
}
