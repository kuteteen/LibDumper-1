package com.libdumper.dumper

import com.libdumper.Utils.longToHex
import com.libdumper.fixer.Fixer
import com.libdumper.process.ProcessDetail
import java.io.*
import java.lang.IllegalArgumentException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.ArrayList

/*
   An Modified Tools.kt from "https://github.com/BryanGIG/KMrite"
*/

class Dumper(private val nativeDir: String, var pkg: String, private val file: String="") {
    private val mem = Memory(pkg)
    private var allmaplist = ArrayList<ProcessDetail>()
    fun dumpFile(autoFix: Boolean): String {
        var log = ""
        try {
            getProcessID()
            log += "PID : ${mem.pid}\n"
            parseMap()
            mem.size = mem.eAddress - mem.sAddress
            log += "Start Address : ${mem.sAddress.longToHex()}\n"
            log += "End Address : ${mem.eAddress.longToHex()}\n"
            log += "Size Memory : ${mem.size}\n"
            if (mem.sAddress > 1L && mem.eAddress > 1L) {
                var path=File("/sdcard/Download/$pkg")
                if(!path.exists())path.mkdirs()
                val pathOut = File("${path.absolutePath}/${mem.sAddress.longToHex()}-$file")
                RandomAccessFile("/proc/${mem.pid}/mem", "r").use { mems ->
                    mems.channel.use { filechannel ->
                        log += "Dumping...\n"
                        val buff = ByteBuffer.allocate(mem.size.toInt())
                        filechannel.read(buff, mem.sAddress)
                        pathOut.outputStream().use { out ->
                            out.write(buff.array())
                            out.close()
                        }
                        filechannel.close()
                        if (autoFix) {
                            log += "Fixing...\n"
                            log += "${Fixer(nativeDir, pathOut, mem).fixDump()}\n"
                        }
                        log += "Done. Saved at ${pathOut.absolutePath}\n\n"
                    }
                }
            }
        } catch (e: Exception) {
            log += "$e\n"
        }
        return log
    }

    fun getProcessname(originname:String):String?{
        var firstindex=originname.indexOf("/")
        var lastindex=originname.lastIndexOf("/")
        if(firstindex>0) {
            var name = originname.substring(lastindex + 1, originname.length)
            return name
        }
        return null
    }
    fun getProcesspath(originname:String):String?{
        var firstindex=originname.indexOf("/")
        var lastindex=originname.lastIndexOf("/")
        if(firstindex>0) {
            var path = originname.substring(firstindex + 1, originname.length)
            return path
        }
        return null
    }

    fun getAllProcesses():ArrayList<ProcessDetail> {
        try{
            getProcessID()
            val files = File("/proc/${mem.pid}/maps")
            if (files.exists()) {
                var lines = files.readLines(Charset.defaultCharset())
                lines.forEach {
                    var processname=getProcessname(it)
                    if (processname!=null&&allmaplist.filter {
                            it.processname!!.contains(processname)
                        }.size==0) {
                        var process = ProcessDetail()
                        process.processname = processname
                        val startAddr = lines.find { line ->
                            line.contains(processname)
                        }
                        val endAddr = lines.findLast { line ->
                            line.contains(processname)
                        }
                        val regex = "\\p{XDigit}+-\\p{XDigit}+".toRegex()
                        if (startAddr == null || endAddr == null) {
                            throw FileNotFoundException("$file not found in ${files.path}")
                        } else {
                            startAddr.let { unused ->
                                regex.find(unused)?.value.let {
                                    if (it != null) {
                                        val result = it.split("-")
                                        process.sAddress = result[0].toLong(16)
                                    }
                                }
                            }
                            endAddr.let { unused ->
                                regex.find(unused)?.value.let {
                                    if (it != null) {
                                        val result = it.split("-")
                                        process.eAddress = result[1].toLong(16)
                                    }
                                }
                            }
                        }
                        allmaplist.add(process)
                    }
                }
            }
        }
        catch (e:Exception){
        }
        return allmaplist
    }

    private fun parseMap() {
        val files = File("/proc/${mem.pid}/maps")
        if (files.exists()) {
            val lines = files.readLines(Charset.defaultCharset())
            lines.forEach {
            }
            val startAddr = lines.find { it.contains(file) }
            val endAddr = lines.findLast { it.contains(file) }
            val regex = "\\p{XDigit}+-\\p{XDigit}+".toRegex()
            if (startAddr == null || endAddr == null) {
                throw FileNotFoundException("$file not found in ${files.path}")
            } else {
                startAddr.let { unused ->
                    regex.find(unused)?.value.let {
                        if (it != null) {
                            val result = it.split("-")
                            mem.sAddress = result[0].toLong(16)
                        }
                    }
                }
                endAddr.let { unused ->
                    regex.find(unused)?.value.let {
                        if (it != null) {
                            val result = it.split("-")
                            mem.eAddress = result[1].toLong(16)
                        }
                    }
                }
            }
        } else {
            throw FileNotFoundException("Failed To Open : ${files.path}")
        }
    }

    private fun getProcessID() {
        val process = Runtime.getRuntime().exec(arrayOf("pidof", mem.pkg))
        val reader = process.inputStream.bufferedReader()
        val buff = reader.readLine()
        reader.close()
        process.waitFor()
        process.destroy()
        if (buff != null && buff.isNotEmpty())
            mem.pid = buff.toInt()
        else
            throw IllegalArgumentException("Make sure your proccess package is running !\n")
    }
}

