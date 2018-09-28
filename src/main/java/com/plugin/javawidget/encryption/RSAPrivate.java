package com.plugin.javawidget.encryption;


import org.bouncycastle.util.encoders.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by LK on 2016/10/31.
 */
public class RSAPrivate {


    /**
     * <p>
     * 用私钥对加密消息解密
     * </p>
     * @param encryptedBase64Data 经过Base64编码的解密数据
     * @param privateKey 私有密钥
     * @return String
     * @exception null
     * **/
    public static String decrypt(String encryptedBase64Data,String privateKey){
        try {
            byte[] encryptedData = Base64.decode(encryptedBase64Data);
            byte[] keyBytes =Base64.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(encryptedData, offSet, 128);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            return  null;
        }
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 需要簽名的数据
     * @param privateKey 私钥(BASE64编码)
     * @param SignType 数字签名方式 {@link java.security.Signature}
     *
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey,String SignType) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SignType);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        byte[] signed = signature.sign();
        return Base64.toBase64String(signed).replace("\r\n","");
    }


}
