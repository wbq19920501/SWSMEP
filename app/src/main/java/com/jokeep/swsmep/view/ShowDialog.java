package com.jokeep.swsmep.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.jokeep.swsmep.R;

/**
 * Created by wbq501 on 2016-1-29 14:04.
 * SWSMEP
 */
public class ShowDialog extends Dialog{
    private TextView text_content;
    private String text;
    public ShowDialog(Context context,String text) {
        super(context);
        this.text = text;
    }

    public ShowDialog(Context context, int themeResId,String text) {
        super(context, themeResId);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        text_content = (TextView) findViewById(R.id.text_content);
        text_content.setText(text);
    }
}
