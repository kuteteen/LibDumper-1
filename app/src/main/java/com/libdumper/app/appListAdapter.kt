package com.libdumper.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.libdumper.R
import com.libdumper.glide.GlideApp

/**
 * @author
 * The adapter of processes from the selected app
 */
class appListAdapter(var context: Context, var applist:ArrayList<AppDetail>) :RecyclerView.Adapter<appListAdapter.ProcessHolder>(){
    private var onapplistener: onAppListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessHolder {

        return ProcessHolder(LayoutInflater.from(context).inflate(R.layout.row_app,null))
    }

    override fun onBindViewHolder(holder: ProcessHolder, position: Int) {
        applist[position].let {
            appdetail->
            holder.appname.text=appdetail.appname
            holder.packagename.text=appdetail.packagename
            GlideApp.with(context)
                .load(appdetail.applicationInfo?.loadIcon(context.getPackageManager()))
                .into(holder.appicon)
            holder.view.setOnClickListener {
                onapplistener?.onSelect(appdetail,position)
            }
        }

    }

    fun setAppListener(onAppListener: onAppListener){
        this.onapplistener=onAppListener
    }
    override fun getItemCount(): Int {
        return applist.size
    }
    class ProcessHolder(view:View):RecyclerView.ViewHolder(view){
        var view=view
        var appname=view.findViewById<TextView>(R.id.appname)
        var packagename=view.findViewById<TextView>(R.id.packagename)
        var appicon=view.findViewById<ImageView>(R.id.appicon)

    }
}