package com.jokeep.swsmep.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jokeep.swsmep.R;
import com.jokeep.swsmep.base.BaseActivity;
import com.jokeep.swsmep.model.UnitBook;

import java.util.ArrayList;
import java.util.List;

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

    TextView phone_name;
    ScrollView manmsg;
    LinearLayout no_msg;

    Intent intent;
    List<UnitBook> listunitbook;
    private String F_OTHERUNITID;
    List<String> listid;
    List<Integer> listint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_manmsg);
        init();
        initdata();
    }

    private void initdata() {
        intent = getIntent();
        listunitbook = new ArrayList<UnitBook>();
        listid = new ArrayList<String>();
        listint = new ArrayList<Integer>();
        phone_name.setText(intent.getExtras().getString("textname"));
        listunitbook = (List<UnitBook>) getIntent().getSerializableExtra("unitbook");
        F_OTHERUNITID = intent.getStringExtra("F_OTHERUNITID").toString();
        for (int i=0;i<listunitbook.size();i++){
            UnitBook unitBook = listunitbook.get(i);
            listid.add(unitBook.getF_OTHERUNITID());
        }
        for (int i=0;i<listid.size();i++){
            if (listid.get(i).toString().equals(F_OTHERUNITID)){
                listint.add(i);
            }
        }
        Log.d("listint",listint.toString()+"-----");
        int length = listint.size();
        if (length == 1){
            UnitBook unitBook = listunitbook.get(0);
            int type = unitBook.getF_TYPE();
            if (type == 0){
                phone1_call2.setVisibility(View.GONE);
                huanbao(unitBook);
            }else {
                phone1_call1.setVisibility(View.GONE);
                yingji(unitBook);
            }
        }else if(length == 2){
            for (int i=0;i<length;i++){
                UnitBook unitBook = listunitbook.get(listint.get(i));
                int type = unitBook.getF_TYPE();
                if (type == 0){
                    huanbao(unitBook);
                }else {
                    yingji(unitBook);
                }
            }
        }else {
            no_msg.setVisibility(View.VISIBLE);
            manmsg.setVisibility(View.GONE);
        }
    }

    private void yingji(UnitBook unitBook) {
        final String f_areacode = unitBook.getF_AREACODE();
        if (f_areacode.equals("")||f_areacode==null){
            area_code2_1.setVisibility(View.GONE);
        }else {
            area_code2_1.setText("区号("+f_areacode+")");
        }
        String officephone1 = unitBook.getF_OFFICEPHONE1();
        String officephone2 = unitBook.getF_OFFICEPHONE2();
        if (officephone1.equals("")||officephone1==null){
            if (officephone2.equals("")||officephone2==null){
                call2.setVisibility(View.GONE);
            }else {
                call2_0.setVisibility(View.GONE);
                call2_1.setText(officephone2);
            }
        }else {
            if (officephone2.equals("")||officephone2==null){
                call2_0.setVisibility(View.GONE);
                call2_1.setText(officephone1);
            }else {
                call2_1.setText(officephone1);
                call2_3.setText(officephone2);
            }
        }
        call2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+call2_1.getText().toString();
                callphone(callnumber);
            }
        });
        call2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+call2_3.getText().toString();
                callphone(callnumber);
            }
        });
        String dutyphone1 = unitBook.getF_DUTYPHONE1();
        String dutyphone2 = unitBook.getF_DUTYPHONE2();
        String dutyphonenote1 = unitBook.getF_DUTYPHONENOTE1();
        String dutyphonenote2 = unitBook.getF_DUTYPHONENOTE2();
        if (dutyphone1.equals("")||dutyphone1==null){
            if (dutyphone2.equals("")||dutyphone2==null){
                callphone2.setVisibility(View.GONE);
            }else {
                callphone2_0.setVisibility(View.GONE);
                callphone2_1.setText(dutyphone2+dutyphonenote2);
            }
        }else {
            if (dutyphone2.equals("")||dutyphone2==null){
                callphone2_0.setVisibility(View.GONE);
                callphone2_1.setText(dutyphone1 + dutyphonenote1);
            }else {
                callphone2_1.setText(dutyphone1 + dutyphonenote1);
                callphone2_3.setText(dutyphone2+dutyphonenote2);
            }
        }
        callphone2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+callphone2_1.getText().toString();
                callphone(callnumber);
            }
        });
        callphone2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode + callphone2_3.getText().toString();
                callphone(callnumber);
            }
        });
        String fax = unitBook.getF_FAX();
        if (fax.equals("")||fax==null){
            fax2_1.setVisibility(View.GONE);
        }else {
            fax2_2.setText(fax);
        }
        String postalcode = unitBook.getF_POSTALCODE();
        if (postalcode.equals("")||fax==null){
            postcode2_1.setVisibility(View.GONE);
        }else {
            postcode2_2.setText(postalcode);
        }
        String email = unitBook.getF_EMAIL();
        if (email.equals("")||fax==null){
            email2_1.setVisibility(View.GONE);
        }else {
            email2_2.setText(email);
        }
        String address = unitBook.getF_ADDRESS();
        if (address.equals("")||fax==null){
            adress2_1.setVisibility(View.GONE);
        }else {
            adress2_2.setText(address);
        }
    }

    private void callphone(String callnumber) {
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callnumber));
        startActivity(intent);
    }

    private void huanbao(UnitBook unitBook) {
        final String f_areacode = unitBook.getF_AREACODE();
        if (f_areacode.equals("")||f_areacode==null){
            area_code1_1.setVisibility(View.GONE);
        }else {
            area_code1_1.setText("区号("+f_areacode+")");
        }
        String officephone1 = unitBook.getF_OFFICEPHONE1();
        String officephone2 = unitBook.getF_OFFICEPHONE2();
        if (officephone1.equals("")||officephone1==null){
            if (officephone2.equals("")||officephone2==null){
                call1.setVisibility(View.GONE);
            }else {
                call1_0.setVisibility(View.GONE);
                call1_1.setText(officephone2);
            }
        }else {
            if (officephone2.equals("")||officephone2==null){
                call1_0.setVisibility(View.GONE);
                call1_1.setText(officephone1);
            }else {
                call1_1.setText(officephone1);
                call1_3.setText(officephone2);
            }
        }
        call1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+call1_1.getText().toString();
                callphone(callnumber);
            }
        });
        call1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+call1_3.getText().toString();
                callphone(callnumber);
            }
        });
        String dutyphone1 = unitBook.getF_DUTYPHONE1();
        String dutyphone2 = unitBook.getF_DUTYPHONE2();
        String dutyphonenote1 = unitBook.getF_DUTYPHONENOTE1();
        String dutyphonenote2 = unitBook.getF_DUTYPHONENOTE2();
        if (dutyphone1.equals("")||dutyphone1==null){
            if (dutyphone2.equals("")||dutyphone2==null){
                callphone1.setVisibility(View.GONE);
            }else {
                callphone1_0.setVisibility(View.GONE);
                callphone1_1.setText(dutyphone2+dutyphonenote2);
            }
        }else {
            if (dutyphone2.equals("")||dutyphone2==null){
                callphone1_0.setVisibility(View.GONE);
                callphone1_1.setText(dutyphone1 + dutyphonenote1);
            }else {
                callphone1_1.setText(dutyphone1 + dutyphonenote1);
                callphone1_3.setText(dutyphone2+dutyphonenote2);
            }
        }
        callphone1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+callphone1_1.getText().toString();
                callphone(callnumber);
            }
        });
        callphone1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callnumber = f_areacode+callphone1_3.getText().toString();
                callphone(callnumber);
            }
        });
        String fax = unitBook.getF_FAX();
        if (fax.equals("")||fax==null){
            fax1_1.setVisibility(View.GONE);
        }else {
            fax1_2.setText(fax);
        }
        String postalcode = unitBook.getF_POSTALCODE();
        if (postalcode.equals("")||fax==null){
            postcode1_1.setVisibility(View.GONE);
        }else {
            postcode1_2.setText(postalcode);
        }
        String email = unitBook.getF_EMAIL();
        if (email.equals("")||fax==null){
            email1_1.setVisibility(View.GONE);
        }else {
            email1_2.setText(email);
        }
        String address = unitBook.getF_ADDRESS();
        if (address.equals("")||fax==null){
            adress1_1.setVisibility(View.GONE);
        }else {
            adress1_2.setText(address);
        }
    }

    private void init() {
        phone_name = (TextView) findViewById(R.id.phone_name);
        manmsg = (ScrollView) findViewById(R.id.manmsg);
        no_msg = (LinearLayout) findViewById(R.id.no_msg);
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
