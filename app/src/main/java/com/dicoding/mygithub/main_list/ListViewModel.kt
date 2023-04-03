package com.dicoding.mygithub.main_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithub.api.ApiConfig
import com.dicoding.mygithub.api.GithubResponse
import com.dicoding.mygithub.api.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel : ViewModel(){
    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers : LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        findUserList()
    }

    var search: String? = null

    fun findUserList() {
        _isLoading.value = true

        if (search != null){
            _isLoading.value = true
            USERNAME_GITHUB = search as String
        }

        val client = ApiConfig.getApiService().getListUsers(USERNAME_GITHUB)
        client.enqueue(object: Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _listUsers.value = response.body()!!.items
                    }else{
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        var USERNAME_GITHUB = "felda"
        private const val TAG = "ListViewModel"
    }

}