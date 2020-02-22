package com.labwhisper.biotech.finaldilution.solution

import android.util.Log
import com.labwhisper.biotech.finaldilution.genericitem.Item
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CareTakerTest {

    val solutionCareTaker = CareTaker<Solution>()

    @BeforeEach
    fun init() {
        solutionCareTaker.clearMementos()
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
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

    @Test
    fun undoWithoutStackRaisesException() {
        Assertions.assertThrows(UndoOnEmptyListException::class.java) {
            solutionCareTaker.undo()
        }
    }

    @Test
    fun undoWithOnlyOneMementoRaisesException() {
        Assertions.assertThrows(UndoOnEmptyListException::class.java) {
            val solution = Solution("solution1")
            solutionCareTaker.addMemento(solution)
            solutionCareTaker.undo()
        }
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

    @Test
    fun redoOnLastIndexRaisesException() {
        Assertions.assertThrows(RedoOnLastChangeException::class.java) {
            val solution1 = Solution("solution1")
            solutionCareTaker.apply {
                addMemento(solution1)
            }
            solutionCareTaker.redo()
        }
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