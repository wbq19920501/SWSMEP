package com.jokeep.swsmep.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.jokeep.swsmep.R;

public class DownloadProgressDialog extends Dialog{

	private DownloadProgressView progressView;
	public DownloadProgressDialog(Context context) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_download_progress);
		progressView = (DownloadProgressView) findViewById(R.id.download_tasks_view);
	}
	
	public void setProgress(int progress){
		progressView.setProgress(progress);
	}
	
	public void setText(String text){
		progressView.setText(text);
	}
}
