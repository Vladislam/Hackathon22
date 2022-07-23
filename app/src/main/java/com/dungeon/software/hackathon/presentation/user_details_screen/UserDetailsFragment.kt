package com.dungeon.software.hackathon.presentation.user_details_screen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentChatBinding
import com.dungeon.software.hackathon.databinding.FragmentUserDetailsBinding
import com.dungeon.software.hackathon.presentation.splash_screen.SplashFragmentDirections
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class UserDetailsFragment : BaseVMFragment<UserDetailsViewModel, FragmentUserDetailsBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_user_details

    override val viewModelClass: KClass<UserDetailsViewModel>
        get() = UserDetailsViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogOut.setOnClickListener {
            viewModel.logout()
            (parentFragment as NavHostFragment)
                .parentFragment
                ?.findNavController()
                ?.navigate(R.id.splashFragment)
        }

        initObservers()
    }

    private fun initObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.collect { user ->
                binding.apply {
                    etEmail.text = user.email
                    etName.setText(user.name)
                    Glide.with(requireContext())
                        .load(user.imageUrl)
                        .error(R.drawable.portrait_placeholder)
                        .circleCrop()
                        .into(ivChatImage)
                }
            }
        }
    }
}