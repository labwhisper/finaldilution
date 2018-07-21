package com.labessence.biotech.finaldilution.peripherals

import com.labessence.biotech.finaldilution.genericitem.Item

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 9/10/2017.
 */

interface DataGatewayOperations<T : Item> {
    fun save(item: T)

    fun update(item: T)

    fun remove(item: T)

    fun load(name: String): T?

    fun loadAll(): List<T>

    fun size(): Int
}
