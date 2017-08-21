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
                                                  defStyleAttr: Int = 0, var doOnCheck: () -> Unit)
    : RelativeLayout(context,
        attrs,
        defStyleAttr) {

    private var checkbox: CheckBox
    private lateinit var checkListItem: CheckListItem
    private var subjectTextView: TextView
    private var actionToDoTextView: TextView
    private var sectionTextView: TextView

    var checkListeners: Map<Boolean, View.OnClickListener> = hashMapOf(
            true to View.OnClickListener {
                if (checkListItem is CheckListLineItem) {
                    (checkListItem as CheckListLineItem).checked = true
                }
                doOnCheck()
            },
            false to View.OnClickListener {})

    init {
        inflate(context, R.layout.checklist_line_item, this)
        checkbox = (findViewById(R.id.checklist_line_item_checked) as CheckBox)
        subjectTextView = (findViewById(R.id.checklist_line_item_subject) as TextView)
        actionToDoTextView = (findViewById(R.id.checklist_line_item_actionToDo) as TextView)
        sectionTextView = (findViewById(R.id.checklist_section) as TextView)
    }

    fun update(checkListItem: CheckListItem) {
        this.checkListItem = checkListItem
        when (checkListItem) {
            is CheckListLineItem -> {
                findViewById(R.id.checklist_section_group)?.visibility = View.GONE
                findViewById(R.id.checklist_line_item_group)?.visibility = View.VISIBLE
                subjectTextView.text = checkListItem.subject
                actionToDoTextView.text = checkListItem.actionToDo
                checkbox.isChecked = checkListItem.checked
                checkListeners.get(checkListItem.isActive).let {
                    checkbox.setOnClickListener(it)
                    subjectTextView.setOnClickListener(it)
                    actionToDoTextView.setOnClickListener(it)
                }
                checkbox.isClickable = checkListItem.isActive
                if (checkListItem.isActive) {
                } else {
                }
                this.isActivated = checkListItem.isActive
            }
            is CheckListSection -> {
                findViewById(R.id.checklist_section_group)?.visibility = View.VISIBLE
                findViewById(R.id.checklist_line_item_group)?.visibility = View.GONE
                sectionTextView.text = checkListItem.sectionTitle
            }
        }
    }
}
