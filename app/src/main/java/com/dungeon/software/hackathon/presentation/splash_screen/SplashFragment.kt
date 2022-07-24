package com.dungeon.software.hackathon.presentation.splash_screen

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dangeon.software.notes.util.pop_up.CustomError
import com.dangeon.software.notes.util.pop_up.PopUpManager
import com.dungeon.software.hackathon.R
import com.dungeon.software.hackathon.base.fragment.BaseVMFragment
import com.dungeon.software.hackathon.databinding.FragmentSplashBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.reflect.KClass

class SplashFragment : BaseVMFragment<SplashViewModel, FragmentSplashBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_splash

    override val viewModelClass: KClass<SplashViewModel>
        get() = SplashViewModel::class

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.authWithIntent(it.data ?: return@registerForActivityResult)
            } else {
                showError()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        popUpManager.attach(binding.rootContainer, lifecycle)
        viewModel.isLoggedIn(activityResult)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.checkedUser.collect { isLoggedIn ->
                if (isLoggedIn) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToChatsListNavGraph())
                } else {
                    showError()
                }
            }
        }
    }

    private fun showError(error: CustomError = CustomError.SomethingWentWrong) {
        popUpManager.showError(error)
        viewModel.isLoggedIn(activityResult)
    }

}