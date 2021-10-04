package com.libdumper

import java.io.File

object Utils {
    const val TAG = "LibDumper : "
    const val MSG_GETINFO = 1

    fun Long.longToHex(): String {
        return Integer.toHexString(this.toInt())
    }

    fun File.getOriginalName(): String {
        var fileName = ""
        try {
            if (exists()) {
                fileName = name.replaceFirst("[.][^.]+$".toRegex(), "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fileName = ""
        }
        return fileName
    }

    fun File.getFileExtension(): String {
        val lastIndexOf = name.lastIndexOf(".")
        return if (lastIndexOf == -1) {
            "" // empty extension
        } else name.substring(lastIndexOf)
    }
}