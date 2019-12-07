package com.labwhisper.biotech.finaldilution.solution

import junit.framework.TestCase
import org.junit.Test

class VolumeDisplayTest {

    val solution = Solution("sol1")

    @Test
    fun `Volume zero should display 0 ml`() {
        TestCase.assertEquals("0 ml", solution.apply { volume = 0.0 }.displayVolume())
    }

    @Test
    fun `Volume greater then 1l should display l`() {
        TestCase.assertEquals("1.2 l", solution.apply { volume = 1200.0 }.displayVolume())
    }

    @Test
    fun `Volume 1l should display l`() {
        TestCase.assertEquals("1 l", solution.apply { volume = 1000.0 }.displayVolume())
    }

    @Test
    fun `Volume lesser then 1l should display ml`() {
        TestCase.assertEquals("800 ml", solution.apply { volume = 800.0 }.displayVolume())
    }

    @Test
    fun `Volume greater then 1ml should display ml`() {
        TestCase.assertEquals("1.2 ml", solution.apply { volume = 1.2 }.displayVolume())
    }

    @Test
    fun `Volume 1ml should display ml`() {
        TestCase.assertEquals("1 ml", solution.apply { volume = 1.0 }.displayVolume())
    }

    @Test
    fun `Volume lesser then 1ml should display ml`() {
        TestCase.assertEquals("800 \u03bcl", solution.apply { volume = 0.8 }.displayVolume())
    }

}