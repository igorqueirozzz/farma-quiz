package dev.queiroz.farmaquiz.extensions

import android.content.Context
import dev.queiroz.farmaquiz.MainActivity

fun Context.getDrawableIdentifier(assetName: String): Int{
    return this.resources.getIdentifier(assetName, "drawable", MainActivity.PACKAGE_NAME)
}