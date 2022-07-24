package com.dungeon.software.hackathon.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dungeon.software.hackathon.base.dialog.BaseProgressDialog
import com.dungeon.software.hackathon.presentation.MainActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseBindingFragment<Binding : ViewDataBinding> : Fragment() {

    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    var baseProgressDialog: BaseProgressDialog? = null

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected fun getRequireMainActivity() = requireActivity() as MainActivity

    protected fun getMainActivity() = activity as? MainActivity?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    protected fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(requireView(), message, duration).show()
    }

    protected fun showMessage(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(requireView(), message, duration).show()
    }

    protected fun showLoading() {
        baseProgressDialog = BaseProgressDialog.show(requireContext())
    }

    protected fun hideLoading() {
        baseProgressDialog?.let {
            it.dismiss()
            baseProgressDialog = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}