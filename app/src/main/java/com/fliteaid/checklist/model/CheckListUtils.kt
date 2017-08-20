package com.fliteaid.checklist.model

/**
 * Created on 23.06.2017. Copyright by REWE Digital GmbH.
 */
enum class CheckListCategory {
    PREFLIGHT("preflight"), START("start"), LANDING("landing");

    private var category_id: String

    constructor(id: String) {
        category_id = id
    }


    override fun toString(): String {
        return category_id
    }
}

