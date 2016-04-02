package com.example.changfeng.taptapword

import org.json.JSONException
import org.json.JSONObject

import java.util.UUID

/**
 * Created by changfeng on 2015/4/19.
 */
class Word {
    val uuid: UUID
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
        get() {
            val phone = StringBuilder()
            if (amPhone != null && !amPhone!!.isEmpty()) {
                phone.append("美:[$amPhone] ")
            }
            if (enPhone != null && !enPhone!!.isEmpty()) {
                phone.append("英:[$enPhone]")
            }
            return phone.toString()
        }

    val formatEnPhone: String
        get() {
            if (enPhone != null && !enPhone!!.isEmpty()) {
                return "英:[$enPhone] "
            }
            return ""
        }

    val formatAmPhone: String
        get() {
            if (amPhone != null && !amPhone!!.isEmpty()) {
                return "美:[$enPhone] "
            }
            return ""
        }

    init {
        uuid = UUID.randomUUID()
    }
}
