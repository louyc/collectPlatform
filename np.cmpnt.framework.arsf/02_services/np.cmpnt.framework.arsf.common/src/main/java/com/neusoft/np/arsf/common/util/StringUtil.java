package com.neusoft.np.arsf.common.util;

/**
 * 将指定的字符串旧的字符串替换为新的字符串 如果遇到html标记跳过标记（进行转义） 使用MD5算法进行加密的操作 将给定的字符串加密。base64
 * 将给定的字节数组加密。base64 base64解密算法 将HashMap转换为字符串，中间以逗号分割 将给定的byte数组转换为十六进制字符串
 * 将encodeHex方法转换的十六进制字符串转换为原始的byte数组 将字符串转换为byte 将给定的字符串变成字符数组，数组中的每一个元素都是一个单词
 * 返回制定长度的随机的字符或数字 从给定的字符串中获取前length个字符 过滤xml标签的内容 将给定字符中转义的xml标签正常的显示
 * 将不满length位的字符前面补0 将null的字符串转换为空串 字符进行编码转换 判断输入的字符串是否含有中文 将指定的字符按照给定的规则分成字符数组
 * 将给定的字符串数组按照给定的分隔符生成一个新字符串 将字符串中的引号、大于号、小于号转换成符合html xml标准的字符串 将字符中的双引号转换
 * 去除空格的方法 颠倒字符串 将Object对象转换为String对象 转换字符串格式：定长截取和填充 判断字符串是否有效，不为空或空串
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 项目名称: NetPatrol5.0 IT监管系统<br>
 * 模块名称: 工具类<br>
 * 功能描述: 处理字符串<br>
 * 创建日期: 2009-8-3 <br>
 * 版权信息: Copyright (c) 2009<br>
 * 公司信息: 东软集团股份有限公司 电信事业部研发二部<br>
 * 
 * @author <a href="mailto: luo-b@neusoft.com">骆宾</a>
 * @version v1.0
 * 
 *          <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2009-8-3      骆宾       创建
 * </pre>
 */
public class StringUtil {

	// Constants used by escapeHTMLTags
	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

	private static final char[] AMP_ENCODE = "&amp;".toCharArray();

	private static final char[] LT_ENCODE = "&lt;".toCharArray();

	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	private static final int fillchar = '=';

	private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789+/";

	private static Random randGen = new Random();

	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
			+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

	public static long lastTxId = System.currentTimeMillis();

	// add by qinshch
	public final static String m_strKey1 = "zxcvbnm,./asdfg";

	public final static String m_strKeya = "cjk;";

	public final static String m_strKey2 = "hjkl;'qwertyuiop";

	public final static String m_strKeyb = "cai2";

	public final static String m_strKey3 = "[]\\1234567890-";

	public final static String m_strKeyc = "%^@#";

	public final static String m_strKey4 = "=` ZXCVBNM<>?:LKJ";

	public final static String m_strKeyd = "*(N";

	public final static String m_strKey5 = "HGFDSAQWERTYUI";

	public final static String m_strKeye = "%^HJ";

	public final static String m_strKey6 = "OP{}|+_)(*&^%$#@!~";

	// end by qinshch

	/**
	 * 将指定的字符串替换为新的字符串
	 * 
	 * @param str
	 * @param oldStr
	 * @param newStr
	 * @return
	 */
	public static String replaceString(String str, String oldStr, String newStr) {

		if (str == null) {
			return null;
		}
		int pos = 0;
		String tmp = str;
		String reStr = "";
		while ((pos = tmp.indexOf(oldStr)) >= 0) {
			reStr = reStr + tmp.substring(0, pos) + newStr;
			tmp = tmp.substring(pos + oldStr.length(), tmp.length());
		}
		if (tmp.equals("")) {
			return reStr;
		}
		return reStr + tmp;
	}

