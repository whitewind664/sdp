package com.github.gogetters.letsgo.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class CircularListTest {

    @Test
    fun sizeCorrespondsToNumberOfElements() {
        val circularList = CircularList(listOf(1, 2))
        assertEquals(2, circularList.size)
    }

    @Test
    fun listContainsAddedElement() {
        val circularList = CircularList(listOf(1, 2))
        assertTrue(circularList.contains(1) && circularList.contains(2))
    }

    @Test
    fun listContainsAllFromUnderlying() {
        val underlying = listOf(1, 2)
        val circularList = CircularList(underlying)
        assertTrue(circularList.containsAll(underlying))
    }

    @Test
    fun canGetElementOutsideOfSize() {
        val circularList = CircularList(listOf(1, 2))
        assertEquals(2, circularList[5])
    }

    @Test
    fun indexOfElementIsSmallerThanSize() {
        val circularList = CircularList(listOf(1, 2))
        assertTrue(circularList.indexOf(1) < circularList.size)
    }

    @Test
    fun newlyCreatedListIsEmpty() {
        val circularList = CircularList<Int>(listOf())
        assertEquals(true, circularList.isEmpty())
    }

    @Test
    fun iteratorNeverStops() {
        val circularList = CircularList(listOf(1, 2))
        val it = circularList.iterator()
        it.next()
        it.next()
        assertTrue(it.hasNext())
        assertEquals(1, it.next())
        assertTrue(it.hasNext())
    }

    @Test
    fun lastIndexOfIsSmallerThanSize() {
        val circularList = CircularList(listOf(1, 2))
        assertTrue(circularList.lastIndexOf(2) < circularList.size)
    }

    @Test
    fun listIteratorHasNextWhenListIsNotEmpty() {
        val circularList = CircularList(listOf(1, 2))
        assertTrue(circularList.listIterator().hasNext())
    }

    @Test
    fun listIteratorFromLastIndexHasOnlyOneNext() {
        val circularList = CircularList(listOf(1, 2))
        val listIterator = circularList.listIterator(1)
        listIterator.next()
        assertFalse(listIterator.hasNext())
    }

    @Test
    fun sublistIsStillCircular() {
        val circularList = CircularList(listOf(1, 2))
        assertEquals(1, circularList.subList(0, 1)[5])
    }
}