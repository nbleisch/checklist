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
import com.fliteaid.checklist.model.CheckListLineItem

open class CheckListFragment : Fragment() {

    private var textMessage: TextView? = null
    private var checklistItemsRecyclerView: RecyclerView? = null
    private var checklistItemAdapter: CheckListItemAdapter? = null
    private var checklistItems: ArrayList<CheckListItem> = ArrayList()
    private var currentCheckItemPosition = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checklistItemsRecyclerView = view.findViewById(R.id.checklist_line_items) as RecyclerView?


        checklistItemAdapter = CheckListItemAdapter()
        checklistItemAdapter?.checkListener = { updatePosition() }

        checklistItemsRecyclerView!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = checklistItemAdapter
        }
    }

    fun updatePosition() {
        var correctedPos = ++currentCheckItemPosition
        for (i in 0 until checklistItems.size)
            checklistItems.get(i).let {
                if (it is CheckListLineItem) {
                    it.isActive = (i == correctedPos)
                } else {
                    correctedPos++
                }
            }
        checklistItemsRecyclerView!!.scrollToPosition(correctedPos)
        checklistItemAdapter!!.notifyDataSetChanged()
    }

    fun initWithCheckList(checklistTitle: String, vararg checklistItems: CheckListItem) {
        textMessage?.text = checklistTitle
        this.checklistItems = arrayListOf(*checklistItems)
        checklistItemAdapter?.checklistItems = this.checklistItems
        updatePosition()
    }

    fun backButtonPressed() {
        checklistItems.get(currentCheckItemPosition).let {
            if (it is CheckListLineItem) {
                it.checked = false
                it.isActive = false
            }
        }
        currentCheckItemPosition = Math.max(currentCheckItemPosition - 1, 0)
        if (currentCheckItemPosition == 0) return

        val checkListItem = checklistItems.get(currentCheckItemPosition)
        if (checkListItem is CheckListLineItem) {
            checkListItem.checked = false
            checkListItem.isActive = false
            checklistItemAdapter?.notifyDataSetChanged()
        } else {
            backButtonPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        checklistItemsRecyclerView!!.scrollToPosition(currentCheckItemPosition)
    }

    class CheckListItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var checkListener: () -> Unit = {}
        var checklistItems: ArrayList<CheckListItem> = ArrayList()

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder.itemView as CheckListItemView).update(checklistItems[position])
        }

        override fun getItemCount(): Int {
            return checklistItems.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(CheckListItemView(parent.context, doOnCheck = (checkListener)))
        }

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

}


