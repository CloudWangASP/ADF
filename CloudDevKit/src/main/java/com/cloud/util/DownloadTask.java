package com.cloud.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.cloud.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<String, Integer, File> {

	private Context mContext;
	private ProgressDialog mProgressDialog;
	private int fileLength;

	public DownloadTask(Context context) {
		mContext = context;
		
		showDownloadProgress();
	}

	@Override
	protected File doInBackground(String... params) {
		// use URLConnection
		try {
			URL url = new URL(params[0]);
			URLConnection connection = url.openConnection();
			connection.connect();

			fileLength = connection.getContentLength();
			Logger.d("apk length=" + fileLength);
			InputStream inputStream = new BufferedInputStream(url.openStream());
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/chio2o_version.apk");
			OutputStream outputStream = new FileOutputStream(file);
 
			byte[] data = new byte[1024];
			long total = 0;
			int count;
			while ((count = inputStream.read(data)) != -1) {
				total += count;
				// publishing the progress....
				publishProgress((int) (total * 100 / fileLength));
				outputStream.write(data, 0, count);
			}

			outputStream.flush();
			outputStream.close();
			inputStream.close();

			return file;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		
		int progress = values[0];
		mProgressDialog.setProgress(progress);
	}
	
	@Override
	protected void onPostExecute(File file) {
		super.onPostExecute(file);
		
		if (file != null) {
			// 安装应用
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), 
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);				
			
			/*
			HouseApplication app = (HouseApplication) mContext.getApplicationContext();
			app.getSettings().setAppLastUpdate();
			app.getSettings().setInAppLastUpdate();
			*/
			
			Activity activity = (Activity) mContext;
			activity.finish();
		} else {
			mProgressDialog.setCancelable(true);
			Toast.makeText(mContext.getApplicationContext(), 
					com.cloud.R.string.download_apk_file_fail, Toast.LENGTH_SHORT).show();
		}
		
		mProgressDialog.dismiss();
	}
	
	/**
	 * 显示下载的进度
	 */
	private void showDownloadProgress() {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(R.string.download_title_string);
		mProgressDialog.setMessage(mContext.getResources().getString(R.string.download_tip_string));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}

}
