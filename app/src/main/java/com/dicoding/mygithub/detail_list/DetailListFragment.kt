package com.dicoding.mygithub.detail_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dicoding.mygithub.R
import com.dicoding.mygithub.api.DetailUserResponse
import com.dicoding.mygithub.databinding.FragmentDetailListBinding
import com.dicoding.mygithub.main_list.MainActivity
import com.dicoding.mygithub.viewpager.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailListFragment : Fragment() {
    private var _binding: FragmentDetailListBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailListBinding.inflate(inflater,container,false)
        val view = binding.root
        setHasOptionsMenu(true)
        return view
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = getString(R.string.title_detail)
            setDisplayHomeAsUpEnabled(true)
        }

        val username = DetailListFragmentArgs.fromBundle(arguments as Bundle).username
        detailViewModel.findDetaiListUser(username)

        detailViewModel.detailListUsers.observe(viewLifecycleOwner){ user ->
            setDetailUser(user)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            showLoading(isLoading)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun showLoading(loading: Boolean?) {
        binding.progressBar.visibility = if (loading == true) View.VISIBLE else View.GONE
    }

    private fun setDetailUser(user: DetailUserResponse?) {
        Glide.with(requireActivity())
            .load(user?.avatarUrl)
            .into(binding.imageDetail)
        binding.fullname.text = user?.name
        binding.usernameDetail.text = user?.login
        binding.followings.text = user?.following.toString()
        binding.followers.text = user?.followers.toString()
        binding.repository.text = user?.publicRepos.toString()
    }


}