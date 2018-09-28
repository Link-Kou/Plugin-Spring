package com.plugin.javawidget.encryption;


import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author LK
 * @date 2018-04-27 16:59
 */
public class HmacSHA512Utils {

	/**
	 * <p>
	 * HmacSHA512加密消息
	 * </p>
	 *
	 * @param data 要加密的数据Byte
	 * @param key  加密的钥匙
	 * @return String
	 * @throws null
	 **/
	public static String encrypt(String data, String key) {
		String result = "";
		byte[] bytesKey = key.getBytes();
		final SecretKeySpec secretKey = new SecretKeySpec(bytesKey, "HmacSHA512");
		try {
			Mac mac = Mac.getInstance("HmacSHA512");
			mac.init(secretKey);
			final byte[] macData = mac.doFinal(data.getBytes());
			byte[] hex = new Hex().encode(macData);
			result = new String(hex, "ISO-8859-1");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result;
	}

}