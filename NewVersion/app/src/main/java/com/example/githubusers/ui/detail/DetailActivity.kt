package com.example.githubusers.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusers.GithubApp
import com.example.githubusers.R
import com.example.githubusers.data.Result
import com.example.githubusers.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back button presses
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to the previous activity
                finish()
            }
        })

        // Initialize Dagger
        (application as GithubApp).appComponent.inject(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]

        //  Get user data
        val username = intent.getStringExtra(EXTRA_USER)!!
        viewModel.getDetailUser(username).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        binding.progressBarDetail.visibility = View.GONE
                        binding.detailName.text = result.data.name
                        binding.detailUsername.text = result.data.username
                        binding.detailLocation.text = result.data.location
                        binding.detailCompany.text = result.data.company
                        binding.detailRepository.text =
                            getString(R.string.repositories_number, result.data.repository.toInt())
                        binding.detailFollowers.text =
                            getString(R.string.followers_number, result.data.follower.toInt())
                        binding.detailFollowing.text =
                            getString(R.string.following_number, result.data.following.toInt())
                        Glide.with(applicationContext)
                            .load(result.data.avatarURL)
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_placeholder)
                            .fitCenter()
                            .into(binding.profileImage)
                    }

                    is Result.Loading -> {
                        binding.progressBarDetail.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.progressBarDetail.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text = getString(R.string.something_wrong)
                    }
                }
            }
        }

        // Sections pager following / followers
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    // Handle the Up button click event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to the previous activity
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"


        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following_tab,
            R.string.followers_tab
        )
    }
}