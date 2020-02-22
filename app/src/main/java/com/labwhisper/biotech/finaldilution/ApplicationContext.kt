package com.labwhisper.biotech.finaldilution

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.google.gson.reflect.TypeToken
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.init.loadDefaultCompounds
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labwhisper.biotech.finaldilution.peripherals.datastores.SharedPreferencesStore
import com.labwhisper.biotech.finaldilution.solution.Solution

class ApplicationContext : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    val solutionGateway: DataGatewayOperations<Solution> by lazy {
        SharedPreferencesStore(
            getSharedPreferences("solutions", Context.MODE_PRIVATE),
            object : TypeToken<List<Solution>>() {})
    }

    val compoundGateway: DataGatewayOperations<Compound> by lazy {
        val store = SharedPreferencesStore(
            getSharedPreferences("compounds", Context.MODE_PRIVATE),
            object : TypeToken<List<Compound>>() {})
        if (store.size() == 0) {
            initEmptyCompoundList(store)
        }
        store
    }

    fun initEmptyCompoundList(store: DataGatewayOperations<Compound>) {
        Log.d(TAG, "No compounds found. Creating initial compound list")
        val compounds = loadDefaultCompounds(this)
        compounds.forEach { it?.let { compound -> store.save(compound) } }
    }

    companion object {
        private val TAG = "Application Context"
    }
}
