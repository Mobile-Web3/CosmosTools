package com.mobileweb3.cosmostools.android.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.common.BitMatrix

internal fun BitMatrix.toBitmap(): Bitmap {
    val height = this.height
    val width = this.width
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (this[x, y]) Color.BLACK else Color.WHITE)
        }
    }
    return bmp
}