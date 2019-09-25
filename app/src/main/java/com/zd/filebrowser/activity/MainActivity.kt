package com.zd.filebrowser.activity

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zd.filebrowser.R
import com.zd.filebrowser.util.PermissionMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
        initData()
    }

    private fun initData() {
        btn_baidu?.setOnClickListener {
            val intent = Intent(this@MainActivity, BaiduActivity::class.java)
            startActivity(intent)
        }
        btn_pdf?.setOnClickListener {
            val intent = Intent(this@MainActivity, PDFViewActivity::class.java)
            startActivity(intent)
        }
        btn_web?.setOnClickListener {
            WebActivity.startActivity(this@MainActivity, WORD_URl)
        }
    }

    private fun requestPermission() {
        //文件存取
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        PermissionMediator.checkPermission(this, permissions, object : PermissionMediator.DefaultPermissionRequest() {
            override fun onPermissionRequest(granted: Boolean, permission: String) {
                if (!granted) {
                    finish()
                }
            }

            override fun onPermissionRequest(
                isAllGranted: Boolean,
                permissions: Array<String>?,
                grantResults: IntArray?
            ) {
                if (!isAllGranted) {
                    finish()
                }
            }
        })
    }

    companion object {
        private const val VIDEO_URL = "https://media.w3.org/2010/05/sintel/trailer.mp4"
        private const val IMAGE_URL = "http://cdn.duitang.com/uploads/blog/201312/13/20131213145320_SS48u.jpeg"
        private const val WORD_URl = "http://vip.ow365.cn/?i=34&n=1&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E5%85%B3%E4%BA%8E%E5%8A%A0%E5%BF%AB%E4%B8%B4%E6%97%B6%E8%AE%BE%E6%96%BD%E5%BB%BA%E8%AE%BE%E9%80%9F%E5%BA%A6%E5%92%8C%E4%BF%9D%E8%AF%81%E5%BB%BA%E8%AE%BE%E6%A0%87%E5%87%86%E7%9A%84%E9%80%9A%E7%9F%A5.docx&p=1"
        private const val PDF_RL = "http://vip.ow365.cn/?i=34&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E4%B8%9A%E7%BB%A9%E5%85%AC%E5%B8%832013%E5%B9%B4%E7%AC%AC%E5%9B%9B%E5%AD%A3%E5%BA%A6%E5%8F%8A%E5%85%A8%E5%B9%B4%E4%B8%9A%E7%BB%A9.pdf&p=1"
    }
}
