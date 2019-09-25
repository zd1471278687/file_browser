package com.zd.filebrowser.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

/**
 * Create by zhangdong 2019/9/25
 */
object FileUtil {

    fun getFileName(context: Context?, uri: Uri?): String? {
        if (context == null || uri == null) {
            return ""
        }
        var result: String? = null
        if ("content" == uri.scheme) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }
}
