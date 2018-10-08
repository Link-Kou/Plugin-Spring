package com.plugin.javawidget.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by LK on 2016/10/13.
 */
public class RandomUtils {


	/**
	 * 字母数字大小写随机
	 * @param length
	 * @return
	 */
	public static String getMix(int length){
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
				"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		return get(chars,length,false);
	}

	/**
	 * 字母数字大小写随机
	 * @param length
	 * @return
	 */
	public static String getNumber(int length){
		String[] chars = new String[] {"0", "1", "2", "3", "4", "5","6", "7", "8", "9" };
		return get(chars,length,false);
	}

	/**
	 * 字母数字随机
	 * @param chars
	 * @param length
	 * @param lowercase
	 * @return
	 */
	public static String get(String[] chars,int length,boolean lowercase){
		StringBuilder shortBuffer = new StringBuilder();
		for (int i = 0; i < length; i++) {
			SecureRandom random = new SecureRandom();
			int number = random.nextInt(chars.length);
			shortBuffer.append(chars[number]);
		}
		return lowercase == true ? shortBuffer.toString().toLowerCase() : shortBuffer.toString();
	}

}
