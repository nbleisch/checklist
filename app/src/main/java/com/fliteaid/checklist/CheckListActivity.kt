package com.fliteaid.checklist

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v7.app.AppCompatActivity
import com.fliteaid.checklist.model.CheckListItem
import com.fliteaid.checklist.model.CheckListLineItem
import com.fliteaid.checklist.model.CheckListSection
import java.io.BufferedReader
import java.io.InputStreamReader

private val preflightCaption = "Checklist - Preflight".toUpperCase()
private val startCaption = "Checklist - Start".toUpperCase()
private val landingCaption = "Checklist - Landing".toUpperCase()

class CheckListActivity : AppCompatActivity() {

    private var fragmentPreflight: CheckListFragment? = null
    private var fragmentStart: CheckListFragment? = null
    private var fragmentLanding: CheckListFragment? = null
    private var fragmentActive: CheckListFragment? = null

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener { checklistStage ->
        when (checklistStage.itemId) {
            R.id.checklist_preflight -> {
                fragmentPreflight?.let {
                    show(it)
                }
                title = preflightCaption
                return@OnNavigationItemSelectedListener true
            }
            R.id.checklist_start -> {
                fragmentStart?.let {
                    show(it)
                }
                title = startCaption
                return@OnNavigationItemSelectedListener true
            }
            R.id.checklist_landing -> {
                fragmentLanding?.let {
                    show(it)
                }
                setTitle(landingCaption)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun show(fragmentToShow: CheckListFragment) {
        val ft = fragmentManager.beginTransaction()
        fragmentActive = fragmentToShow
        ft.hide(fragmentPreflight)
        ft.hide(fragmentStart)
        ft.hide(fragmentLanding)
        ft.show(fragmentToShow)
        ft.commit()
    }

    override fun onBackPressed() {
        fragmentActive?.backButtonPressed()
    }

    private fun initCheckLists() {
        fragmentPreflight?.initWithCheckList("Preflight", *loadCheckList(R.raw.preflight))
        fragmentStart?.initWithCheckList("Start", *loadCheckList(R.raw.start))
        fragmentLanding?.initWithCheckList("Landing", *loadCheckList(R.raw.landing))
    }

    fun loadCheckList(resourceId: Int): Array<CheckListItem> {
        val inputStream = resources.openRawResource(resourceId)
        val checklistItems = ArrayList<CheckListItem>()
        val checklistReader = BufferedReader(InputStreamReader(inputStream))
        checklistReader.readLines().forEach {
            if (!it.isEmpty()) {
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

        fragmentPreflight = fragmentManager.findFragmentById(R.id.fragment_checklist_preflight) as CheckListFragment?
        fragmentStart = fragmentManager.findFragmentById(R.id.fragment_checklist_start) as CheckListFragment?
        fragmentLanding = fragmentManager.findFragmentById(R.id.fragment_checklist_landing) as CheckListFragment?
        val navigation = findViewById(R.id.checklist_categories_navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        initCheckLists()
        title = preflightCaption
        show(fragmentPreflight!!)
    }

}
