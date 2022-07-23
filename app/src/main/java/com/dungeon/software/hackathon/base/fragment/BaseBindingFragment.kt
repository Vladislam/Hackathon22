package com.dungeon.software.hackathon.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dungeon.software.hackathon.presentation.MainActivity

abstract class BaseBindingFragment<Binding : ViewDataBinding> : Fragment() {

    private var _binding: Binding? = null
    protected val binding get() = _binding!!

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

    protected fun showMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    protected fun showMessage(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}