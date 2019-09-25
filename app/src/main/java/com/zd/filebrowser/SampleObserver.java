package com.zd.filebrowser;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.TextView;
import com.baidu.bdocreader.BDocInfo;
import com.baidu.bdocreader.downloader.DocDownloadObserver;
import com.baidu.bdocreader.downloader.DocDownloadableItem;
import com.baidu.bdocreader.downloader.DocDownloadableItem.DownloadStatus;

/**
 * Create by zhangdong 2019/9/25
 */
public class SampleObserver extends DocDownloadObserver {

    TextView tv;
    BDocInfo externDocInfo;
    Handler handler = new Handler(Looper.getMainLooper());

    public SampleObserver(TextView textView, BDocInfo docInfo) {
        tv = textView;
        externDocInfo = docInfo;
    }

    @Override
    public void update(DocDownloadableItem downloader) {
        float progress = downloader.getProgress();

        // DownloadStatus状态比较详尽，但界面显示时仅需要几类状态即可(下载中、已暂停、完成)
        String statusForUI = getStatusForUI(downloader.getStatus());

        String failReson = downloader.getFailReason();
        int errorCode = downloader.getErrorCode();
        String locaDirForThisDoc = downloader.getLocalAbsolutePath();

        if (externDocInfo.getDocId().equals(downloader.getDocId()) && !TextUtils.isEmpty(locaDirForThisDoc)) {
            externDocInfo.setLocalFileDir(locaDirForThisDoc);
        }

        final StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("progress:").append(progress).append("%\t").append("state:").append(statusForUI).append("\n")
                .append("errorCode:").append(errorCode).append("\n")
                .append("failReson:").append(failReson).append("\n").append("savedPath:").append(locaDirForThisDoc);
        handler.post(new Runnable() {

            @Override
            public void run() {
                tv.setText(sbuilder.toString());
            }

        });
    }

    /**
     * DownloadStatus状态比较详尽，但界面显示时仅需要几类状态即可(下载中、已暂停、完成)
     * @param status
     * @return
     */
    public static String getStatusForUI(DownloadStatus status) {
        String statusForUI = null;

        if (status == DownloadStatus.PENDING || status == DownloadStatus.DOWNLOADING) {
            statusForUI = "downloading";
        } else if (status == DownloadStatus.PAUSED || status == DocDownloadableItem.DownloadStatus.ERROR) {
            statusForUI = "paused";
        } else {
            statusForUI = status.name(); // status: "completed" / "deleted manually"
        }

        return statusForUI;
    }
}
