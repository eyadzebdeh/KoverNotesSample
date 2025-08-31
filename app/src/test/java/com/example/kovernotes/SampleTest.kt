package com.example.kovernotes

import org.junit.Test
import org.junit.Assert.assertEquals

class SampleTest {
    @Test
    fun testAdd() {
        val result = Sample.add(2, 3)
        assertEquals(5, result)
    }
}
