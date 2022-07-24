package com.dungeon.software.hackathon.presentation.opponent_user_details_screen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentOpponentUserDetailsBinding
import com.dungeon.software.hackathon.domain.models.User
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class OpponentUserDetailsFragment :
    BaseVMFragment<OpponentUserDetailsViewModel, FragmentOpponentUserDetailsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_opponent_user_details
    override val viewModelClass: KClass<OpponentUserDetailsViewModel>
        get() = OpponentUserDetailsViewModel::class

    private val adapter = UserAdapter {
        findNavController().navigate(
            OpponentUserDetailsFragmentDirections.actionOpponentUserDetailsFragmentSelf(
                it
            )
        )
    }

    companion object {
        const val USER_BUNDLE_TAG = "user"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFriends.adapter = adapter

        handleArguments()
        setupListeners()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendsState.collect {
                    if (it.isEmpty()) {
                        binding.emptyState.visibility = View.VISIBLE
                        binding.rvFriends.visibility = View.GONE
                    } else {
                        binding.emptyState.visibility = View.GONE
                        binding.rvFriends.visibility = View.VISIBLE
                    }
                    adapter.submitList(it.toMutableList())
                }
            }
        }
    }

    private fun setupListeners() = with(binding) {
        ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleArguments() {
        val user = arguments?.getParcelable<User>(USER_BUNDLE_TAG)
        user?.let {
            viewModel.getFriends(user.friends)
            binding.user = user
        }
    }
}