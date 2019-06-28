package com.labwhisper.biotech.finaldilution.solution

import com.labwhisper.biotech.finaldilution.genericitem.Item
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class CareTaker<T : Item> : Serializable {

    //TODO Set undo limit - test performance / data storage
    private var mementos: MutableList<T> = ArrayList()
    private var currentPosition: Int = -1

    val canUndo get() = currentPosition > 0
    val canRedo get() = currentPosition < mementos.size - 1


    fun addMemento(memento: T) {
        mementos.getOrNull(currentPosition).let { current ->
            if (memento == current) return
        }
        mementos = mementos.filterIndexed { index, _ ->
            index <= currentPosition
        }.toMutableList().apply { add(memento.deepCopy() as T); currentPosition++ }
    }

    fun clearMementos() {
        mementos.clear()
    }


    fun undo(): T {
        if (!canUndo) {
            throw UndoOnEmptyListException()
        }
        //ASSERTION: currentState == memento(currentPosition)
        return mementos[--currentPosition].deepCopy() as T
    }

    fun redo(): T {
        if (!canRedo) {
            throw RedoOnLastChangeException()
        }
        return mementos[++currentPosition].deepCopy() as T
    }

}