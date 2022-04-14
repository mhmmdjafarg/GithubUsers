package com.pbd.githubusers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pbd.githubusers.databinding.ActivityDetailUserBinding
import com.pbd.githubusers.retrofit.ApiConfig
import com.pbd.githubusers.room.Favorite
import com.pbd.githubusers.room.FavoriteDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val db by lazy { FavoriteDB(this) }
    private var avatarUrl : String = ""
    private var username : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Get user data
        username = intent.getStringExtra(EXTRA_USER)!!
        getDetailFromApi(username)

        //Initialized Tab Layout
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        // floating action
        setFavoritesIcon()
        binding.floatingActionButton.setOnClickListener{
            setFavorite()
        }
    }

    private fun setFavoritesIcon() = CoroutineScope(Dispatchers.IO).launch {
        val favoriteDb: Favorite? =
            db.favoriteDao().getFavoriteByUsername(username)
        val floatingButton : FloatingActionButton = binding.floatingActionButton
        if (favoriteDb == null){
            withContext(Dispatchers.Main){
                floatingButton.setImageResource(R.drawable.ic_unfavorite)
            }
        } else {
            withContext(Dispatchers.Main){
                floatingButton.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    private fun setFavorite() = CoroutineScope(Dispatchers.IO).launch {
        val favoriteDb: Favorite? =
            db.favoriteDao().getFavoriteByUsername(username)
        val floatingButton : FloatingActionButton = binding.floatingActionButton
        if (favoriteDb == null){
            db.favoriteDao().insert(Favorite(0, username, avatarUrl))
            withContext(Dispatchers.Main){
                floatingButton.setImageResource(R.drawable.ic_favorite)
            }
        } else {
            db.favoriteDao().delete(favoriteDb)
            withContext(Dispatchers.Main){
                floatingButton.setImageResource(R.drawable.ic_unfavorite)
            }
        }
    }

    private fun getDetailFromApi(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username = username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.e("DetailActivity", "OnSuccess: ${responseBody.id}")
                        showData(responseBody)
                    }
                } else {
                    Log.e("DetailActivity", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                showLoading(false)
                Log.e("DetailActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarContent.visibility = View.VISIBLE
        } else {
            binding.progressBarContent.visibility = View.GONE
        }
    }

    private fun showData(responseData: ResponseDetailUser) {
        binding.apply {
            Glide.with(applicationContext)
                .load(responseData.avatarUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .fitCenter()
                .into(binding.profileImage)
            detailUsername.text = responseData.login ?: "Unknown"
            detailName.text = responseData.name ?: "Unknown"
            detailFollowing.text = getString(R.string.following, responseData.following)
            detailFollowers.text = getString(R.string.followers, responseData.followers)
            detailCompany.text = responseData.company ?: "Unknown"
            detailLocation.text = responseData.location ?: "Unknown"
            detailRepository.text = getString(R.string.dummy_repos, responseData.publicRepos)
        }
        avatarUrl = responseData.avatarUrl ?: "unknown"
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