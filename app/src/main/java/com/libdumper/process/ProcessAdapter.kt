package com.libdumper.process

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.libdumper.R

/**
 * @author
 * The adapter of processes from the selected app
 */
class ProcessAdapter(var context: Context, var processlist: ArrayList<ProcessDetail>) :
    RecyclerView.Adapter<ProcessAdapter.ProcessHolder>() {
    private var onProcessListener: onProcessListener? = null
    private var issingle = true
    private var isallselected = false
    private var onSelectedProcessListener: onSelectedProcessListener? = null
    private var selectedprocesslist = java.util.ArrayList<ProcessDetail>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessHolder {

        return ProcessHolder(LayoutInflater.from(context).inflate(R.layout.row_process, null))
    }

    override fun onBindViewHolder(holder: ProcessHolder, position: Int) {
        holder.setIsRecyclable(false)
        var processdetail = processlist[position]
        holder.processname.text = processdetail.processname
        if (issingle) {
            holder.view.setOnClickListener {
                onProcessListener?.onSelect(processdetail)
            }
            holder.mutiplechoose.visibility = View.GONE
        } else {
            holder.view.setOnClickListener(null)
            holder.mutiplechoose.visibility = View.VISIBLE
            holder.mutiplechoose.setOnCheckedChangeListener(null)
            if (processdetail.isselect)
                holder.mutiplechoose.isChecked = true
            else holder.mutiplechoose.isChecked = false
            holder.mutiplechoose.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (!selectedprocesslist.contains(processdetail)) selectedprocesslist.add(
                        processdetail
                    )
                } else {
                    if (selectedprocesslist.contains(processdetail)) selectedprocesslist.remove(
                        processdetail
                    )
                }
                processlist[position].isselect = isChecked
                onSelectedProcessListener?.onSelect(selectedprocesslist)
            }
        }
    }

    override fun getItemCount(): Int {
        return processlist.size
    }

    class ProcessHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view = view
        var processname = view.findViewById<TextView>(R.id.processname)
        var mutiplechoose = view.findViewById<CheckBox>(R.id.mutiplechoose)
    }

    fun setProcessListener(onProcessListener: onProcessListener) {
        this.onProcessListener = onProcessListener
    }

    fun setSingleMutipleListener(issingle: Boolean) {
        this.issingle = issingle
        notifyDataSetChanged()
    }

    fun setSelectedProcessListener(onSelectedProcessListener: onSelectedProcessListener) {
        this.onSelectedProcessListener = onSelectedProcessListener
    }

    fun setAllSoSelect(isallsoselect:Boolean){
        processlist.forEach {
            if(it.processname!!.contains(".so")) {
                it.isselect = isallsoselect
                if (isallsoselect)
                    if (!selectedprocesslist.contains(it)) selectedprocesslist.add(it)
                    else
                        if (selectedprocesslist.contains(it)) selectedprocesslist.remove(it)
            }
        }
        onSelectedProcessListener?.onSelect(selectedprocesslist)
        notifyDataSetChanged()
    }
}

interface onSelectedProcessListener {
    fun onSelect(selectedprocesslist: java.util.ArrayList<ProcessDetail>)
}