package com.pbd.githubusers

import com.google.gson.annotations.SerializedName

data class ResponseFollowingFollowers(

	@field:SerializedName("ResponseFollowingFollowers")
	val responseFollowingFollowers: List<ItemSearch>? = null
)