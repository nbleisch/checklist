package com.fliteaid.checklist.model

/**
 * Created on 23.06.2017. Copyright by REWE Digital GmbH.
 */
sealed class CheckListItem

data class CheckListLineItem(
        var checked: Boolean,
        var subject: String,
        var actionToDo: String
) : CheckListItem()

data class CheckListSection(
        var sectionTitle: String
) : CheckListItem()
