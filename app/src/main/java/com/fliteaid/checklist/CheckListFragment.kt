package com.fliteaid.checklist

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fliteaid.checklist.model.CheckListItem

open class CheckListFragment : Fragment() {

    private var textMessage: TextView? = null
    private var checklistItemsRecyclerView: RecyclerView? = null
    private var checklistItemAdapter: CheckListItemAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checklistItemsRecyclerView = view.findViewById(R.id.checklist_line_items) as RecyclerView?

        checklistItemAdapter = CheckListItemAdapter()
        checklistItemsRecyclerView!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            //layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = checklistItemAdapter
        }
    }

    fun initWithCheckList(checklistTitle: String, vararg checklistItems: CheckListItem) {
        textMessage?.text = checklistTitle
        checklistItemAdapter?.checklistItems = arrayListOf(*checklistItems)
    }
}

class CheckListItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var checklistItems: ArrayList<CheckListItem> = ArrayList()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as CheckListItemView).update(checklistItems[position])
    }

    override fun getItemCount(): Int {
        return checklistItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(CheckListItemView(parent.context))
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
