package com.jokeep.swsmep.base;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by wbq501 on 2016-1-27 09:46.
 * SWSMEP
 */
public class Client {
    private String action = "";
    private String message = "";
    private String uuid = "";
    private String res = "";

    private JSONObject json;

    private String nameSpace = "http://tempuri.org/";
    private String soapAction;

    private SoapObject rpc;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE transport;

    public Client(String action, String uuid) {
        this.action = action;
        this.uuid = uuid;
    }

    public Client(String action, String message, String uuid) {
        this.action = action;
        this.message = message;
        this.uuid = uuid;
    }
    public void callKey() {
        try {
            rpc = new SoapObject(nameSpace, action);
            rpc.addProperty(SaveMsg.TOKENID, uuid);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            transport = new HttpTransportSE(HttpIP.IP);
            soapAction = nameSpace + action;
            transport.call(soapAction, envelope);

            res = envelope.getResponse().toString();
        } catch (Exception e) {
            res = SaveMsg.ERRORMSG;
            e.printStackTrace();
        }
    }
    public void calllogin(){
        try {
            rpc = new SoapObject(nameSpace, HttpIP.Login);
            rpc.addProperty("parameter", AES.encrypt(message));
            rpc.addProperty(SaveMsg.TOKENID, uuid);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            transport = new HttpTransportSE(HttpIP.IP);
            soapAction = nameSpace + HttpIP.Login;
            transport.call(soapAction, envelope);

            res = AES.desEncrypt(envelope.getResponse().toString());
        } catch (Exception e) {
            res = SaveMsg.ERRORMSG;
            e.printStackTrace();
        }
    }
    public String getResponseString() {
        return res;
    }

    // 封装字节数组与参数
    public static byte[] getPacket(String json, byte[] image) {
        byte[] jsonb = json.getBytes();
        int length = image.length + jsonb.length;
        System.out.println(image.length + "    " + jsonb.length);
        byte[] bytes = new byte[length + 1];
        byte[] lengthb = InttoByteArray(jsonb.length, 1);
        System.arraycopy(lengthb, 0, bytes, 0, 1);
        System.arraycopy(jsonb, 0, bytes, 1, jsonb.length);
        System.arraycopy(image, 0, bytes, 1 + jsonb.length, image.length);
        return bytes;
    }
    // 将int转换为字节数组
    public static byte[] InttoByteArray(int iSource, int iArrayLen) {
        byte[] bLocalArr = new byte[iArrayLen];
        for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
            bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
        }
        return bLocalArr;
    }
}
