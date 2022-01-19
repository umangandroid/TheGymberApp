package com.umang.thegymberapp.utils

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.umang.thegymberapp.R


/**
 * To load image using glide
 *
 * @param url : gym image url
 */
fun ImageView.loadImage(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    val requestOptions = RequestOptions()
    requestOptions.placeholder(circularProgressDrawable)
    requestOptions.error(R.drawable.baseline_error_24)

    Glide
        .with(this.context)
        .setDefaultRequestOptions(requestOptions)
        .load(url)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        /*.placeholder(circularProgressDrawable)*/
        .into(this)


}