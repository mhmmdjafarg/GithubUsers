package com.example.githubusers.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username = ""
    override fun createFragment(position: Int): Fragment {
        val followingFragment = FollowingFragment()
        followingFragment.arguments = Bundle().apply {
            putInt(FollowingFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowingFragment.ARG_USERNAME, username)
        }
        return followingFragment
    }

    override fun getItemCount(): Int {
        return TAB_COUNT
    }

    companion object {
        const val TAB_COUNT = 2
    }


}