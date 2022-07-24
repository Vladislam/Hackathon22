package com.dungeon.software.hackathon.base.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseVMFragment<VM : BaseViewModel, Binding : ViewDataBinding> :
    BaseBindingFragment<Binding>() {

    protected val viewModel by lazy { initViewModel() }

    protected abstract val viewModelClass: KClass<VM>

    private fun initViewModel(): VM {
        return getViewModel(clazz = viewModelClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.error.collect {
                popUpManager.showError(it)
            }
        }
    }
}