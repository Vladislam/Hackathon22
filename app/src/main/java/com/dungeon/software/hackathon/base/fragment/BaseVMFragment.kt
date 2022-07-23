package com.dungeon.software.hackathon.base.fragment

import androidx.databinding.ViewDataBinding
import com.dungeon.software.hackathon.base.view_model.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseVMFragment<VM: BaseViewModel, Binding: ViewDataBinding> : BaseBindingFragment<Binding>() {

    protected val viewModel by lazy { initViewModel() }

    protected abstract val viewModelClass: KClass<VM>

    private fun initViewModel() : VM {
         return getViewModel(clazz = viewModelClass)
    }

}