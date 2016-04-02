package com.example.changfeng.taptapword

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by changfeng on 2015/5/4.
 */
class HelpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_help, container, false)
        return view
    }

    companion object {
        private val TAG = "HelpFragment"
    }
}
