package com.libdumper.app

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Hao
 */
@Parcelize
data class AppDetail (
    var appname:String?=null,
    var packagename:String?=null,
    var applicationInfo: ApplicationInfo?=null
) : Parcelable