	/**
	 * 
	 * 如果遇到html标记跳过标记（进行转义）
	 * 
	 * @param in
	 * @return String
	 */
	public static final String escapeHTMLTags(String in) {
		if (in == null) {
			return null;
		}
		char ch;
		int i = 0;
		int last = 0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3));
		for (; i < len; i++) {
			ch = input[i];
			if (ch > '>') {
				continue;
			} else if (ch == '<') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '>') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(GT_ENCODE);
			}
		}
		if (last == 0) {
			return in;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	/**
	 * 
	 * 使用MD5算法进行加密的操作
	 * 
	 * @param data
	 *            String类型
	 * @return String String类型
	 */
	public synchronized static final String hash(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5"); // 实例化加密类。
			digest.update(data.getBytes());
			return encodeHex(digest.digest()); // 返回一个十六进制的字符串。
		} catch (NoSuchAlgorithmException nsae) {
			System.err.println("Failed to load the MD5 MessageDigest. " + "Jive will be unable to function normally.");
			nsae.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 将HashMap转换为字符串，中间以逗号分割
	 * 
	 * @param map
	 *            HashMap类型
	 * @return String String类型
	 */
	public static final String mapToString(HashMap<?, ?> map) {
		StringBuffer buffer = new StringBuffer();
		Iterator<?> iter = map.keySet().iterator();
		map.values().iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next().toString()).append(",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}

	/**
	 * 
	 * 将给定的byte数组转换为十六进制字符串
	 * 
	 * @param bytes
	 *            数组
	 * @return String 十六进制字符串
	 */
	public static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;

		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * 
	 * 将encodeHex方法转换的十六进制字符串转换为原始的byte数组
	 * 
	 * @param hex
	 *            十六进制字符串
	 * @return bytes byte数组
	 */
	public static final byte[] decodeHex(String hex) {
		char[] chars = hex.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			byte newByte = 0x00;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = newByte;
			byteCount++;
		}
		return bytes;
	}

	/**
	 * 
	 * 将字符串转换为byte
	 * 
	 * @param ch
	 *            字符串
	 * @return byte类型
	 */
	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case '0':
			return 0x00;
		case '1':
			return 0x01;
		case '2':
			return 0x02;
		case '3':
			return 0x03;
		case '4':
			return 0x04;
		case '5':
			return 0x05;
		case '6':
			return 0x06;
		case '7':
			return 0x07;
		case '8':
			return 0x08;
		case '9':
			return 0x09;
		case 'a':
			return 0x0A;
		case 'b':
			return 0x0B;
		case 'c':
			return 0x0C;
		case 'd':
			return 0x0D;
		case 'e':
			return 0x0E;
		case 'f':
			return 0x0F;
		}
		return 0x00;
	}

	/**
	 * 
	 * 将给定的字符串加密。base64
	 * 
	 * @param data
	 *            给定的字符串
	 * @return String 加密后的字符串
	 */
	public static String encodeBase64(String data) {
		return encodeBase64(data.getBytes());
	}

	/**
	 * 
	 * 将给定的字节数组加密。base64
	 * 
	 * @param data
	 *            字节数组
	 * @return String 加密后的字符串
	 */
	public static String encodeBase64(byte[] data) {
		int c;
		int len = data.length;
		StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
		for (int i = 0; i < len; ++i) {
			c = (data[i] >> 2) & 0x3f;
			ret.append(cvt.charAt(c));
			c = (data[i] << 4) & 0x3f;
			if (++i < len) {
				c |= (data[i] >> 4) & 0x0f;

			}
			ret.append(cvt.charAt(c));
			if (i < len) {
				c = (data[i] << 2) & 0x3f;
				if (++i < len) {
					c |= (data[i] >> 6) & 0x03;

				}
				ret.append(cvt.charAt(c));
			} else {
				++i;
				ret.append((char) fillchar);
			}

			if (i < len) {
				c = data[i] & 0x3f;
				ret.append(cvt.charAt(c));
			} else {
				ret.append((char) fillchar);
			}
		}
		return ret.toString();
	}

	/**
	 * 
	 * base64解密算法
	 * 
	 * @param data
	 * @return String
	 */
	public static String decodeBase64(String data) {
		return decodeBase64(data.getBytes());
	}

	/**
	 * 
	 * base64解密算法
	 * 
	 * @param data
	 * @return String
	 */
	public static String decodeBase64(byte[] data) {
		int c, c1;
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; ++i) {
			c = cvt.indexOf(data[i]);
			++i;
			c1 = cvt.indexOf(data[i]);
			c = ((c << 2) | ((c1 >> 4) & 0x3));
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (fillchar == c) {
					break;
				}

				c = cvt.indexOf((char) c);
				c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
				ret.append((char) c1);
			}

			if (++i < len) {
				c1 = data[i];
				if (fillchar == c1) {
					break;
				}

				c1 = cvt.indexOf((char) c1);
				c = ((c << 6) & 0xc0) | c1;
				ret.append((char) c);
			}
		}
		return ret.toString();
	}

	/**
	 * 
	 * 将给定的字符串变成字符数组，数组中的每一个元素都是一个单词。例如:i,am,luobin 数组为：i am luobin
	 * 
	 * @param text
	 * @return String[]
	 */
	public static final String[] toLowerCaseWordArray(String text) {
		if (text == null || text.length() == 0) {
			return new String[0];
		}

		ArrayList<String> wordList = new ArrayList<String>();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = 0;

		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			String tmp = text.substring(start, end).trim(); // 每一个单词区分开来。
			// Remove characters that are not needed.
			tmp = replaceString(tmp, "+", "");
			tmp = replaceString(tmp, "/", "");
			tmp = replaceString(tmp, "\\", "");
			tmp = replaceString(tmp, "#", "");
			tmp = replaceString(tmp, "*", "");
			tmp = replaceString(tmp, ")", "");
			tmp = replaceString(tmp, "(", "");
			tmp = replaceString(tmp, "&", "");
			if (tmp.length() > 0) {
				wordList.add(tmp);
			}
		}
		return (String[]) wordList.toArray(new String[wordList.size()]);
	}

	/**
	 * 
	 * 返回制定长度的随机的字符或数字
	 * 
	 * @param length
	 * @return String
	 */
	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 
	 * 从给定的字符串中获取前length个字符
	 * 
	 * @param string
	 * @param length
	 * @return String
	 */
	public static final String chopAtWord(String string, int length) {
		if (string == null) {
			return string;
		}

		char[] charArray = string.toCharArray();
		int sLength = string.length();
		if (length < sLength) {
			sLength = length;
		}

		// First check if there is a newline character before length; if so,
		// chop word there.
		for (int i = 0; i < sLength - 1; i++) {
			// Windows
			if (charArray[i] == '\r' && charArray[i + 1] == '\n') {
				return string.substring(0, i + 1);
			}
			// Unix
			else if (charArray[i] == '\n') {
				return string.substring(0, i);
			}
		}
		// Also check boundary case of Unix newline
		if (charArray[sLength - 1] == '\n') {
			return string.substring(0, sLength - 1);
		}

		// Done checking for newline, now see if the total string is less than
		// the specified chop point.
		if (string.length() < length) {
			return string;
		}

		// No newline, so chop at the first whitespace.
		for (int i = length - 1; i > 0; i--) {
			if (charArray[i] == ' ') {
				return string.substring(0, i).trim();
			}
		}

		// Did not find word boundary so return original String chopped at
		// specified length.
		return string.substring(0, length);
	}

	/**
	 * 
	 * 过滤xml标签的内容
	 * 
	 * @param string
	 * @return String
	 */
	public static final String escapeForXML(String string) {
		if (string == null) {
			return null;
		}
		char ch;
		int i = 0;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3));
		for (; i < len; i++) {
			ch = input[i];
			if (ch > '>') {
				continue;
			} else if (ch == '<') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '&') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(AMP_ENCODE);
			} else if (ch == '"') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(QUOTE_ENCODE);
			}
		}
		if (last == 0) {
			return string;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	/**
	 * 
	 * 将给定字符中转义的xml标签正常的显示
	 * 
	 * @param string
	 * @return String
	 */
	public static final String unescapeFromXML(String string) {
		if (string == null) {
			return "";
		}
		string = replaceString(string, "&lt;", "<");
		string = replaceString(string, "&gt;", ">");
		string = replaceString(string, "&quot;", "\"");
		return replaceString(string, "&amp;", "&");
	}

	private static final char[] zeroArray = "0000000000000000".toCharArray();

	/**
	 * 
	 * 将不满length位的字符前面补0
	 * 
	 * @param string
	 * @param length
	 * @return String
	 */
	public static final String zeroPadString(String string, int length) {
		if (string == null || string.length() > length) {
			return string;
		}
		StringBuffer buf = new StringBuffer(length);
		buf.append(zeroArray, 0, length - string.length()).append(string);
		return buf.toString();
	}

	/**
	 * 
	 * 将null的字符串转换为空串
	 * 
	 * @param result
	 * @return String
	 */
	public static String changeNull(String result) {
		if (result == null) {
			result = "";
		}
		return result;
	}

	/**
	 * 
	 * 字符转换
	 * 
	 * @param str
	 *            String
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	public static String IsoToGB(String str) throws java.io.UnsupportedEncodingException {
		String strGB = new String(str.getBytes("ISO-8859-1"), "GBK");
		return strGB;
	}

	/**
	 * 字符转换
	 * 
	 * @param str
	 *            String
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	public static String GBToIso(String str) throws java.io.UnsupportedEncodingException {
		String strGB = new String(str.getBytes("GBK"), "ISO-8859-1");
		return strGB;
	}

	/**
	 * 字符转换
	 * 
	 * @param str
	 *            String
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	public static String GBToUTF8(String str) throws java.io.UnsupportedEncodingException {
		String strGB = new String(str.getBytes("GBK"), "UTF-8");
		return strGB;
	}

	/**
	 * 字符转换
	 * 
	 * @param str
	 *            String
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	public static String GBKToUTF8(String str) throws java.io.UnsupportedEncodingException {
		String strGB = new String(str.getBytes("GBK"), "UTF-8");
		return strGB;
	}

	/**
	 * 
	 * 判断输入的字符串是否含有中文
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean checkChinese(String str) {
		boolean flag = false; // 如果含有,则结果为true.否则为false
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] < 0) {
				flag = true;
				break; // 如果字符串里含有中文，那么返回true
			}
		}
		return flag;
	}

	/**
	 * 
	 * 将指定的字符按照给定的规则分成字符数组
	 * 
	 * @param inputValue
	 * @param delim
	 * @return String[]
	 */
	public static String[] getStringArray(String inputValue, String delim) {
		if (inputValue == null) {
			inputValue = "";
		}
		inputValue = inputValue.trim(); // very important
		java.util.StringTokenizer t = new java.util.StringTokenizer(inputValue, delim);
		String[] ret = new String[t.countTokens()];
		int index = 0;
		while (t.hasMoreTokens()) {
			String token = t.nextToken().trim();
			// check for valid value here if needed
			ret[index] = token;
			index++;
		}
		return ret;
	}

	/**
	 * 
	 * 将给定的字符串数组按照给定的分隔符生成一个字符串
	 * 
	 * @param inputValue
	 * @param delim
	 * @return
	 */
	public static String arrayToString(String[] inputValue, String delim) {
		if (inputValue == null || inputValue.length < 1) {
			return null;
		}
		StringBuffer result = new StringBuffer(inputValue[0]);
		for (int i = 1; i < inputValue.length; i++) {
			result.append(delim).append(inputValue[i]);
		}
		return result.toString();
	}

	/**
	 * 
	 * 将字符串中的引号、大于号、小于号转换成符合html xml标准的字符串
	 * 
	 * @param aS_src
	 *            要处理字符串
	 * @return 处理后的字符串
	 */
	public static String replaceQuotForXml(String aS_src) {
		String lS_ret = aS_src;
		lS_ret = lS_ret.replaceAll("<", "&lt;");
		lS_ret = lS_ret.replaceAll(">", "&gt;");
		lS_ret = lS_ret.replaceAll("\"", "&quot;");
		lS_ret = lS_ret.replaceAll("&", "&amp;");
		return lS_ret;
	}

	/**
	 * 
	 * 将字符中的双引号转换
	 * 
	 * @param aS_src
	 * @return
	 */
	public static String replaceQuot(String aS_src) {
		String lS_ret = aS_src;
		lS_ret = lS_ret.replaceAll("\"", "&quot;");
		return lS_ret;
	}

	/**
	 * 
	 * 去除空格的方法
	 * 
	 * @param source
	 * @return
	 */
	public static String trimString(String source) {
		if (source == null)
			return "";
		else if (source.indexOf("　") > -1) {
			return source.replaceAll("　", "").trim();
		} else {
			return source.trim();
		}
	}

	/**
	 * 
	 * 将Object对象转换为String对象
	 * 
	 * @param obj
	 * @return String
	 */
	public static String objectToString(Object obj) {
		String str_obj = null;
		if (obj instanceof byte[]) {
			str_obj = new String((byte[]) obj);
		} else if (obj instanceof String) { // 根据实际情况来生成返回字符串
			str_obj = (String) obj;
		} else {
			str_obj = (String) obj; // 暂时没有实现
		}
		return str_obj;
	}

	/**
	 * 
	 * 转换字符串格式：定长截取和填充
	 * 
	 * @param param
	 *            将要转换的字符串
	 * @param keepLength
	 *            保留字串的长度
	 * @param doMethod
	 *            0: 后补 1: 前补
	 * @param ins
	 *            填充的内容
	 * @return String
	 */
	public static String changeString(String param, int keepLength, int doMethod, String ins) {
		String outStr = param;
		if (param.length() < keepLength) {
			String temp = "";
			for (int i = 0; i < keepLength - param.length(); i++) {
				temp += ins;
			}

			if (doMethod == 0) {
				outStr = temp + outStr;
			} else {
				outStr = outStr + temp;
			}

		} else {
			outStr = param.substring(0, keepLength);
		}
		return outStr;
	}

	/**
	 * 
	 * 判断字符串是否有效，不为空或空串
	 * 
	 * @param s
	 *            String类型
	 * @return boolean
	 */
	public static boolean isValid(String s) {
		if (s == null || s.trim().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 判断是否为空对象:Null is true , Not null is false;
	 * 
	 * @param o
	 *            Object类型
	 * @return boolean
	 */
	public static boolean checkNull(Object o) {
		if (o == null || ((String) o).trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 颠倒字符串
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String reverse(String str) {
		int count = str.length();
		char[] value = new char[count];
		str.getChars(0, count, value, 0);
		int n = count - 1;
		for (int j = (n - 1) >> 1; j >= 0; --j) {
			char temp = value[j];
			value[j] = value[n - j];
			value[n - j] = temp;
		}
		return new String(value);
	}

	/**
	 * 密码加密
	 * 
	 * @param strPasswd
	 * @return
	 */
	public static String encodeCipher(String strPasswd) {
		int n;
		char code;
		if ((strPasswd == null) || strPasswd.length() == 0) {
			return "";
		}
		String strKey = StringUtil.m_strKey1 + StringUtil.m_strKey2 + StringUtil.m_strKey3 + StringUtil.m_strKey4
				+ StringUtil.m_strKey5 + StringUtil.m_strKey6;
		while (strPasswd.length() < 8) {
			strPasswd = strPasswd + (char) 1;
		}
		String des = "";
		for (n = 0; n <= strPasswd.length() - 1; n++) {
			while (true) {
				code = (char) Math.rint(Math.random() * 100);
				while ((code > 0) && (((code ^ strPasswd.charAt(n)) < 0) || ((code ^ strPasswd.charAt(n)) > 90))) {
					code = (char) ((int) code - 1);
				}
				char mid = 0;
				int flag;
				flag = code ^ strPasswd.charAt(n);
				if (flag > 93) {
					mid = 0;
				} else {
					mid = strKey.charAt(flag);
				}
				if ((code > 35) & (code < 122) & (code != 124) & (code != 39) & (code != 44) & (mid != 124)
						& (mid != 39) & (mid != 44)) {
					break;
				}
			}
			char temp = 0;
			temp = strKey.charAt(code ^ strPasswd.charAt(n));
			des = des + (char) code + temp;
		}
		return des;
	}

	/**
	 * 加密后密码解密生成密码
	 * 
	 * @param varCode
	 * @return
	 */
	public static String decodeCipher(String varCode) {
		int n;
		if ((varCode == null) || (varCode.length() == 0)) {
			return "";
		}
		String strKey = StringUtil.m_strKey1 + StringUtil.m_strKey2 + StringUtil.m_strKey3 + StringUtil.m_strKey4
				+ StringUtil.m_strKey5 + StringUtil.m_strKey6;
		if (varCode.length() % 2 != 0) {
			varCode = varCode + "?";
		}
		String des = "";
		for (n = 0; n <= varCode.length() / 2 - 1; n++) {
			char b;
			b = varCode.charAt(n * 2);
			int a;
			a = (int) strKey.indexOf(varCode.charAt(n * 2 + 1));
			des = des + (char) ((int) b ^ a);
		}
		n = des.indexOf(1);
		if (n > 0) {
			return des.substring(0, n);
		} else {
			return des;
		}
	}

	/**
	 * 单词中的大写字母转换为下滑线,如regionId -> region_id
	 * @param str
	 * @return
	 */
	public static String camel2Underline(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		Pattern p = Pattern.compile("[A-Z]");
		StringBuilder builder = new StringBuilder(str);
		Matcher mc = p.matcher(str);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
			i++;
		}
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();

	}

	/**
	 * 字符串中的大写字母转换为下滑线+小写字母,如region_id -> regionId
	 * @param str
	 * @return
	 */
	public static String underline2Camel(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(str);
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
			str = builder.toString();
		}
		Pattern p = Pattern.compile("_");
		Matcher mc = p.matcher(str);
		while (mc.find()) {
			if (mc.end() + 1 > str.length())
				break;
			builder.replace(mc.start() + 1, mc.end() + 1, builder.substring(mc.start() + 1, mc.end() + 1).toUpperCase());

		}
		return builder.toString().replaceAll("_", "");
	}

	/**
	 * json串转换成Map结构
	 * 
	 * @param varCode
	 * @return
	 */
	public static Map<?, ?> jsonStr2Map(String str) {
		return np.cmpnt.libs.commons.util.StringUtil.jsonStr2Map(str);
	}

	public static void main(String[] args) {
		System.out.println(underline2Camel("ip#trait_code#if_icmp#if_snmp#in_use#desc#seed_id####"));
	}

}
