package com.dungeon.software.hackathon.presentation.user_details_screen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
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

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.data?.let { selectedImageUri ->
                    // Get the path from the Uri
                    binding.ivChatImage.setImageURI(null)
                    viewModel.deleteFile(binding.user?.imageUrl ?: "")
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

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogOut.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.splashFragment)
        }

        binding.fab.setOnClickListener { openImageChooser() }

        initObservers()
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            activityResult.launch(this)
        }
    }

    private fun initObservers() {

        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.collect { user ->
                binding.apply {
                    setUser(user)
                    Glide.with(requireContext())
                        .load(user.imageUrl)
                        .error(R.drawable.portrait_placeholder)
                        .transform(CircleCrop())
                        .placeholder(circularProgressDrawable)
                        .into(ivChatImage)
                }
            }
        }
    }
}