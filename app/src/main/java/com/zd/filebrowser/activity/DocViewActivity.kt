package com.zd.filebrowser.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import com.baidu.bdocreader.BDocInfo
import com.baidu.bdocreader.BDocView
import com.zd.filebrowser.R
import kotlinx.android.synthetic.main.activity_doc_view.*

/**
 * Create by zhangdong 2019/9/25
 */
class DocViewActivity : AppCompatActivity() {
    private var currentSize = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_doc_view)
        initData()
    }

    private fun initData() {
        val docInfo = this.intent.getParcelableExtra<BDocInfo>("ONE_DOC")
        val mHandler = Handler()
        dv_doc?.setOnDocLoadStateListener(object : BDocView.OnDocLoadStateListener() {
            override fun onDocLoadComplete() {
                Log.d("test", "onDocLoadComplete")
                mHandler.post {
                    progressBar?.visibility = View.GONE
                    dv_doc?.visibility = View.VISIBLE
                }
            }

            override fun onDocLoadFailed(errorDesc: String?) {
                Log.d("test", "onDocLoadFailed errorDesc=$errorDesc")
                // errorDesc format: ERROR_XXXX_DESC(code=xxxxx)
                when {
                    errorDesc?.startsWith(BDocView.ERROR_DESC_BDOCINFO_CHECK_FAILED) == true ->
                        Log.d("test", "bdocInfo is invalid")
                    errorDesc?.startsWith(BDocView.ERROR_DESC_LOAD_RENDER_FALED) == true ->
                        Log.d("test", "load render failed, please connect to Baidu Cloud")
                    errorDesc?.startsWith(BDocView.ERROR_DESC_RENDER_INTERNAL_ERROR) == true -> {
                        Log.d("test", "render error, may be the token is expired")
                        val code =
                            errorDesc.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace(")", "")
                        Log.d("test", "render error, code=$code")
                    }
                }
                mHandler.post { progressBar?.visibility = View.GONE }
            }

            override fun onCurrentPageChanged(currentPage: Int) {
                // 记录当前页面
                Log.i("test", "currentPage = $currentPage")
            }
        })
        dv_doc?.loadDoc(docInfo)
        // 设置字号大小
        tv_bigger?.setOnClickListener {
            // demo步长0.5f, 实际使用时可根据自己需要设置步长(每次点击的增减值)
            currentSize = Math.min(2.0f, currentSize + 0.5f)
            /**
             * setFontSize 可以接受float值的取值范围为(0,2]
             * 即：0到2之间的float值，不包含0，包含2。
             */
            dv_doc?.setFontSize(currentSize)
        }
        tv_smaller?.setOnClickListener {
            // demo步长0.5f, 实际使用时可根据自己需要设置步长(每次点击的增减值)
            currentSize = Math.max(0.5f, currentSize - 0.5f)
            /**
             * setFontSize 可以接受float值的取值范围为(0,2]
             * 即：0到2之间的float值，不包含0，包含2。
             */
            dv_doc?.setFontSize(currentSize)
        }
    }
}
