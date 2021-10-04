package com.libdumper.app

import android.app.DialogFragment
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.libdumper.R
import java.util.*


/**
 * @author Hao
 * A dialog fragment for display the processes of the selected app
 */
class ApplistFragment: DialogFragment() {
    private var appList:ArrayList<AppDetail> ? = null
    private var onAppListener: onAppListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appList = it.getParcelableArrayList("applist")
        }
    }

    override fun onResume() {
        val params: ViewGroup.LayoutParams = getDialog().window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        getDialog().window!!.attributes = params as WindowManager.LayoutParams

        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater?.inflate(R.layout.fragment_applist,container)
        appList?.apply {
            var processlistview= view?.findViewById<RecyclerView>(R.id.processlistview)
            var llm = LinearLayoutManager(activity)
            llm!!.orientation = RecyclerView.VERTICAL
            processlistview?.layoutManager = llm
            var processAdapter= appListAdapter(activity,this).also {
                it.setAppListener(object: onAppListener {
                    override fun onSelect(appDetail: AppDetail, position: Int) {
                        onAppListener?.onSelect(appDetail,position)
                        this@ApplistFragment.dismiss()
                    }
                })
            }
            processlistview?.adapter = processAdapter
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    fun setProcessListener(onAppListener: onAppListener){
        this.onAppListener=onAppListener
    }

    companion object {
        @JvmStatic
        fun newInstance(applist:ArrayList<AppDetail>) =
            ApplistFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("applist", applist)
                }
            }
    }
}
interface onAppListener{
    fun onSelect(appDetail: AppDetail, position:Int)
}

