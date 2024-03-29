package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.settings.SettingPreferences
import com.example.githubuser.settings.SettingViewModel
import com.example.githubuser.settings.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.noUser.visibility =  View.GONE
        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubUser.layoutManager = layoutManager
        mainViewModel.githubSearch.observe(this) { data ->
            binding.noUser.visibility = if (data.totalCount == null) View.VISIBLE else View.GONE
        }
        mainViewModel.listUsers.observe(this) { listUsers ->
            setUsersData(listUsers)
            binding.noUser.visibility = if (listUsers.isEmpty()) View.VISIBLE else View.GONE
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.isFailed.observe(this) {
            binding.noUser.visibility = if (it) View.VISIBLE else View.GONE
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_favorite -> {
                        val moveToFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(moveToFavorite)
                        true
                    }
                    R.id.menu_setting -> {
                        val moveToSetting = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(moveToSetting)
                        true
                    }

                    else -> {true}
                }
            }
            searchView
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    val adapter = GithubUserAdapter()
                    adapter.submitList(null)
                    binding.rvGithubUser.adapter = adapter
                    binding.noUser.visibility =  View.GONE
                    mainViewModel.setSearchQuery(searchView.text.toString())
                    searchBar.hint = searchView.text
                    searchView.hide()
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchView.text.toString()
                        performSearch(query)
                    }
                    false
                }
        }

        val modePreferences = SettingPreferences.getInstance(dataStore)
        val modeViewModel = ViewModelProvider(
            this,
            SettingViewModel.ViewModelFactory(modePreferences)
        )[SettingViewModel::class.java]

        modeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                for (i in 0 until binding.searchBar.menu.size()) {
                    val menuItem: MenuItem = binding.searchBar.menu.getItem(i)
                    if (i == 0)
                    {
                        menuItem.setIcon(R.drawable.baseline_favorite_light_24)
                    }
                    else {
                        menuItem.setIcon(R.drawable.baseline_settings_light_24)
                    }
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                for (i in 0 until binding.searchBar.menu.size()) {
                    val menuItem: MenuItem = binding.searchBar.menu.getItem(i)
                    if (i == 0)
                    {
                        menuItem.setIcon(R.drawable.baseline_favorite_24)
                    }
                    else {
                        menuItem.setIcon(R.drawable.baseline_settings_24)
                    }
                }
            }
        }
    }

    private fun performSearch(query: String) {
        mainViewModel.findUsers(query)
    }

    private fun setUsersData(listUsers: List<ItemsItem?>?) {
        val adapter = GithubUserAdapter()
        adapter.submitList(listUsers)
        binding.rvGithubUser.adapter = adapter
        adapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val moveToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveToDetail.putExtra(DetailUserActivity.USER_NAME, data.login.toString())
                moveToDetail.putExtra(DetailUserActivity.AVATAR_URL, data.avatarUrl.toString())
                startActivity(moveToDetail)
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}