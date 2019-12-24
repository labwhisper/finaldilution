package com.labwhisper.biotech.finaldilution

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.google.gson.reflect.TypeToken
import com.labwhisper.biotech.finaldilution.component.Component
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

    init {
        Log.d(TAG, "Creating an application context")
        instance = this
    }

    fun initEmptyCompoundList(store: DataGatewayOperations<Compound>) {
        Log.d(TAG, "No compounds found. Creating initial compound list")
        val compounds = loadDefaultCompounds(instance)
        compounds.forEach { it?.let { compound -> store.save(compound) } }
    }

    fun loadAllCompoundsSorted(): List<Compound> {
        return compoundGateway.loadAll().sorted()
    }

    fun safeSaveCompound(compound: Compound) {
        //if name exist -> sameNameException ( to be handled by function user )
        compoundGateway.save(compound)
    }

    fun updateCompound(compound: Compound) {
        return compoundGateway.update(compound)
    }

    //FIXME Add test cases
    fun renameCompound(compound: Compound, oldCompound: Compound) {
        compoundGateway.rename(compound, oldCompound.name)
        for (solution in solutionGateway.loadAll()) {
            val oldComponent = solution.getComponentWithCompound(oldCompound)
            oldComponent?.let {
                val newComponent = Component(
                    compound,
                    oldComponent.desiredConcentration,
                    oldComponent.availableConcentration
                ).apply { setSolutionVolume(solution.volume) }
                solution.removeComponent(oldComponent)
                solution.addComponent(newComponent)
                solutionGateway.update(solution)
            }
        }
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
