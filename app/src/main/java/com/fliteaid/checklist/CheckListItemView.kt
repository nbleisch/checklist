package com.fliteaid.checklist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import com.fliteaid.checklist.model.CheckListItem
import com.fliteaid.checklist.model.CheckListLineItem
import com.fliteaid.checklist.model.CheckListSection

class CheckListItemView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0)
    : RelativeLayout(context,
        attrs,
        defStyleAttr) {

    private var checkbox: CheckBox

    init {
        isClickable = true
        inflate(context, R.layout.check_list_line_item, this)
        checkbox = (findViewById(R.id.checklist_line_item_checked) as CheckBox)
        setOnClickListener {
            checkbox.isChecked = !checkbox.isChecked
        }
        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            println("Hello Checked")
        }

        (findViewById(R.id.checklist_line_item_subject) as TextView)?.setOnClickListener {
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    fun update(checkListItem: CheckListItem) {
        when (checkListItem) {
            is CheckListLineItem -> {
                findViewById(R.id.checklist_section_group)?.visibility = View.GONE
                findViewById(R.id.checklist_line_item_group)?.visibility = View.VISIBLE
                (findViewById(R.id.checklist_line_item_subject) as TextView)?.text = checkListItem.subject
                (findViewById(R.id.checklist_line_item_actionToDo) as TextView)?.text = checkListItem.actionToDo
            }
            is CheckListSection -> {
                findViewById(R.id.checklist_section_group)?.visibility = View.VISIBLE
                findViewById(R.id.checklist_line_item_group)?.visibility = View.GONE
                (findViewById(R.id.checklist_section) as TextView)?.text = checkListItem.sectionTitle
            }
        }
    }
}
