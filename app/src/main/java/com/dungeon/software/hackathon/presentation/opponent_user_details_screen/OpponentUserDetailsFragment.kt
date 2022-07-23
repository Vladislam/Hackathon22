package com.dungeon.software.hackathon.presentation.opponent_user_details_screen

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentOpponentUserDetailsBinding
import com.dungeon.software.hackathon.domain.models.User
import kotlin.reflect.KClass

class OpponentUserDetailsFragment :
    BaseVMFragment<OpponentUserDetailsViewModel, FragmentOpponentUserDetailsBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_opponent_user_details
    override val viewModelClass: KClass<OpponentUserDetailsViewModel>
        get() = OpponentUserDetailsViewModel::class

    companion object {
        const val USER_BUNDLE_TAG = "user"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleArguments()
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleArguments() {
        val user = arguments?.getParcelable<User>(USER_BUNDLE_TAG)
        binding.user = user
    }
}