package com.example.githubuser.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import com.example.githubuser.databinding.FavoriteUserBinding

class FavoriteUserAdapter : ListAdapter<FavoriteUserEntity, FavoriteUserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FavoriteUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(review)
        }
    }
    class MyViewHolder(val binding: FavoriteUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteUserEntity){
            binding.favoriteUserUsername.text = user.username
            Glide.with(itemView)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.favoriteUserPhoto)
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUserEntity)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUserEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity): Boolean {
                return oldItem.username == newItem.username
            }
            override fun areContentsTheSame(oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity): Boolean {
                return oldItem.username == newItem.username
            }
        }
    }
}