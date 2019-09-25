package com.zd.filebrowser.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.shockwave.pdfium.PdfDocument
import com.zd.filebrowser.R
import com.zd.filebrowser.util.FileUtil
import kotlinx.android.synthetic.main.activity_pdf_viewer.*

/**
 * Create by zhangdong 2019/9/25
 */
class PDFViewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener,
    OnPageErrorListener {

    private val TAG = PDFViewActivity::class.java.simpleName

    private val REQUEST_CODE = 42

    private val SAMPLE_FILE = "sample.pdf"
    private var pdfFileName: String? = ""
    private var pageNumber: Int? = 0
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data
            displayFromUri(uri)
        }
    }

    private fun initData() {
        pdfView.setBackgroundColor(Color.LTGRAY)
        if (uri != null) {
            displayFromUri(uri)
        } else {
            displayFromAsset(SAMPLE_FILE)
        }
        title = pdfFileName
        btn_pick_file?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            try {
                startActivityForResult(intent, REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                //alert user that file manager not working
                Toast.makeText(this, R.string.toast_pick_file_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayFromAsset(assetFileName: String) {
        if (TextUtils.isEmpty(assetFileName)) {
            return
        }
        pdfFileName = assetFileName
        if (pdfView != null) {
            pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber ?: 0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
//                .pageFitPolicy(FitPolicy.BOTH)
                .load()
        }
    }

    private fun displayFromUri(uri: Uri?) {
        if (uri == null) {
            return
        }
        pdfFileName = FileUtil.getFileName(this, uri)

        pdfView.fromUri(uri)
            .defaultPage(pageNumber ?: 0)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .spacing(10) // in dp
            .onPageError(this)
            .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        title = String.format("%s %s / %s", pdfFileName, page + 1, pageCount)
    }

    override fun loadComplete(nbPages: Int) {
        val meta = pdfView.documentMeta
        Log.e(TAG, "title = " + meta.title)
        Log.e(TAG, "author = " + meta.author)
        Log.e(TAG, "subject = " + meta.subject)
        Log.e(TAG, "keywords = " + meta.keywords)
        Log.e(TAG, "creator = " + meta.creator)
        Log.e(TAG, "producer = " + meta.producer)
        Log.e(TAG, "creationDate = " + meta.creationDate)
        Log.e(TAG, "modDate = " + meta.modDate)

        printBookmarksTree(pdfView.tableOfContents, "-")
    }

    override fun onPageError(page: Int, t: Throwable?) {
        Log.e(TAG, "Cannot load page $page")
    }

    private fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
        for (b in tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.title, b.pageIdx))

            if (b.hasChildren()) {
                printBookmarksTree(b.children, "$sep-")
            }
        }
    }
}
