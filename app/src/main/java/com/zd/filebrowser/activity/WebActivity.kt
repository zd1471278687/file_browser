package com.zd.filebrowser.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zd.filebrowser.GlobalConstant
import com.zd.filebrowser.R
import kotlinx.android.synthetic.main.activity_webview.*

/**
 * Create by zhangdong 2019/9/24
 */
class WebActivity : AppCompatActivity() {
    private var mUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        initData()
        initClick()
    }

    override fun onBackPressed() {
        if (web_h5?.canGoBack() == true) {
            web_h5?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initData() {
        if (intent != null) {
//            mUrl = intent.getStringExtra(GlobalConstant.IntentConstant.H5_URL)
            mUrl = word_url
        }
        if (TextUtils.isEmpty(mUrl)) {
            finish()
        }
        val settings = web_h5?.settings
        //允许定位
        settings?.setGeolocationEnabled(true)
        settings?.javaScriptEnabled = true
        settings?.useWideViewPort = true
        settings?.loadWithOverviewMode = true
        settings?.domStorageEnabled = true
        settings?.allowContentAccess = false
        settings?.allowFileAccess = false
        settings?.allowFileAccessFromFileURLs = false
        settings?.allowUniversalAccessFromFileURLs = false
        web_h5?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pb_loading?.visibility = if (newProgress >= 100) View.GONE else View.VISIBLE
                pb_loading?.progress = newProgress
            }
        }
        web_h5?.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("zd---", url ?: "")
            }
        }
        web_h5?.loadUrl(mUrl)
    }

    private fun initClick() {
        btn_change_file?.setOnClickListener { ll_item?.visibility = View.VISIBLE }
        btn_confirm?.setOnClickListener {
            val id = rg_file?.checkedRadioButtonId
            when (id) {
                R.id.rb_word -> mUrl = word_url
                R.id.rb_excel -> mUrl = excel_url
                R.id.rb_power -> mUrl = power_point_url
                R.id.rb_pdf -> mUrl = pdf_url
                R.id.rb_zip -> mUrl = zip_url
                R.id.rb_wps -> mUrl = wps_url
                R.id.rb_txt -> mUrl = txt_url
            }
            web_h5?.loadUrl(mUrl)
            ll_item?.visibility = View.GONE
        }
    }

    companion object {
        private const val word_url = "http://vip.ow365.cn/?i=34&n=1&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E5%85%B3%E4%BA%8E%E5%8A%A0%E5%BF%AB%E4%B8%B4%E6%97%B6%E8%AE%BE%E6%96%BD%E5%BB%BA%E8%AE%BE%E9%80%9F%E5%BA%A6%E5%92%8C%E4%BF%9D%E8%AF%81%E5%BB%BA%E8%AE%BE%E6%A0%87%E5%87%86%E7%9A%84%E9%80%9A%E7%9F%A5.docx&p=1"
        private const val excel_url = "http://vip.ow365.cn/?i=34&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E5%9C%A8%E7%BA%BF%E9%A2%84%E8%A7%88Office%E6%9C%8D%E5%8A%A1%E8%B4%B9%E7%94%A8%E8%AF%B4%E6%98%8E%EF%BC%88%E9%A2%84%E8%A7%88%E7%A4%BA%E4%BE%8B%E6%96%87%E4%BB%B6%EF%BC%89.xlsx&p=1"
        private const val power_point_url = "http://vip.ow365.cn/?i=34&n=5&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E6%B7%B1%E5%85%A5%E6%B5%85%E5%87%BAHTML5%E6%B8%B8%E6%88%8F%E5%BC%80%E5%8F%91.pptx&p=1"
        private const val pdf_url = "http://vip.ow365.cn/?i=34&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E4%B8%9A%E7%BB%A9%E5%85%AC%E5%B8%832013%E5%B9%B4%E7%AC%AC%E5%9B%9B%E5%AD%A3%E5%BA%A6%E5%8F%8A%E5%85%A8%E5%B9%B4%E4%B8%9A%E7%BB%A9.pdf&p=1"
        private const val zip_url = "http://vip.ow365.cn/?i=34&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F2010%E5%B9%B4%E9%93%81%E8%B7%AF%E5%9F%BA%E5%BB%BA%E6%9C%88%E6%8A%A5%E7%A9%BA%E8%A1%A8%E8%A6%81%E6%B1%82%E6%83%85%E5%86%B5.rar&p=1"
        private const val wps_url = "http://vip.ow365.cn/?i=34&n=1&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2F%E8%B4%B5%E5%B7%9E%E5%B8%88%E8%8C%83%E5%A4%A7%E5%AD%A6%E5%85%B3%E4%BA%8E%E6%AF%95%E4%B8%9A%E5%A4%A7%E5%AD%A6%E7%94%9F%E5%B0%B1%E4%B8%9A%E6%83%85%E5%86%B5%E7%9A%84%E8%B0%83%E6%9F%A5.wps&p=1"
        private const val txt_url = "http://vip.ow365.cn/?i=34&furl=http%3A%2F%2Fofficeweb365.com%2Fviewfile%2FOfficeWeb365%E5%9C%A8%E7%BA%BF%E9%A2%84%E8%A7%88Word%E6%96%87%E6%A1%A3%E5%8F%8A%E5%85%B6%E5%AE%83%E6%96%87%E6%A1%A3.txt&p=1"
        fun startActivity(activity: AppCompatActivity?, url: String?) {
            if (activity == null) {
                return
            }
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra(GlobalConstant.IntentConstant.H5_URL, url)
            activity.startActivity(intent)
        }
    }
}
