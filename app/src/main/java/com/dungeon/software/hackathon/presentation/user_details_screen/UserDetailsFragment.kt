package com.dungeon.software.hackathon.presentation.user_details_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentUserDetailsBinding
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

        binding.fab.setOnClickListener { openImageChooser() }

        initObservers()
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.collect { user ->
                binding.apply {
                    etEmail.text = user.email
                    etName.setText(user.name)
                    Glide.with(requireContext())
                        .load(user.imageUrl)
                        .error(R.drawable.portrait_placeholder)
                        .transform(CircleCrop())
                        .into(ivChatImage)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === SELECT_PICTURE) {
            // Get the url from data
            val selectedImageUri: Uri? = data?.data
            if (null != selectedImageUri) {
                // Get the path from the Uri
                binding.ivChatImage.setImageURI(null)
                Glide.with(requireContext())
                    .load(selectedImageUri)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.portrait_placeholder)
                    .error(R.drawable.portrait_placeholder)
                    .into(binding.ivChatImage)
                viewModel.updateImageProfile(selectedImageUri)
            }
        }
    }

    companion object {
        private const val SELECT_PICTURE = 666
    }
}