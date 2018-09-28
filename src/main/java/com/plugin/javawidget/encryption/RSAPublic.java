package com.plugin.javawidget.encryption;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by LK on 2016/10/31.
 */
public class RSAPublic {

	/**
	 * <p>
	 * 用公钥对加密消息解密
	 * </p>
	 *
	 * @param data      解密数据Byte
	 * @param publicKey 私有密钥
	 * @return String
	 * @throws null
	 **/
	public static String encrypt(byte[] data, String publicKey) {
		try {
			byte[] keyBytes = Base64.decode(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > 117) {
					cache = cipher.doFinal(data, offSet, 117);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * 117;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			return Base64.toBase64String(encryptedData).replace("\r\n", "");
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 *
	 * @param data      需要驗證的数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * @param SignType  数字签名方式 {@link java.security.Signature}
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String publicKey, String sign, String SignType) {
		try {
			byte[] keyBytes = Base64.decode(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicK = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(SignType);
			signature.initVerify(publicK);
			signature.update(data.getBytes());
			return signature.verify(Base64.decode(sign));
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException e) {
			return false;
		}
	}
}
