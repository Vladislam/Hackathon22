package com.dungeon.software.hackathon.util.binding_adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.dungeon.software.hackathon.R

@BindingAdapter("app:loadImage")
fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.ic_people_48)
        .placeholder(R.drawable.ic_people_48)
        .into(this)
}