package com.example.changfeng.taptapword

import com.example.changfeng.taptapword.util.isEnglishWord
import org.junit.Test

import org.junit.Assert.*

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class ExampleUnitTest {
//    @Test
//    @Throws(Exception::class)
//    fun addition_isCorrect() {
//        assertEquals(4, 2+2)
//    }

    @Test
    @Throws(Exception::class)
    fun isWordCorrect() {
        val word1 = "Java`s"
        assertEquals(true, word1.isEnglishWord())

        val word2 = "Java’s"
        assertEquals(true, word2.isEnglishWord())

        val word3 = "https://i.umeng.com/user/products"
        assertEquals(false, word3.isEnglishWord())
    }


}