package com.pbd.githubusers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pbd.githubusers.fragments.FollowingFollowersFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val followingFragment = FollowingFollowersFragment()
        followingFragment.arguments = Bundle().apply {
            putInt(FollowingFollowersFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowingFollowersFragment.ARG_USERNAME, username)
        }
        return followingFragment
    }


    override fun getItemCount(): Int {
        return 2
    }

}