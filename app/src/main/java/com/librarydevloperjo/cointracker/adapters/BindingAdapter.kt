package com.librarydevloperjo.cointracker.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.librarydevloperjo.cointracker.R

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    view.visibility = if (isGone==true) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isInvisible")
fun bindIsInvisible(view: View, isInvisible: Boolean?) {
    view.visibility = if (isInvisible==true) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}
@BindingAdapter("isStar")
fun bindIsStar(view: ImageView, isStar: Boolean?) {
    val drawable = when (isStar) {
        true -> R.drawable.ratingstar_filled
        else -> R.drawable.ratingstar_empty
    }
    view.setImageResource(drawable)
}

@BindingAdapter("isSortByDesc")
fun bindIsSortByDesc(view: ImageView, isSortByDesc: Boolean?) {
    val drawableRes = when (isSortByDesc) {
        true -> R.drawable.sortdescending
        false -> R.drawable.sortascending
        null -> R.drawable.sort
    }
    view.setImageResource(drawableRes)
}

// need to define this, see : https://stackoverflow.com/questions/35809290/set-drawable-resource-id-in-androidsrc-for-imageview-using-data-binding-in-andr
@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}