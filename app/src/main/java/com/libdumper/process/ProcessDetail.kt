package com.libdumper.process

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Hao
 */
@Parcelize
data class ProcessDetail (
    var processpath:String?=null,
    var processname: String?=null,
    var sAddress: Long = 0L,
    var eAddress: Long = 0L,
    var size:Long = 0L,
    var isselect:Boolean=false
) : Parcelable