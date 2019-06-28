package com.labwhisper.biotech.finaldilution.solution

import com.labwhisper.biotech.finaldilution.genericitem.Item
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CareTakerTest {

    val solutionCareTaker = CareTaker<Solution>()

    @Before
    fun init() {
        solutionCareTaker.clearMementos()
    }

    @Test
    fun addMementoOnlyIfItDiffersFromCurrent() {
        val solution1 = Solution("solution1")
        val solution2 = Solution("solution2")
        solutionCareTaker.addMemento(solution1)
        solutionCareTaker.addMemento(solution2)
        solutionCareTaker.addMemento(solution2)
        val result = solutionCareTaker.undo()
        assertEquals(result, solution1)
    }

    @Test(expected = UndoOnEmptyListException::class)
    fun undoWithoutStackRaisesException() {
        solutionCareTaker.undo()
    }

    @Test(expected = UndoOnEmptyListException::class)
    fun undoWithOnlyOneMementoRaisesException() {
        val solution = Solution("solution1")
        solutionCareTaker.addMemento(solution)
        solutionCareTaker.undo()
    }

    @Test
    fun twoUndosGivesBackOneBeforeLastMemento() {
        val solution1 = Solution("solution1")
        val solution2 = Solution("solution2")
        solutionCareTaker.apply {
            addMemento(solution1)
            addMemento(solution2)
        }
        val result = solutionCareTaker.undo()
        assertEquals(result, solution1)
    }

    @Test(expected = RedoOnLastChangeException::class)
    fun redoOnLastIndexRaisesException() {
        val solution1 = Solution("solution1")
        solutionCareTaker.apply {
            addMemento(solution1)
        }
        solutionCareTaker.redo()
    }

    @Test
    fun redoAfterUndoGivesBackCurrentState() {
        val solution1 = Solution("solution1")
        val solution2 = Solution("solution2")
        solutionCareTaker.apply {
            addMemento(solution1)
            addMemento(solution2)
            undo()
        }
        val result = solutionCareTaker.redo()
        assertEquals(result, solution2)
    }

    @Test
    fun seriesOfUndoRedoGivesProperOrderOfMementos() {
        val saves = listOf(
            Solution("solution1"),
            Solution("solution2"),
            Solution("solution3"),
            Solution("solution4"),
            Solution("solution5")
        )
        saves.forEach { solutionCareTaker.addMemento(it) }
        val testSolutionList = ArrayList<Item?>()

        solutionCareTaker.apply {
            (1..4).map { testSolutionList.add(undo()) }
            (1..4).map { testSolutionList.add(redo()) }
        }

        assertEquals(
            testSolutionList,
            saves
                .dropLast(1)
                .reversed()
                .plus(saves.takeLast(4))
        )
    }


}