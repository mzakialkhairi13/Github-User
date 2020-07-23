package com.mzakialkhairi.githubsearch.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mzakialkhairi.githubsearch.view.fragment.FollowerFragment
import com.mzakialkhairi.githubsearch.view.fragment.FollowingFragment

class SectionPagerAdapter(fm: FragmentManager, uname : String): FragmentPagerAdapter(fm){

    var username = uname

    private val pages = listOf(
        FollowerFragment(username),
        FollowingFragment(username)
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Follower"
            else -> "Following"
        }
    }
}