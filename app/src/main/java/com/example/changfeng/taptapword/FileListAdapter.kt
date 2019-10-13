/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.changfeng.taptapword

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.find

class FileListAdapter(context: Context, private val resourceId: Int,
                      objects: List<FileInfo>) : ArrayAdapter<FileInfo>(context, resourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val fileInfo = getItem(position)!!

        val view: View = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.file_list_item, parent, false)
        view.find<TextView>(R.id.list_item).text = fileInfo.fileName
        return view
    }
}
