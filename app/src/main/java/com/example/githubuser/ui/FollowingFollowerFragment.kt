package com.example.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowingFollowerBinding

class FollowingFollowerFragment : Fragment() {

    private var position : Int = 0
    private var username : String = ""
    private var _binding: FragmentFollowingFollowerBinding? = null
        private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        detailViewModel.isFollowLoading.observe(requireActivity()) {
            showLoading(it)
        }
        if (position == 1){
            detailViewModel.findFollowers(username)
            detailViewModel.followers.observe(viewLifecycleOwner) {
                setFollowData(it)
            }
        } else {
            detailViewModel.findFollowing(username)
            detailViewModel.following.observe(viewLifecycleOwner) {
                setFollowData(it)
            }
        }

    }
    private fun setFollowData(listFollow: List<ItemsItem?>?) {
        val adapter = FollowAdapter()
        adapter.submitList(listFollow)
        binding.rvFollow.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.followingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "user_name"
    }
}