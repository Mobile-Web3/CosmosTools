package com.mobileweb3.cosmostools.android.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

fun <T : Drawable> RequestBuilder<T>.applyDefaults(): RequestBuilder<T> {
    return apply(
        RequestOptions()
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
    ).transition(DrawableTransitionOptions.withCrossFade())
}