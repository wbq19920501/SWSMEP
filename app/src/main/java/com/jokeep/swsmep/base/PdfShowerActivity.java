package com.jokeep.swsmep.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jokeep.swsmep.R;
import com.kinggrid.iapppdf.ui.viewer.IAppPDFActivity;
import com.kinggrid.iapppdf.ui.viewer.ViewerActivityController;

/**
 * Created by wbq501 on 2016-1-8 13:31.
 * SWSMEP
 */
public class PdfShowerActivity extends IAppPDFActivity implements View.OnClickListener{

    private static final String TAG = "BookShower";
    final static int TYPE_ANNOT_STAMP = 1;
    private boolean hasLoaded = false;
    private int templateId;

    private FrameLayout frameLayout;
    private static Context context;
    private String url;
    private final static String SDCARD_PATH = Environment
            .getExternalStorageDirectory().getPath().toString();
    private Intent intent;
    private float f = 12;
    private int color = Color.RED;
    private int type = 0;
    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        super.onCreateImpl(savedInstanceState);
        context = this;
        intent = getIntent();
        setScreenPort();
        setContentView(R.layout.activity_pdf);
        frameLayout = (FrameLayout) findViewById(R.id.book_frame);

        findViewById(R.id.layout_annotation_add).setOnClickListener(this);
        findViewById(R.id.layout_annotation_delete).setOnClickListener(this);
        copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU368sLBQG57FhM8Bkq7aPAVrvKeTdxzwi2Wk0Yn+WSxoXx6aD2yiupr6ji7hzsE6/QRx89Izb7etgW5cXVl5PwISjX+xEgRoNggvmgA8zkJXOVWEbHWAH22+t7LdPt+jENUl53rvJabZGBUtMVMHP2J32poSbszHQQyNDZrHtqZuuSgCNRP4FpYjl8hG/IVrYXSo6k2pEkUqzd5hh5kngQSOW8fXpxdRHfEuWC1PB9ruQ=";
        userName = intent.getStringExtra("userName");
        f = intent.getFloatExtra("penf", f);
        color = intent.getIntExtra("pencolor", color);
        type = intent.getIntExtra("pentype",type);
        initPDFView(frameLayout);
        clearPDFReadSettings();
        clearPDFReadSettings();
        setLoadingText(R.string.msg_loading_tip);
        getController().setLoadPageFinishedListener(
                new ViewerActivityController.LoadPageFinishedListener() {

                    @Override
                    public void onLoadPageFinished() {
                        if (!hasLoaded) {
//							loadFieldTemplates();
                            Thread thread = new Thread(new LoadAnnotations());
                            thread.start();
                            hasLoaded = true;
                        }
                    }
                });
        super.setOnKinggridSignListener(new OnKinggridSignListener() {
            @Override
            public void onSaveSign() {
                Log.i(TAG, "onSaveSign");
                savePDF();
            }
            @Override
            public void onCloseSign() {
                Log.i(TAG, "onCloseSign");
            }
            @Override
            public void onClearSign() {
                Log.i(TAG, "onClearSign");
            }
        });
        super.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageChange(String currentPage) {
                Log.i(TAG, "onPageChange:" + currentPage);
            }
        });
        setPenInfo(f,color,type);
    }
    private void savePDF() {
        boolean changpdf = isDocumentModified();
        clearPDFReadSettings();
        if (changpdf){
            doSaveHandwriteInfo(true);
            saveDocument();
        }
    }

    @Override
    protected void loadFieldTemplates() {
        super.loadFieldTemplates();

        setAllAnnotationsVisible(true);
        refreshDocument();

        if (getIntent().hasExtra("template")) {
            templateId = getIntent().getExtras().getInt("template");
            Thread thread = new Thread(new LoadAnnots());
            thread.start();
        }
    }
    public class LoadAnnots implements Runnable{
        @Override
        public void run() {
            refreshDocument();
        }
    }
    public class LoadAnnotations implements Runnable{
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            myhandler.sendMessage(msg);
        }
    }
    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshDocument();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)){
            savePDF();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_annotation_add:
                update();
                openHandwriteAnnotation();
                break;
            case R.id.layout_annotation_delete:
                if (!isDocumentModified()) {
                    Toast.makeText(PdfShowerActivity.this, "未签批", Toast.LENGTH_SHORT).show();
                } else {
                    createDialog(TYPE_ANNOT_STAMP);
                }
                break;
        }
    }
    public boolean isDocumentModified() {
        boolean boo = super.isDocumentModified();
        return boo;
    }
    public static PdfShowerActivity getInstance() {
        return (PdfShowerActivity) context;
    }
    @Override
    protected void onStartImpl() {
        super.onStartImpl();
    }
    @Override
    protected void onResumeImpl() {
        setScreenPort();
        super.onResumeImpl();
    }
    @Override
    protected void onDestroyImpl(boolean finishing) {
        super.onDestroyImpl(finishing);
        clearPDFReadSettings();
        System.exit(0);
    }
    private void createDialog(final int type) {
        final AlertDialog.Builder builder = new Builder(context);
        switch (type){
            case TYPE_ANNOT_STAMP:
                builder.setMessage(getString(R.string.dialog_message_annot_hardwrite));
                break;
        }
        builder.setTitle(getString(R.string.dialog_title));
        builder.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteAllAnnotations(type);
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final Dialog dialog1 = builder.create();
        dialog1.setCancelable(false);
        dialog1.show();
    }
}
