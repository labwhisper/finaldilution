package com.labessence.biotech.finaldilution

import android.app.Application
import android.content.Context
import android.util.Log
import com.labessence.biotech.finaldilution.compound.Compound
import com.labessence.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labessence.biotech.finaldilution.solution.Solution

class ApplicationContext : Application() {
    //TODO Remove currentSolution from here and pass it with Activities
    var currentSolution: Solution? = null
        get() = field ?: loadSolution()
        set(value) {
            setCurrentSolutionOrCreate(value)
            field = value
        }
    var solutionGateway: DataGatewayOperations<Solution>? = null
    //        this.compoundGateway.save(new Compound("KCl", 74.55));
    //        this.compoundGateway.save(new Compound("NaCl", 58.44));
    //        this.compoundGateway.save(new Compound("EDTA", 372.24));
    //        this.compoundGateway.save(new Compound("MgCl2", 95.21));
    //        this.compoundGateway.save(new Compound("TRIS", 121.14));
    //        this.compoundGateway.save(new Compound("SDS", 288.372));
    var compoundGateway: DataGatewayOperations<Compound>? = null

    init {
        instance = this
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
            solution = solutionGateway!!.load(storedSolutionName) ?: null
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
        for (solution in solutionGateway!!.loadAll()) {
            if (solution.getComponentWithCompound(compound) != null) {
                solution.removeComponent(solution.getComponentWithCompound(compound)!!)
                solutionGateway!!.update(solution)
            }
        }
        if (currentSolution!!.getComponentWithCompound(compound) != null) {
            currentSolution!!.removeComponent(currentSolution!!.getComponentWithCompound(compound)!!)
        }
        compoundGateway!!.remove(compound)
    }

    fun saveCurrentWorkOnSolution() {
        solutionGateway!!.update(this.currentSolution!!)
    }

    companion object {
        private val TAG = "Application Context"
        val FINAL_DILUTION_PREFERENCES = "FINAL_DILUTION_PREFERENCES"
        private lateinit var instance: ApplicationContext

        val context: Context
            get() = instance
    }
}
