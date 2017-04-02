package com.example.changfeng.taptapword.util

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by changfeng on 2015/4/23.
 */
class MyFile internal constructor(private val mAppComtext: Context) {
    companion object {
        private val TAG = "MyFile"


        fun createDir(): Boolean {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                var sdDir = Environment.getExternalStorageDirectory().absolutePath
                sdDir += File.separator + "command"
                val newFile = File(sdDir)
                if (!newFile.exists()) {
                    newFile.mkdir()
                    //                Log.d(TAG, "createFile() called() "+ newFile.getAbsolutePath());
                    return true
                }
            }
            return false
        }

        fun createFile(): Boolean {
            val file = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "sharedMemory.txt")
            if (!file.exists()) {
                try {
                    file.createNewFile()
                } catch (e: Exception) {
                }

            }
            return false
        }

        val outputBackupFile: File?
            get() {

                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val backDir = Environment.getExternalStorageDirectory()

                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

                    return File(backDir.path + File.separator + "TapTapWord_" + timeStamp + ".json")
                } else {
                    return null
                }
            }


        fun deleleFile(fileName: String): Boolean {
            val file = File(fileName)
            if (file.isFile) {
                return file.delete()
            }
            return false
        }


        fun copyFile(oldPath: String, newPath: String): Boolean {
            try {
                var bytesum = 0
                var byteread = 0
                val oldfile = File(oldPath)
                val newfile = File(newPath)
                if (!newfile.exists()) {
                    newfile.createNewFile()
                }
                if (oldfile.exists()) {
                    // 文件存在时
                    val inStream = FileInputStream(oldPath) // 读入原文件
                    val fs = FileOutputStream(newPath)
                    val buffer = ByteArray(1444)
                    byteread = inStream.read(buffer)
                    while (byteread != -1) {
                        bytesum += byteread // 字节数 文件大小
                        fs.write(buffer, 0, byteread)
                        byteread = inStream.read(buffer)
                    }
                    inStream.close()
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false

            }

        }
    }


}
