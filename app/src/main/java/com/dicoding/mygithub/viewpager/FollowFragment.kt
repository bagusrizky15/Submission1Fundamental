package com.dicoding.mygithub.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithub.R
import com.dicoding.mygithub.api.ItemsItem
import com.dicoding.mygithub.databinding.FragmentFollowBinding
import com.dicoding.mygithub.main_list.ListGithubAdapter

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel by viewModels<FollowViewModel>()


    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "username_user"
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position: Int? = null
        var username: String? = null

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (position == 1){
            followViewModel.userFollowings.observe(viewLifecycleOwner){ followings ->
                if (followings.isEmpty()){
                    showText(getString(R.string.following_not_found))
                    return@observe
                }
                setUserFollowings(followings)
            }

            followViewModel.findUserFollowings(username.toString())
        } else {
            followViewModel.userFollowers.observe(viewLifecycleOwner){ followers ->
                if (followers.isEmpty()){
                    showText(getString(R.string.follower_not_found))
                    return@observe
                }
                setUserFollowers(followers)
            }

            followViewModel.findUserFollowers(username.toString())
        }
    }

    private fun setUserFollowers(followers: List<ItemsItem>) {
        val adapter = ListGithubAdapter(followers)
        binding.rvFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : ListGithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                Toast.makeText(requireActivity(), "Kamu memilih " + data.login, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUserFollowings(followings: List<ItemsItem>) {
        val adapter = ListGithubAdapter(followings)
        binding.rvFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : ListGithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                Toast.makeText(requireActivity(), "Kamu memilih " + data.login, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean?) {
        binding.progressBarFollow.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun showText(msg: String) {
        binding.tvText.visibility = View.VISIBLE
        binding.tvText.text = msg
    }

}