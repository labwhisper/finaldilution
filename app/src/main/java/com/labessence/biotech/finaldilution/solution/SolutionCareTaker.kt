package com.labessence.biotech.finaldilution.solution

import java.io.Serializable

class SolutionCareTaker : Serializable {

    //TODO Set undo limit - test performance / data storage
    private var mementos: MutableList<Solution> = ArrayList()
    private var currentPosition: Int = -1

    val canUndo get() = currentPosition > 0
    val canRedo get() = currentPosition < mementos.size - 1


    fun addMemento(memento: Solution) {
        mementos.getOrNull(currentPosition).let { current ->
            if (memento == current) return
        }
        mementos = mementos.filterIndexed { index, _ ->
            index <= currentPosition
        }.toMutableList().apply { add(memento.deepCopy()); currentPosition++ }
    }

    fun clearMementos() {
        mementos.clear()
    }


    fun undo(): Solution {
        if (!canUndo) {
            throw UndoOnEmptyListException()
        }
        //ASSERTION: currentState == memento(currentPosition)
        return mementos[--currentPosition].deepCopy()
    }

    fun redo(): Solution {
        if (!canRedo) {
            throw RedoOnLastChangeException()
        }
        return mementos[++currentPosition].deepCopy()
    }

}