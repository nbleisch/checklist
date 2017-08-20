package com.fliteaid.checklist

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import com.fliteaid.checklist.model.CheckListItem
import com.fliteaid.checklist.model.CheckListLineItem
import com.fliteaid.checklist.model.CheckListSection
import java.io.BufferedReader
import java.io.InputStreamReader

class CheckListActivity : AppCompatActivity() {

    private var fragment_preflight: CheckListFragment? = null
    private var fragment_start: CheckListFragment? = null
    private var fragment_landing: CheckListFragment? = null

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener { checklistStage ->
        when (checklistStage.itemId) {
            R.id.checklist_preflight -> {
                fragment_preflight?.let {
                    show(it)
                }
                title = "Checklist - Preflight"
                return@OnNavigationItemSelectedListener true
            }
            R.id.checklist_start -> {
                fragment_start?.let {
                    show(it)
                }
                title = "Checklist - Start"
                return@OnNavigationItemSelectedListener true
            }
            R.id.checklist_landing -> {
                fragment_landing?.let {
                    show(it)
                }
                setTitle("Checklist - Landing")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun show(fragmentToShow: Fragment) {
        val ft = fragmentManager.beginTransaction()
        ft.hide(fragment_preflight)
        ft.hide(fragment_start)
        ft.hide(fragment_landing)
        ft.show(fragmentToShow)
        ft.commit()
    }

    private fun initCheckLists() {
        fragment_preflight?.initWithCheckList("Preflight", *loadCheckList(R.raw.preflight))
        fragment_start?.initWithCheckList("Start", *loadCheckList(R.raw.start))
        fragment_landing?.initWithCheckList("Landing", *loadCheckList(R.raw.landing))
    }

    fun loadCheckList(resourceId: Int): Array<CheckListItem> {
        val inputStream = resources.openRawResource(resourceId)
        val checklistItems = ArrayList<CheckListItem>()
        val checklistReader = BufferedReader(InputStreamReader(inputStream))
        checklistReader.readLines().forEach {
            if (! it.isEmpty()) {
                val parts = it.split(",")
                if (parts.size > 1) {
                    checklistItems.add(CheckListLineItem(false, parts[0], parts[1]))
                } else {
                    checklistItems.add(CheckListSection(parts[0]))
                }
            }
        }
        return checklistItems.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)

        fragment_preflight = fragmentManager.findFragmentById(R.id.fragment_checklist_preflight) as CheckListFragment?
        fragment_start = fragmentManager.findFragmentById(R.id.fragment_checklist_start) as CheckListFragment?
        fragment_landing = fragmentManager.findFragmentById(R.id.fragment_checklist_landing) as CheckListFragment?
        val navigation = findViewById(R.id.checklist_categories_navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        initCheckLists()
        show(fragment_preflight!!)
    }

}
