package com.github.gogetters.letsgo.utils

class CircularList<E>(private val underlyingList: List<E>): List<E> {
    override val size: Int get() = underlyingList.size

    override fun contains(element: E): Boolean = underlyingList.contains(element)

    override fun containsAll(elements: Collection<E>): Boolean = underlyingList.containsAll(elements)

    override fun get(index: Int): E = underlyingList[index % size]

    override fun indexOf(element: E): Int = underlyingList.indexOf(element)

    override fun isEmpty(): Boolean = underlyingList.isEmpty()

    override fun iterator(): Iterator<E> {
        return object : Iterator<E> {
            val underlyingIterator = underlyingList.iterator()
            override fun hasNext(): Boolean {
                return underlyingList.isNotEmpty()
            }

            override fun next(): E {
                return if (underlyingIterator.hasNext()) underlyingIterator.next()
                else underlyingList.iterator().next()
            }
        }
    }

    override fun lastIndexOf(element: E): Int = underlyingList.lastIndexOf(element)

    override fun listIterator(): ListIterator<E> = underlyingList.listIterator()

    override fun listIterator(index: Int): ListIterator<E> = underlyingList.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<E> =
            CircularList(underlyingList.subList(fromIndex, toIndex))

}