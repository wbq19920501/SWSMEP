package com.jokeep.swsmep.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;

/**
 * Created by wbq501 on 2016-2-17 09:57.
 * SWSMEP
 */
public class PhoneManMsgActivity extends BaseActivity{
    LinearLayout back;

    LinearLayout phone1_call1,phone1_call2;
    TextView area_code1_1,area_code2_1;
    LinearLayout call1,call1_0,callphone1,callphone1_0,fax1_1
                ,postcode1_1,email1_1,adress1_1;

    TextView call1_1,call1_3,callphone1_1,callphone1_3,fax1_2
            ,postcode1_2,email1_2,adress1_2;
    ImageView call1_2,call1_4,callphone1_2,callphone1_4;

    LinearLayout call2,call2_0,callphone2,callphone2_0,fax2_1
            ,postcode2_1,email2_1,adress2_1;

    TextView call2_1,call2_3,callphone2_1,callphone2_3,fax2_2
            ,postcode2_2,email2_2,adress2_2;
    ImageView call2_2,call2_4,callphone2_2,callphone2_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_manmsg);
        init();
        initdata();
    }

    private void initdata() {

    }

    private void init() {
        phone1_call1 = (LinearLayout) findViewById(R.id.phone1_call1);
        phone1_call2 = (LinearLayout) findViewById(R.id.phone1_call2);

        area_code1_1 = (TextView) findViewById(R.id.area_code1_1);
        call1 = (LinearLayout) findViewById(R.id.call1);
        call1_0 = (LinearLayout) findViewById(R.id.call1_0);
        callphone1 = (LinearLayout) findViewById(R.id.callphone1);
        callphone1_0 = (LinearLayout) findViewById(R.id.callphone1_0);
        fax1_1 = (LinearLayout) findViewById(R.id.fax1_1);
        postcode1_1 = (LinearLayout) findViewById(R.id.postcode1_1);
        email1_1 = (LinearLayout) findViewById(R.id.email1_1);
        adress1_1 = (LinearLayout) findViewById(R.id.adress1_1);

        call1_1 = (TextView) findViewById(R.id.call1_1);
        call1_3 = (TextView) findViewById(R.id.call1_3);
        callphone1_1 = (TextView) findViewById(R.id.callphone1_1);
        callphone1_3 = (TextView) findViewById(R.id.callphone1_3);
        fax1_2 = (TextView) findViewById(R.id.fax1_2);
        postcode1_2 = (TextView) findViewById(R.id.postcode1_2);
        email1_2 = (TextView) findViewById(R.id.email1_2);
        adress1_2 = (TextView) findViewById(R.id.adress1_2);

        call1_2 = (ImageView) findViewById(R.id.call1_2);
        call1_4 = (ImageView) findViewById(R.id.call1_4);
        callphone1_2 = (ImageView) findViewById(R.id.callphone1_2);
        callphone1_4 = (ImageView) findViewById(R.id.callphone1_4);


        area_code2_1 = (TextView) findViewById(R.id.area_code2_1);
        call2 = (LinearLayout) findViewById(R.id.call2);
        call2_0 = (LinearLayout) findViewById(R.id.call2_0);
        callphone2 = (LinearLayout) findViewById(R.id.callphone2);
        callphone2_0 = (LinearLayout) findViewById(R.id.callphone2_0);
        fax2_1 = (LinearLayout) findViewById(R.id.fax2_1);
        postcode2_1 = (LinearLayout) findViewById(R.id.postcode2_1);
        email2_1 = (LinearLayout) findViewById(R.id.email2_1);
        adress2_1 = (LinearLayout) findViewById(R.id.adress2_1);

        call2_1 = (TextView) findViewById(R.id.call2_1);
        call2_3 = (TextView) findViewById(R.id.call2_3);
        callphone2_1 = (TextView) findViewById(R.id.callphone2_1);
        callphone2_3 = (TextView) findViewById(R.id.callphone2_3);
        fax2_2 = (TextView) findViewById(R.id.fax2_2);
        postcode2_2 = (TextView) findViewById(R.id.postcode2_2);
        email2_2 = (TextView) findViewById(R.id.email2_2);
        adress2_2 = (TextView) findViewById(R.id.adress2_2);

        call2_2 = (ImageView) findViewById(R.id.call2_2);
        call2_4 = (ImageView) findViewById(R.id.call2_4);
        callphone2_2 = (ImageView) findViewById(R.id.callphone2_2);
        callphone2_4 = (ImageView) findViewById(R.id.callphone2_4);

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnim();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitAnim() {
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
}
