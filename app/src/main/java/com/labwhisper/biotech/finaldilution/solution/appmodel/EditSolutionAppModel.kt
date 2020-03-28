package com.labwhisper.biotech.finaldilution.solution.appmodel

import android.util.Log
import com.labwhisper.biotech.finaldilution.compound.Compound
import com.labwhisper.biotech.finaldilution.compound.CompoundChangePropagator
import com.labwhisper.biotech.finaldilution.peripherals.DataGatewayOperations
import com.labwhisper.biotech.finaldilution.solution.Solution
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class EditSolutionAppModel(
    private val compoundGateway: DataGatewayOperations<Compound>,
    private val solutionGateway: DataGatewayOperations<Solution>
) {

    val compoundList: BehaviorSubject<List<Compound>> =
        BehaviorSubject.createDefault(loadAllCompoundsSorted())

    val solution: BehaviorSubject<Solution> = BehaviorSubject.create()

    private val compoundChangePropagator by lazy {
        CompoundChangePropagator(solutionGateway, compoundGateway)
    }

    val disposable = CompositeDisposable()

    init {
        disposable.add(compoundList.distinctUntilChanged().subscribe {
            reloadSolution()
        })
    }

    //TODO All of the following should be in the interactor
    // and only from there we can access compoundGateway
    // For the time being I am adding gateways here
    // And AppModel should only be a model - no functions!


    private fun loadAllCompoundsSorted() = compoundGateway.loadAll().sorted()

    fun findSolutionsWithCompound(compound: Compound): List<Solution> {
        return solutionGateway.loadAll().filter { solution ->
            solution.components.map { it.compound }.contains(compound)
        }
    }

    fun safeSaveCompound(compound: Compound) {
        //if name exist -> sameNameException ( to be handled by function user )
        compoundGateway.save(compound)
        compoundList.onNext(loadAllCompoundsSorted())
    }

    fun updateCompound(compound: Compound, oldCompound: Compound) {
        compoundChangePropagator.propagateCompoundUpdate(compound, oldCompound)
        compoundList.onNext(loadAllCompoundsSorted())
    }

    //FIXME Add test cases
    fun renameCompound(compound: Compound, oldCompound: Compound) {
        compoundChangePropagator.propagateCompoundRename(compound, oldCompound)
        compoundList.onNext(loadAllCompoundsSorted())
    }

    fun removeCompoundFromEverywhere(compound: Compound) {
        compoundChangePropagator.propagateCompoundRemoval(compound)
        compoundList.onNext(loadAllCompoundsSorted())
    }

    fun updateSolution(updatedSolution: Solution) {
        Log.d(TAG, "Update solution: $updatedSolution")
        solutionGateway.update(updatedSolution)
        solution.onNext(updatedSolution)
    }

    fun renameSolution(updatedSolution: Solution, oldName: String) {
        Log.d(TAG, "Rename solution: $updatedSolution")
        solutionGateway.rename(updatedSolution, oldName)
        solution.onNext(updatedSolution)
    }

    fun reloadSolution() = solution.value?.name?.let { name ->
        solutionGateway.load(name)?.let { updatedSolution ->
            Log.d(TAG, "Reload solution: $updatedSolution")
            solution.onNext(updatedSolution)
        }
    }

    companion object {
        const val TAG = "Edit Solution App Model"
    }

}