package com.labessence.biotech.finaldilution

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.init.loadDefaultCompounds
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labessence.biotech.finaldilution.peripherals.datastores.SharedPreferencesStore
import com.labessence.biotech.finaldilution.solution.Solution

class ApplicationContext : Application() {

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

    init {
        Log.d(TAG, "Creating an application context")
        instance = this
    }

    fun initEmptyCompoundList(store: DataGatewayOperations<Compound>) {
        Log.d(TAG, "No compounds found. Creating initial compound list")
        val compounds: List<Compound> = loadDefaultCompounds(instance)
        compounds.forEach { store.save(it) }
    }

    fun removeCompoundFromEverywhere(compound: Compound) {
        for (solution in solutionGateway.loadAll()) {
            val component = solution.getComponentWithCompound(compound)
            if (component != null) {
                solution.removeComponent(component)
                solutionGateway.update(solution)
            }
        }
        compoundGateway.remove(compound)
    }

    fun saveCurrentWorkOnSolution(solution: Solution) {
        solutionGateway.update(solution)
    }

    fun renameSolution(solution: Solution, oldName: String) {
        solutionGateway.rename(solution, oldName)
    }

    fun reloadSolution(solution: Solution): Solution? {
        return solutionGateway.load(solution.name)
    }

    companion object {
        private val TAG = "Application Context"
        val FINAL_DILUTION_PREFERENCES = "FINAL_DILUTION_PREFERENCES"
        private lateinit var instance: ApplicationContext

        val context: Context
            get() = instance
    }
}
