package com.pbd.githubusers.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pbd.githubusers.*
import com.pbd.githubusers.adapter.ListUserAdapter
import com.pbd.githubusers.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFollowersFragment : Fragment() {
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var rvFollowingFollowers: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFollowingFollowers = requireView().findViewById(R.id.foll_list)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        when(index) {
            1 -> username?.let { getFollowingFromApi(it) }
            else -> username?.let { getFollowersFromApi(it) }
        }
        showRecyclerList()
    }

    private fun getFollowingFromApi(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowingUser(username)
        client.enqueue(object : Callback<List<ItemSearch>> {
            override fun onResponse(
                call: Call<List<ItemSearch>>,
                response: Response<List<ItemSearch>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    Log.d("FOLLOWINGTAG",response.body().toString())
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showData(responseBody)
                    }
                } else {
                    Log.e("FRAGMENT_FOLLOWING", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemSearch>>, t: Throwable) {
                showLoading(false)
                Log.e("FRAGMENT_FOLLOWING", "onFailure: ${t.message}")
            }
        })
    }

    private fun getFollowersFromApi(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowersUser(username)
        client.enqueue(object : Callback<List<ItemSearch>> {
            override fun onResponse(
                call: Call<List<ItemSearch>>,
                response: Response<List<ItemSearch>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showData(responseBody)
                    }
                } else {
                    Log.e("FRAGMENT_FOLLOWING", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemSearch>>, t: Throwable) {
                showLoading(false)
                Log.e("FRAGMENT_FOLLOWING", "onFailure: ${t.message}")
            }
        })
    }

    private fun showRecyclerList() {
        listUserAdapter = ListUserAdapter(arrayListOf())
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvFollowingFollowers.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            rvFollowingFollowers.layoutManager = LinearLayoutManager(requireContext())
        }
        rvFollowingFollowers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemSearch) {
                val moveToDetailIntent = Intent(context, DetailUserActivity::class.java)
                moveToDetailIntent.putExtra(DetailUserActivity.EXTRA_USER, data.login)
                startActivity(moveToDetailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showData(responseData: List<ItemSearch>) {
        val noResult: TextView = requireView().findViewById(R.id.no_result)
        if (responseData.isEmpty()) {
            noResult.visibility = View.VISIBLE
            rvFollowingFollowers.visibility = View.GONE
        } else {
            noResult.visibility = View.GONE
            rvFollowingFollowers.visibility = View.VISIBLE
            listUserAdapter.setData(responseData)
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}