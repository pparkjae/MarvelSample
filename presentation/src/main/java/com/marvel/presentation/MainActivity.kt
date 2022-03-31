package com.marvel.presentation

import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        registerReceiver(onDownloadComplete, IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(onDownloadComplete)
        } catch (e: Exception) {
        }
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                try {
                    val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    val cursor = manager.query(query)

                    if (!cursor.moveToFirst()) {
                        return
                    }
                    val downloadLocalUri = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                    val downloadLocalType = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)

                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(columnIndex)
                    val fileUri = cursor.getString(downloadLocalUri)
                    val fileType = cursor.getString(downloadLocalType)

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileType)

                        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(
                                FileProvider.getUriForFile(
                                    applicationContext,
                                    "$packageName.provider",
                                    File(
                                        Uri.parse(fileUri).path!!
                                    )
                                ), mimeType
                            )
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        })
                    }
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
}