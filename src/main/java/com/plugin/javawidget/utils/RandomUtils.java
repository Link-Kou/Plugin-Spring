package com.plugin.javawidget.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by LK on 2016/10/13.
 */
public class RandomUtils {

	public static String get(int length) {
		String baseStr = "abcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(baseStr.length());
			sb.append(baseStr.charAt(number));
		}
		return sb.toString();
	}

	public static String number(int length){
		if(length > 32){
			length = 32;
		}
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
				"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		StringBuilder shortBuffer = new StringBuilder();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < length; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

}
