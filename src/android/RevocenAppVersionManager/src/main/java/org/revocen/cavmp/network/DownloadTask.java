package org.revocen.cavmp.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.revocen.cavmp.util.FileUtil;
import org.revocen.cavmp.util.StringUtils;

public class DownloadTask {

	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	private static final int DOWNLOAD_ERROR = 3;

	private static final String MESSAGE_KEY = "message_key";

	public static final String SAVE_PATH = "save_path";
	public static final String FILE_NAME = "file_name";

	private String mUrl;
	private String mFilePath;
	private OnDownloadListener mDownloadListener;
	private Boolean isCancel = false;

	private String mAbsFilePath;
	private int mProgress;
	private String mFileName;
	/**
	 * 是否覆盖已经存在的文件
	 */
	private boolean mCoverDuplicateFile = false;

	public DownloadTask(String url, String savePath) {
		mUrl = url;
		mFilePath = savePath;
	}

	public DownloadTask(String url, String savePath, String fileName) {
		mUrl = url;
		mFilePath = savePath;
		mFileName = fileName;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				mDownloadListener.onDownloadingListener(mProgress);
				break;

			case DOWNLOAD_FINISH:
				Bundle bundle = msg.getData();
				mDownloadListener.onDownloadFinishedListener(bundle.getString(SAVE_PATH), bundle.getString(FILE_NAME));
				break;

			case DOWNLOAD_ERROR:
				mDownloadListener.onDownloadException((Exception) msg.obj);
				break;

			default:
				break;
			}
		};
	};

	public interface OnDownloadListener {
		void onDownloadingListener(int progress);

		void onDownloadFinishedListener(String filePath, String fileName);

		void onDownloadException(Exception ex);
	}

	public OnDownloadListener getDownloadListener() {
		return mDownloadListener;
	}

	public void setOnDownloadListener(OnDownloadListener mDownloadListener) {
		this.mDownloadListener = mDownloadListener;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public void start() {
		new DownadTaskThread().start();
	}

	class DownadTaskThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mAbsFilePath = sdpath + mFilePath;
					URL url = new URL(mUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File folder = new File(mAbsFilePath);
					// 判断文件目录是否存在
					if (!folder.exists()) {
						folder.mkdir();
					}

					String newFileName = "";
					if (StringUtils.isBlank(mFileName)) {
						newFileName = mUrl.substring(mUrl.lastIndexOf("/") + 1);
					} else {
						newFileName = mFileName;
					}
					
					File file = new File(mAbsFilePath, newFileName);
					if (file.exists()) {
						file = handleDuplicateFile(file);
						newFileName = file.getName();
					}				
					
					FileOutputStream fos = new FileOutputStream(file);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						mProgress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString(SAVE_PATH, mAbsFilePath);
							bundle.putString(FILE_NAME, newFileName);
							msg.what = DOWNLOAD_FINISH;
							msg.setData(bundle);
							mHandler.sendMessage(msg);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!isCancel);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.obj = e;
				msg.what = DOWNLOAD_ERROR;
				mHandler.sendMessage(msg);
			}
		}
	}

	public File handleDuplicateFile(File file) {
		FileUtil fileUtil = new FileUtil();
		String realName = fileUtil.getFileName(file);
		String suffixName = fileUtil.getSuffixName(file);
		if (!mCoverDuplicateFile) {
			int icount = 1;
			while (true) {
				File tempFile = new File(mAbsFilePath, realName + "_" + icount + suffixName);
				if (!tempFile.exists()) {
					return tempFile;
				}else {
					icount++;
				}
			}
		}
		return file;
	}

	public boolean isCoverDuplicateFile() {
		return mCoverDuplicateFile;
	}

	public void setCoverDuplicateFile(boolean isCover) {
		this.mCoverDuplicateFile = isCover;
	}

}
