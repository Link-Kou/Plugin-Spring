package com.plugin.javawidget.encryption;

/**
 * Created by LK on 2016/10/17.
 */
public class Base16Utils {

	/**
	 * 字符串转换成十六进制值
	 *
	 * @param bin String 我们看到的要转换成十六进制的字符串
	 * @return
	 */
	public static String encode(String bin) {
		return encode(bin.getBytes());
	}

	/**
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)
	 * 来转换成16进制字符串。
	 */
	public static String encode(byte[] inbuf) {
		StringBuffer strBuf = new StringBuffer();
		for (byte anInbuf : inbuf) {
			String byteStr = Integer.toHexString(anInbuf & 0xFF);
			if (byteStr.length() != 2) {
				strBuf.append('0').append(byteStr);
			} else {
				strBuf.append(byteStr);
			}

		}
		return new String(strBuf);
	}

	/**
	 * 十六进制转换字符串
	 *
	 * @param hex String 十六进制
	 * @return String 转换后的字符串
	 */
	public static String decoder(String hex) {
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte aB : b) {
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 转换byte到16进制
	 *
	 * @param b 要转换的byte
	 * @return 16进制格式
	 */
	private static String byteToHexString(byte b) {
		String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
