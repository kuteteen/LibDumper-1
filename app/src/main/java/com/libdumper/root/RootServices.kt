package com.libdumper.root

import android.content.Intent
import android.os.*
import android.util.Log
import com.libdumper.Utils.MSG_GETINFO
import com.libdumper.Utils.TAG
import com.libdumper.dumper.Dumper
import com.topjohnwu.superuser.ipc.RootService

class RootServices : RootService(), Handler.Callback {

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "RootServices: onBind")
        val h = Handler(Looper.getMainLooper(), this)
        val m = Messenger(h)
        return m.binder
    }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what != MSG_GETINFO) return false
        val reply = Message.obtain()
        val data = Bundle()
        val nativeDir = msg.data.getString("native")
        val fixMe = msg.data.getBoolean("fixMe")
        val pkg = msg.data.getString("pkg")
        val file = msg.data.getString("file_dump")
        if (nativeDir != null && pkg != null && file != null) {
            if(file.equals("isfolder"))
            {
                var allprocesslist=Dumper(nativeDir,pkg, file).getAllProcesses()
                sendBroadcast(Intent("com.libdumper.getallprocess").also {
                    it.putExtra("allprocesses",allprocesslist)
                })
            }
            else
            data.putString("result", Dumper(nativeDir,pkg, file).dumpFile(fixMe))
        } else {
            data.putString("result", "Invalid Data!!")
        }
        reply.data = data
        try {
            msg.replyTo.send(reply)
        } catch (e: RemoteException) {
            Log.e(TAG, "Remote error", e)
        }
        return false
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "RootServices: onUnbind, client process unbound")
        // Default returns false, which means NOT daemon mode
        return super.onUnbind(intent)
    }
}