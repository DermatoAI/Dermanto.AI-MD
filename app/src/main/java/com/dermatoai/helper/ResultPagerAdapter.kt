package com.dermatoai.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * class as adapter pattern used for manage fragment in viewPager2.
 * @param activity
 * @param fragments
 */
class ResultPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(activity) {
    /**
     * provide size of fragment that used in viewPager
     */
    override fun getItemCount(): Int = fragments.size

    /**
     * method used for adding fragment to viewPager2.
     * @param position
     */
    override fun createFragment(position: Int): Fragment = fragments[position]
}