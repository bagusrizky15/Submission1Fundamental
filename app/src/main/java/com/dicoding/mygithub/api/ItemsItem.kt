package com.dicoding.mygithub.api

import com.google.gson.annotations.SerializedName

data class ItemsItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

)