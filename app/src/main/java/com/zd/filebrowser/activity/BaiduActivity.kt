package com.zd.filebrowser.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.baidu.bdocreader.BDocInfo
import com.baidu.bdocreader.downloader.DocDownloadManager
import com.baidu.bdocreader.downloader.DocDownloadableItem
import com.zd.filebrowser.R
import com.zd.filebrowser.SampleObserver
import kotlinx.android.synthetic.main.activity_baidu.*

/**
 * Create by zhangdong 2019/9/25
 */
class BaiduActivity : AppCompatActivity() {
    private var docInfo: BDocInfo? = null
    private var observer: SampleObserver? = null
    // Warning: 如果将该对象引用 放在Application子类中，要确认用户名发生改变时，重新调用getInstance，会得到一个新的对象
    private var downloadManager: DocDownloadManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baidu)
        initData()
        initOnClick()
    }

    private fun initData() {
        downloadManager = DocDownloadManager.getInstance(this,
            USER_NAME
        )
        /**
         * 特注：因为token有时间限制，以下字段请填写为您自己的信息，否则文档无法显示。
         */
        val host = "BCEDOC" // 百度云传回的host
        val docId = "doc-gkjraanw4f89uu5" // 百度云传回的docId
        val docType = "doc" // 文档类型 doc/ppt/ppts等
        val token = "TOKEN" // 百度云传回的token
        val thisDocDir = "" // 文档下载器会返回文档的下载路径，到时通过docInfo.setLocalFileDir更新。
        // !!特注!!：以上目录，指定为空串""时表示在线浏览
        val totalPage = 3 // 总页数，必须准确填写 否则在离线浏览时会有问题
        val docTitle = "百度云文档服务"
        val startPage = 1 // 起始浏览页，最小值为1，请不要填入小于1的值

        docInfo = BDocInfo(host, docId, docType, token)
            .setLocalFileDir(thisDocDir)
            .setTotalPage(totalPage)
            .setDocTitle(docTitle)
            .setStartPage(startPage)

        observer = SampleObserver(tvState, docInfo)

        tvurl?.text = "Current docId: " + docInfo?.docId
        val item = downloadManager?.getDocDownloadableItemByDocId(docInfo?.docId)
        if (item != null) {
            item.addDocObserver(observer)
            observer?.update(item)
        } else {
            tvState.text = "state: not started"
        }
    }

    private fun initOnClick() {
        btnStart?.setOnClickListener {
            downloadManager?.startOrResumeDownloader(
                docInfo?.docId, docInfo?.token, docInfo?.host,
                observer
            )
        }
        btnPause?.setOnClickListener {
            downloadManager?.pauseDownloader(docInfo?.docId)
        }
        btnDelete?.setOnClickListener {
            downloadManager?.deleteDownloader(docInfo?.docId)
        }
        btnPlay?.setOnClickListener {
            val downloadItem = downloadManager?.getDocDownloadableItemByDocId(docInfo?.docId)
            if (downloadItem != null && downloadItem.status == DocDownloadableItem.DownloadStatus.COMPLETED) {
                val intent = Intent(this, DocViewActivity::class.java)
                intent.putExtra("ONE_DOC", docInfo)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Downloading Task is not Completed", Toast.LENGTH_SHORT).show()
            }
        }
        btnPlayOnline?.setOnClickListener {
            val intent = Intent(this, DocViewActivity::class.java)
            intent.putExtra("ONE_DOC", docInfo)
            startActivity(intent)
        }
    }

    companion object {
        private const val VIDEO_URL = "https://media.w3.org/2010/05/sintel/trailer.mp4"
        private const val IMAGE_URL = "http://cdn.duitang.com/uploads/blog/201312/13/20131213145320_SS48u.jpeg"
        private const val DOX_URL = "newteach.pbworks.com%2Ff%2Fele%2Bnewsletter.docx"
        private const val USER_NAME = "zhangdong"
    }
}
