package com.dungeon.software.hackathon.base.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dungeon.software.hackathon.base.view_holder.BindingHolder

abstract class BaseRecyclerViewAdapter<T : Any, Binding : ViewDataBinding> :
    ListAdapter<T, BindingHolder<Binding>>(DiffCallback<T>()) {

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<Binding> =
        BindingHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BindingHolder<Binding>, position: Int) {
        onBind(holder.binding, holder.adapterPosition)
    }

    override fun submitList(list: MutableList<T>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    protected abstract fun onBind(binding: Binding, position: Int)

    class DiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem
    }
}