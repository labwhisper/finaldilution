package com.labessence.biotech.finaldilution

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labessence.biotech.finaldilution.peripherals.datastores.SharedPreferencesStore
import com.labessence.biotech.finaldilution.solution.Solution

class ApplicationContext : Application() {
    //TODO Remove currentSolution from here and pass it with Activities
    var currentSolution: Solution? = null
        get() = field ?: loadSolution()
        set(value) {
            setCurrentSolutionOrCreate(value)
            field = value
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
        store.save(Compound("KCl", 74.55))
        store.save(Compound("NaCl", 58.44))
        store.save(Compound("EDTA", 372.24))
        store.save(Compound("MgCl2", 95.21))
        store.save(Compound("TRIS", 121.14))
        store.save(Compound("SDS", 288.372))
    }

    override fun onTerminate() {
        //TODO Clear DataGateway
        super.onTerminate()
    }

    fun loadSolution(): Solution? {
        //TODO Current Solution bedzie przechowywane w podrecznych danych androida.
        //TODO Przeniesc mechanizm zapisywania stanu do nowego komponentu
        var solution: Solution? = null
        val settings = getSharedPreferences(FINAL_DILUTION_PREFERENCES, 0)
        val storedSolutionName = settings.getString("currentSolution", null)
        if (storedSolutionName != null) {
            solution = solutionGateway.load(storedSolutionName)
            Log.d(TAG, "Retrieved current solution using preferences : $storedSolutionName")
        }
        return solution
    }

    fun setCurrentSolutionOrCreate(currentSolution: Solution?) {
        val settings = getSharedPreferences(FINAL_DILUTION_PREFERENCES, 0)
        val editor = settings.edit()
        editor.putString("currentSolution", currentSolution?.name)
        editor.apply()
        Log.d(TAG, "Stored current solution to preferences : " + (currentSolution?.name ?: "NULL"))
    }

    fun removeCompoundFromEverywhere(compound: Compound) {
        saveCurrentWorkOnSolution()
        for (solution in solutionGateway.loadAll()) {
            val component = solution.getComponentWithCompound(compound)
            if (component != null) {
                solution.removeComponent(component)
                solutionGateway.update(solution)
            }
        }
        currentSolution?.run {
            getComponentWithCompound(compound)?.let { removeComponent(it) }
        }
        compoundGateway.remove(compound)
    }

    fun saveCurrentWorkOnSolution() {
        this.currentSolution?.let { solutionGateway.update(it) }
    }

    companion object {
        private val TAG = "Application Context"
        val FINAL_DILUTION_PREFERENCES = "FINAL_DILUTION_PREFERENCES"
        private lateinit var instance: ApplicationContext

        val context: Context
            get() = instance
    }
}
