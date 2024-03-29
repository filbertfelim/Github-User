package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.data.local.entity.FavoriteUserEntity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        binding.noFavorite.visibility =  View.GONE
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val viewModelFactory = ViewModelFactory.getInstance(this)
        val favoriteViewModel : FavoriteViewModel by viewModels {
            viewModelFactory
        }
        val adapter = FavoriteUserAdapter()
        binding.rvFavorite.adapter = adapter
        favoriteViewModel.getFavoriteUsers().observe(this) { data ->
            setUserFavoriteData(data,adapter)
        }
    }
    private fun setUserFavoriteData(listUsers: List<FavoriteUserEntity?>?, adapter : FavoriteUserAdapter) {
        if (listUsers != null) {
            val items = arrayListOf<FavoriteUserEntity>()
            listUsers.map {
                val item = it?.let { it1 -> FavoriteUserEntity(username = it1.username, avatarUrl = it.avatarUrl) }
                if (item != null) {
                    items.add(item)
                }
            }
            adapter.submitList(items)
            adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: FavoriteUserEntity) {
                    val moveToDetail = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                    moveToDetail.putExtra(DetailUserActivity.USER_NAME,
                        data.username
                    )
                    startActivity(moveToDetail)
                }
            })
        } else {
            binding.noFavorite.visibility =  View.VISIBLE
        }
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
}