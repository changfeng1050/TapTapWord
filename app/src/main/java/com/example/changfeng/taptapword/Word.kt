package com.example.changfeng.taptapword

import java.util.*

/**
 * Created by changfeng on 2015/4/19.
 */
class Word {
    val uuid: UUID = UUID.randomUUID()
    var id: Long = 0
    var language: String? = null
    var name: String? = null
    var enPhone: String? = null
    var amPhone: String? = null
    var means: String? = null
    var isArchived = false
    var note: String? = null
    var webExplains: String? = null

    var year: Int = 0
    var month: Int = 0
    var date: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var second: Int = 0

    val formatPhones: String
        get() = "$formatEnPhone $formatAmPhone".trim()

    val formatEnPhone: String
        get() = if (enPhone.isNullOrEmpty()) "" else "英:[$enPhone]"

    val formatAmPhone: String
        get() = if (amPhone.isNullOrEmpty()) "" else "美:[$amPhone]"

}
