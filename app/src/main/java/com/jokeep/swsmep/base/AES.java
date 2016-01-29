package com.jokeep.swsmep.base;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	public static String key = "";
	public static String iv = "";

	public static String encrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			int blockSize = cipher.getBlockSize();

			byte[] dataBytes = data.getBytes();
			int plaintextLength = dataBytes.length;
			if (plaintextLength % blockSize != 0) {
				plaintextLength = plaintextLength
						+ (blockSize - (plaintextLength % blockSize));
			}

			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);
			return new BASE64Encoder().encode(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String desEncrypt(String data) {
		try {
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			byte[] original = cipher.doFinal(encrypted1);
			byte[] a = original;
			byte[] tmp = new byte[data.length()];
			int i = 0;
			for (byte x : a) {
				if (x != 0) {
					tmp[i++] = x;
				}
			}
			a = new byte[i];
			for (int j = 0; j < i; j++) {
				a[j] = tmp[j];
			}
			String originalString = new String(a);
			return originalString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
