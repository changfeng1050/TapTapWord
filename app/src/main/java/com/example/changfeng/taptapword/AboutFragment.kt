package com.example.changfeng.taptapword

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by changfeng on 2015/4/17.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_about, container, false)
        val versionTextView = view?.findViewById(R.id.version_text_view) as TextView
        versionTextView.text = version

        return view
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    val version: String
        get() {
            try {
                return activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return ""
            }

        }

    companion object {

        private val TAG = "AboutFragment"
    }
}
