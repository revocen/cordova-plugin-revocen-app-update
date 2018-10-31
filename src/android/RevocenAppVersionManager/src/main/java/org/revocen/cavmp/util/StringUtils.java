package org.revocen.cavmp.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {

		if (isBlank(string)) {
			return "";
		}

		StringBuffer unicode = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {

			// 取出每一个字符
			char c = string.charAt(i);

			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}

		return unicode.toString();
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {

		StringBuffer string = new StringBuffer();

		String[] hex = unicode.split("\\\\u");

		for (int i = 1; i < hex.length; i++) {

			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);

			// 追加成string
			string.append((char) data);
		}

		return string.toString();
	}

	/**
	 * 字符串转ISO-8859-1编码
	 * 
	 * @param str
	 * @return
	 */
	public static String toUTF_8(String str) {
		try {
			return new String(str.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 比较两个字符串长度
	 * 
	 * @param str1
	 * @param str2
	 * @return str1.lenght() > str2.lenght() return true
	 */
	public static Boolean compareLength(String str1, String str2) {
		if (str1.length() > str2.length()) {
			return true;
		}
		return false;
	}

	public static String captureName(String name) {
		if (!isBlank(name)) {
			char[] cs = name.toCharArray();
			cs[0] -= 32;
			return String.valueOf(cs);
		}

		return name;
	}

	/**
	 * 首字母转大写
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String transFirstToUpper(String str) {
		if (!isBlank(str)) {
			char[] cs = str.toCharArray();
			cs[0] -= 32;
			return String.valueOf(cs);
		}

		return str;
	}

	/**
	 * 首字母转小写
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String transFirstToLower(String str) {
		if (!isBlank(str)) {
			char[] cs = str.toCharArray();
			cs[0] += 32;
			return String.valueOf(cs);
		}

		return str;
	}

	public static String checkPhone(String phoneNo) {
		if (StringUtils.isBlank(phoneNo)) {
			return "请填写手机号";
		}

		if (phoneNo.length() != 11) {
			return "请您输入11位的手机号码";
		}

		if (!isMobileNum2(phoneNo)) {
			return "您输入的不是手机号码";
		}

		return "";
	}

	public static boolean isMobileNum(String paramString) {
		return Pattern.compile("(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])").matcher(paramString).matches();
	}

	public static boolean isMobileNum2(String paramString) {
		return Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(145)|(147))\\d{8}$").matcher(paramString).matches();
	}

	public static Boolean isBlank(String parameter) {
		if (parameter == null || parameter.length() <= 0) {
			return true;
		}

		return false;
	}

	public static Boolean isNotBlank(String target) {
		if (target != null && target.length() > 0) {
			return true;
		}

		return false;
	}

	public static Boolean isAllBlank(String... parameter) {
		for (String str : parameter) {
			if (!isBlank(str))
				return false;
		}

		return true;
	}

	public static Boolean isAllNotBlank(String... parameter) {
		for (String str : parameter) {
			if (!isNotBlank(str))
				return false;
		}

		return true;
	}

	public static String getString(String str) {
		return isBlank(str) ? "" : str;
	}

	/**
	 * 字节数组转十六进制字符串
	 * 
	 * @param b 字节数组
	 * @return 十六进制字符串
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				hs = hs.append("0").append(stmp);
			} else {
				hs = hs.append(stmp);
			}
		}
		return String.valueOf(hs);
	}

	/**
	 * 将字符串转换为ASCII码,每个字符的ASCII码之间用空格隔开
	 * 
	 * @param str
	 * @return
	 */
	public String toASCII(String str) {

		char[] chars = str.toCharArray();
		String ret = "";

		for (int i = 0; i < chars.length; i++) {

			if (i == chars.length - 1) {

				ret += (int) chars[i] + "";

			} else {
				ret += (int) chars[i] + " ";
			}
		}

		return ret;
	}

	public static String hintString(String val, Integer showLength) {
		return val.substring(val.length() - showLength, val.length());
	}

	public static String hintPhoneNo(String val, Integer showLength) {
		return "XXX" + hintString(val, showLength);
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static String floor(String str) {
		if (isBlank(str)) {
			return "";
		}
		if (!isNum(str)) {
			return str;
		}
		Double dbl = Double.valueOf(str);
		int number = (int) Math.floor(dbl);
		return String.valueOf(number);
	}

	public static String round(String str, int length) {
		if (isBlank(str)) {
			return "";
		}
		if (!isNum(str)) {
			return str;
		}
		BigDecimal b = new BigDecimal(str);
		return b.setScale(length, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static String getChineseUpperNumber(String input) {
		if (isBlank(input)) {
			return "";
		}

		return "";
	}

	public static boolean checkNameChese(String name) {
		boolean res = true;
		char[] cTemp = name.toCharArray();
		for (int i = 0; i < name.length(); i++) {
			if (!isChinese(cTemp[i])) {
				res = false;
				break;
			}
		}
		return res;
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
}
