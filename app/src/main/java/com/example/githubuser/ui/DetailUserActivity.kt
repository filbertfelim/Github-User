package com.example.githubuser.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        val viewModelFactory = ViewModelFactory.getInstance(this)
        favoriteViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
        detailViewModel.findUser(intent.getStringExtra(USER_NAME).toString())
        lifecycleScope.launch {
            val isFavorite = withContext(Dispatchers.IO) {
                favoriteViewModel.getFavoriteUserByUsername(intent.getStringExtra(USER_NAME).toString())
            }
            if (isFavorite) {
                binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.baseline_favorite_24))
            } else {
                binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.baseline_favorite_border_24))
            }
        }
        detailViewModel.detailUser.observe(this) { detailUser ->
            binding.userName.visibility = View.VISIBLE
            binding.userUsername.visibility = View.VISIBLE
            binding.followers.visibility = View.VISIBLE
            binding.following.visibility = View.VISIBLE
            setUserData(detailUser)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fabAdd.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(user: DetailUserResponse) {
        binding.userName.text = user.name
        Glide.with(this)
            .load(user.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.userImage)
        binding.userUsername.text = user.login
        binding.toolbarTitle.text = user.name
        binding.followers.text = getString(R.string.user_followers, user.followers)
        binding.following.text = getString(R.string.user_following, user.following)
        val sectionsPagerAdapter = FollowPagerAdapter(this)
        sectionsPagerAdapter.username = user.login.toString()
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.fab_add) {
                lifecycleScope.launch {
                    val isFavorite = withContext(Dispatchers.IO) {
                        favoriteViewModel.getFavoriteUserByUsername(intent.getStringExtra(USER_NAME).toString())
                    }
                    if (!isFavorite) {
                        favoriteViewModel.insertNewFavorite(intent.getStringExtra(USER_NAME).toString(),intent.getStringExtra(AVATAR_URL).toString())
                        binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.baseline_favorite_24))
                        Toast.makeText(
                            this@DetailUserActivity,
                            "Berhasil menambah " + intent.getStringExtra(USER_NAME).toString() + " ke favorit" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        favoriteViewModel.deleteFromFavorite(intent.getStringExtra(USER_NAME).toString())
                        binding.fabAdd.setImageDrawable(ContextCompat.getDrawable(binding.fabAdd.context, R.drawable.baseline_favorite_border_24))
                        Toast.makeText(
                            this@DetailUserActivity,
                            "Berhasil menghapus " + intent.getStringExtra(USER_NAME).toString() + " dari favorit" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        const val USER_NAME = "user_name"
        const val AVATAR_URL = "avatar_url"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }
}