package com.libdumper.fixer


import com.libdumper.dumper.Memory
import com.libdumper.Utils.getFileExtension
import com.libdumper.Utils.getOriginalName
import com.libdumper.Utils.longToHex
import java.io.File

class Fixer(private val nativeDir:String, private val files: File, val memory: Memory) {

    fun fixDump(): String {
        val param = arrayOf(
            "${nativeDir}${File.separator}libfixer.so",
            files.absolutePath,
            memory.sAddress.longToHex(),
            "/sdcard/Download/${files.getOriginalName()}_fix${files.getFileExtension()}"
        )
        val proc = Runtime.getRuntime().exec(param)
        proc.waitFor()
        val res = proc.inputStream.bufferedReader().readLines()
        proc.destroy()
        return res.joinToString("\n")
    }
}