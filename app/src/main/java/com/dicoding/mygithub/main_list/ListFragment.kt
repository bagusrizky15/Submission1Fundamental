package com.dicoding.mygithub.main_list

import android.app.SearchManager
import android.content.ClipData.Item
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithub.api.ItemsItem
import com.dicoding.mygithub.R
import com.dicoding.mygithub.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = getString(R.string.title_list)
            setDisplayHomeAsUpEnabled(false)
        }
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvListGithub.layoutManager = layoutManager


        listViewModel.listUsers.observe(viewLifecycleOwner){ users ->
            if (users.isEmpty()){
                showText(getString(R.string.user_not_found))
            }else {
                binding.rvListGithub.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.GONE
                setListData(users)
            }

        }

        listViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

    }

    private fun showText(message: String){
        binding.rvListGithub.visibility = View.GONE
        binding.tvNotFound.visibility = View.VISIBLE
        binding.tvNotFound.text = message
    }


    private fun showLoading(isLoading: Boolean?) {
        binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun setListData(users: List<ItemsItem>) {
        val adapter = ListGithubAdapter(users)
        binding.rvListGithub.adapter = adapter
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListGithub.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter.setOnItemClickCallback(object : ListGithubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val toDetailListFragment = ListFragmentDirections.actionListFragmentToDetailListFragment()
                toDetailListFragment.username = data.login
                view?.findNavController()?.navigate(toDetailListFragment)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(requireContext(), MainActivity::class.java)))
        searchView.queryHint  = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    listViewModel.search = newText
                    listViewModel.findUserList()
                    return true
                }
                return false
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(context, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.favorite -> {
                val intent = Intent(context, FavoritActivity::class.java)
                startActivity(intent)
                return  true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}