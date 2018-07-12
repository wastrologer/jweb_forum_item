package com.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供对字符串的常规处理逻辑 Created with IntelliJ IDEA. User: winsonwu Date: 14-3-17 Time:
 * 上午10:08 To change this template use File | Settings | File Templates.
 */
public abstract class StringUtil {

	public static final String EMPTY = "";

	/**
	 * 把String的首字母变成大写
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String changeThefirstCharToUppercase(String sourceStr) {
		if (null == sourceStr) {
			return null;
		} else {
			sourceStr = sourceStr.trim();
		}
		if (sourceStr.length() == 0) {
			return sourceStr;
		}
		if (sourceStr.length() == 1) {
			return sourceStr.toUpperCase();
		}
		StringBuilder sb = new StringBuilder();
		char[] charsOfStr = sourceStr.toCharArray();
		for (int i = 0; i < charsOfStr.length; i++) {
			char c = charsOfStr[i];
			if (i == 0) {
				if (c >= 'a' && c <= 'z') {
					c -= 32;
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static String changeJavaStyleToDbcloumStyle(String sourceStr) {
		if (null == sourceStr) {
			return null;
		} else {
			sourceStr = sourceStr.trim();
		}
		if (sourceStr.length() == 0) {
			return sourceStr;
		}
		StringBuilder sb = new StringBuilder();
		char[] charsOfStr = sourceStr.toCharArray();

		for (int i = 0; i < charsOfStr.length; i++) {
			char c = charsOfStr[i];
			if (i == 0) {
				if (c >= 'A' && c <= 'Z') {
					c += 32;
				}
				sb.append(c);
			} else {
				if (c >= 'A' && c <= 'Z') {
					c += 32;
					sb.append("_").append(c);
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	// public static String bSubstringAppendpoint(String s, int length) {
	// try {
	// String tmp = bSubstring(s, length);
	// if (!tmp.equals(s)) {
	// return tmp + "...";
	// }
	// return s;
	// } catch (Exception e) {
	// Log.run.warn("bSubstring catch an exception! ", e);
	// }
	//
	// return "";
	// }

	public static String getSimpleClassName(String longClassName) {
		if (!isEmpty(longClassName)) {
			int lastIndex = longClassName.lastIndexOf('.');
			if (lastIndex >= 0 && lastIndex < longClassName.length()) {
				return longClassName.substring(lastIndex + 1);
			}
		}
		return longClassName;
	}

	/**
	 * @Title: removeBlank
	 * @Description: 删除参数里面的空格
	 * @param str
	 * @return String 返回类型
	 * @throws
	 */
	public static String removeBlank(String str) {
		String ret = null;
		if (str != null) {
			ret = str.replaceAll(" ", "");
		} else {

		}
		return ret;
	}

	/**
	 * 将字符串转换成 long 型
	 * 
	 * @param s
	 *            字符串
	 * @return 正常情况返回 long 类型数据，如果出错返回 Long.MIN_VALUE，
	 */
	public static long toLong(String s) {
		return toLong(s, Long.MIN_VALUE);
	}

	/**
	 * 将字符串转换成 long 型，如果发生转换的异常那么返回默认值
	 * 
	 * @param s
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return 如果发生转换的异常那么返回默认值
	 */
	public static long toLong(String s, long defaultValue) {
		long ret = defaultValue;

		try {
			if (s != null)
				ret = Long.valueOf(s.trim());
		} catch (Exception e) {
		}

		return ret;
	}

	/**
	 * 将字符串转换成 int 型，如果发生转换的异常那么返回默认值
	 * 
	 * @param s
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return 如果发生转换的异常那么返回默认值
	 */
	public static int toInt(String s, int defaultValue) {
		int ret = defaultValue;

		try {
			ret = Integer.valueOf(s);
		} catch (Exception e) {
		}

		return ret;
	}

	/**
	 * 返回一个字符串在原字符串中的结束位置
	 * 
	 * @param oriString
	 *            参数说明 ：oriString 原字符串
	 * @param subString
	 *            参数说明 ：subString 子字符串
	 * @return 返回值：注释出失败、错误、异常时的返回情况
	 * @see
	 */
	public static int getSubStringBeginPos(String oriString, String subString) {
		if (oriString == null || subString == null || oriString.length() == 0) {
			return -1;
		}

		return oriString.indexOf(subString);
	}

	/**
	 * 返回一个字符串在原字符串中的结束位置
	 * 
	 * @param oriString
	 *            参数说明 ：oriString 原字符串
	 * @param subString
	 *            参数说明 ：subString 子字符串
	 * @return 返回值：注释出失败、错误、异常时的返回情况
	 */
	public static int getSubStringEndPos(String oriString, String subString) {
		if (oriString == null || subString == null || oriString.length() == 0 || oriString.indexOf(subString) == -1) {
			return -1;
		}

		return oriString.indexOf(subString) + subString.length();
	}

	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty(&quot;&quot;)        = true
	 * StringUtils.isEmpty(&quot; &quot;)       = false
	 * StringUtils.isEmpty(&quot;bob&quot;)     = false
	 * StringUtils.isEmpty(&quot;  bob  &quot;) = false
	 * </pre>
	 * <p/>
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * <p>
	 * Checks if a String is not empty ("") and not null.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isNotEmpty(null)      = false
	 * StringUtils.isNotEmpty(&quot;&quot;)        = false
	 * StringUtils.isNotEmpty(&quot; &quot;)       = true
	 * StringUtils.isNotEmpty(&quot;bob&quot;)     = true
	 * StringUtils.isNotEmpty(&quot;  bob  &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank(&quot;&quot;)        = true
	 * StringUtils.isBlank(&quot; &quot;)       = true
	 * StringUtils.isBlank(&quot;bob&quot;)     = false
	 * StringUtils.isBlank(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank(&quot;&quot;)        = false
	 * StringUtils.isNotBlank(&quot; &quot;)       = false
	 * StringUtils.isNotBlank(&quot;bob&quot;)     = true
	 * StringUtils.isNotBlank(&quot;  bob  &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and not
	 *         whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String,
	 * handling <code>null</code> by returning <code>null</code>.
	 * </p>
	 * <p/>
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and
	 * end characters &lt;= 32. To strip whitespace use strip(String).
	 * </p>
	 * <p/>
	 * <p>
	 * To trim your choice of characters, use the strip(String, String) methods.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.trim(null)          = null
	 * StringUtils.trim(&quot;&quot;)            = &quot;&quot;
	 * StringUtils.trim(&quot;     &quot;)       = &quot;&quot;
	 * StringUtils.trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtils.trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed string, <code>null</code> if null String input
	 */
	public static String trim(String str) {
		return (str == null ? null : str.trim());
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * </p>
	 * <p/>
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.equals(null, null)   = true
	 * StringUtils.equals(null, &quot;abc&quot;)  = false
	 * StringUtils.equals(&quot;abc&quot;, null)  = false
	 * StringUtils.equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * StringUtils.equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 * 
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 * @see String#equals(Object)
	 */
	public static boolean equals(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal
	 * ignoring the case.
	 * </p>
	 * <p/>
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered equal. Comparison is case insensitive.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.equalsIgnoreCase(null, null)   = true
	 * StringUtils.equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * StringUtils.equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * StringUtils.equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * StringUtils.equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 * 
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case insensitive, or
	 *         both <code>null</code>
	 * @see String#equalsIgnoreCase(String)
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equalsIgnoreCase(str2));
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *)         = -1
	 * StringUtils.indexOf(&quot;&quot;, *)           = -1
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, 'a') = 0
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, 'b') = 2
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return the first index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, char searchChar) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		return str.indexOf(searchChar);
	}

	/**
	 * <p>
	 * Finds the first index within a String from a start position, handling
	 * <code>null</code>. This method uses {@link String#indexOf(int, int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>. A
	 * negative start position is treated as zero. A start position greater than
	 * the string length returns <code>-1</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *, *)          = -1
	 * StringUtils.indexOf(&quot;&quot;, *, *)            = -1
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, 'b', 0)  = 2
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, 'b', 3)  = 5
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, 'b', 9)  = -1
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, 'b', -1) = 2
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, char searchChar, int startPos) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		return str.indexOf(searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(String)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *)          = -1
	 * StringUtils.indexOf(*, null)          = -1
	 * StringUtils.indexOf(&quot;&quot;, &quot;&quot;)           = 0
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;&quot;)   = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.indexOf(searchStr);
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(String, int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative start
	 * position is treated as zero. An empty ("") search String always matches.
	 * A start position greater than the string length only matches an empty
	 * search String.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *, *)          = -1
	 * StringUtils.indexOf(*, null, *)          = -1
	 * StringUtils.indexOf(&quot;&quot;, &quot;&quot;, 0)           = 0
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 0)  = 0
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 0)  = 2
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 0) = 1
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 3)  = 5
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 9)  = -1
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, -1) = 2
	 * StringUtils.indexOf(&quot;aabaabaa&quot;, &quot;&quot;, 2)   = 2
	 * StringUtils.indexOf(&quot;abc&quot;, &quot;&quot;, 9)        = 3
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		// JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
		if (searchStr.length() == 0 && startPos >= str.length()) {
			return str.length();
		}
		return str.indexOf(searchStr, startPos);
	}

	// LastIndexOf
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)         = -1
	 * StringUtils.lastIndexOf(&quot;&quot;, *)           = -1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'a') = 7
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'b') = 5
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return the last index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		return str.lastIndexOf(searchChar);
	}

	/**
	 * <p>
	 * Finds the last index within a String from a start position, handling
	 * <code>null</code>. This method uses {@link String#lastIndexOf(int, int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>. A
	 * negative start position returns <code>-1</code>. A start position greater
	 * than the string length searches the whole string.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf(&quot;&quot;, *,  *)           = -1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'b', 8)  = 5
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'b', 4)  = 2
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'b', 0)  = -1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'b', 9)  = 5
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'b', -1) = -1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, 'a', 0)  = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @param startPos
	 *            the start position
	 * @return the last index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar, int startPos) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		return str.lastIndexOf(searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(String)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)          = -1
	 * StringUtils.lastIndexOf(*, null)          = -1
	 * StringUtils.lastIndexOf(&quot;&quot;, &quot;&quot;)           = 0
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;&quot;)   = 8
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the last index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.lastIndexOf(searchStr);
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(String, int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>. A negative start
	 * position returns <code>-1</code>. An empty ("") search String always
	 * matches unless the start position is negative. A start position greater
	 * than the string length searches the whole string.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf(*, null, *)          = -1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 8)  = 7
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 8)  = 5
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 8) = 4
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 9)  = 5
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, -1) = -1
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 0)  = 0
	 * StringUtils.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 0)  = -1
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @param startPos
	 *            the start position, negative treated as zero
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.lastIndexOf(searchStr, startPos);
	}

	// Contains
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Checks if String contains a search character, handling <code>null</code>.
	 * This method uses {@link String#indexOf(int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>false</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.contains(null, *)    = false
	 * StringUtils.contains(&quot;&quot;, *)      = false
	 * StringUtils.contains(&quot;abc&quot;, 'a') = true
	 * StringUtils.contains(&quot;abc&quot;, 'z') = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return true if the String contains the search character, false if not or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, char searchChar) {
		if (str == null || str.length() == 0) {
			return false;
		}
		return (str.indexOf(searchChar) >= 0);
	}

	/**
	 * <p>
	 * Find the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(int)}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> String will return <code>false</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.contains(null, *)     = false
	 * StringUtils.contains(*, null)     = false
	 * StringUtils.contains(&quot;&quot;, &quot;&quot;)      = true
	 * StringUtils.contains(&quot;abc&quot;, &quot;&quot;)   = true
	 * StringUtils.contains(&quot;abc&quot;, &quot;a&quot;)  = true
	 * StringUtils.contains(&quot;abc&quot;, &quot;z&quot;)  = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return true if the String contains the search character, false if not or
	 *         <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return (str.indexOf(searchStr) >= 0);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * <p/>
	 * <p>
	 * A negative start position can be used to start <code>n</code> characters
	 * from the end of the String.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> String will return <code>null</code>. An empty ("")
	 * String will return "".
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.substring(null, *)   = null
	 * StringUtils.substring(&quot;&quot;, *)     = &quot;&quot;
	 * StringUtils.substring(&quot;abc&quot;, 0)  = &quot;abc&quot;
	 * StringUtils.substring(&quot;abc&quot;, 2)  = &quot;c&quot;
	 * StringUtils.substring(&quot;abc&quot;, 4)  = &quot;&quot;
	 * StringUtils.substring(&quot;abc&quot;, -2) = &quot;bc&quot;
	 * StringUtils.substring(&quot;abc&quot;, -4) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the substring from, may be null
	 * @param start
	 *            the position to start from, negative means count back from the
	 *            end of the String by this many characters
	 * @return substring from start position, <code>null</code> if null String
	 *         input
	 */
	public static String substring(String str, int start) {
		if (str == null) {
			return null;
		}

		// handle negatives, which means last n characters
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return EMPTY;
		}

		return str.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * <p/>
	 * <p>
	 * A negative start position can be used to start/end <code>n</code>
	 * characters from the end of the String.
	 * </p>
	 * <p/>
	 * <p>
	 * The returned substring starts with the character in the
	 * <code>start</code> position and ends before the <code>end</code>
	 * position. All postion counting is zero-based -- i.e., to start at the
	 * beginning of the string use <code>start = 0</code>. Negative start and
	 * end positions can be used to specify offsets relative to the end of the
	 * String.
	 * </p>
	 * <p/>
	 * <p>
	 * If <code>start</code> is not strictly to the left of <code>end</code>, ""
	 * is returned.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.substring(null, *, *)    = null
	 * StringUtils.substring(&quot;&quot;, * ,  *)    = &quot;&quot;;
	 * StringUtils.substring(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * StringUtils.substring(&quot;abc&quot;, 2, 0)   = &quot;&quot;
	 * StringUtils.substring(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * StringUtils.substring(&quot;abc&quot;, 4, 6)   = &quot;&quot;
	 * StringUtils.substring(&quot;abc&quot;, 2, 2)   = &quot;&quot;
	 * StringUtils.substring(&quot;abc&quot;, -2, -1) = &quot;b&quot;
	 * StringUtils.substring(&quot;abc&quot;, -4, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the substring from, may be null
	 * @param start
	 *            the position to start from, negative means count back from the
	 *            end of the String by this many characters
	 * @param end
	 *            the position to end at (exclusive), negative means count back
	 *            from the end of the String by this many characters
	 * @return substring from start position to end positon, <code>null</code>
	 *         if null String input
	 */
	public static String substring(String str, int start, int end) {
		if (str == null) {
			return null;
		}

		// handle negatives
		if (end < 0) {
			end = str.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		// check length next
		if (end > str.length()) {
			end = str.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	// Left/Right/Mid
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Gets the leftmost <code>len</code> characters of a String.
	 * </p>
	 * <p/>
	 * <p>
	 * If <code>len</code> characters are not available, or the String is
	 * <code>null</code>, the String will be returned without an exception. An
	 * exception is thrown if len is negative.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.left(null, *)    = null
	 * StringUtils.left(*, -ve)     = &quot;&quot;
	 * StringUtils.left(&quot;&quot;, *)      = &quot;&quot;
	 * StringUtils.left(&quot;abc&quot;, 0)   = &quot;&quot;
	 * StringUtils.left(&quot;abc&quot;, 2)   = &quot;ab&quot;
	 * StringUtils.left(&quot;abc&quot;, 4)   = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the leftmost characters from, may be null
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the leftmost characters, <code>null</code> if null String input
	 */
	public static String left(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}

		return str.substring(0, len);

	}

	/**
	 * <p>
	 * Gets the rightmost <code>len</code> characters of a String.
	 * </p>
	 * <p/>
	 * <p>
	 * If <code>len</code> characters are not available, or the String is
	 * <code>null</code>, the String will be returned without an an exception.
	 * An exception is thrown if len is negative.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.right(null, *)    = null
	 * StringUtils.right(*, -ve)     = &quot;&quot;
	 * StringUtils.right(&quot;&quot;, *)      = &quot;&quot;
	 * StringUtils.right(&quot;abc&quot;, 0)   = &quot;&quot;
	 * StringUtils.right(&quot;abc&quot;, 2)   = &quot;bc&quot;
	 * StringUtils.right(&quot;abc&quot;, 4)   = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the rightmost characters from, may be null
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the rightmost characters, <code>null</code> if null String input
	 */
	public static String right(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(str.length() - len);

	}

	/**
	 * <p>
	 * Gets <code>len</code> characters from the middle of a String.
	 * </p>
	 * <p/>
	 * <p>
	 * If <code>len</code> characters are not available, the remainder of the
	 * String will be returned without an exception. If the String is
	 * <code>null</code>, <code>null</code> will be returned. An exception is
	 * thrown if len is negative.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.mid(null, *, *)    = null
	 * StringUtils.mid(*, *, -ve)     = &quot;&quot;
	 * StringUtils.mid(&quot;&quot;, 0, *)      = &quot;&quot;
	 * StringUtils.mid(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * StringUtils.mid(&quot;abc&quot;, 0, 4)   = &quot;abc&quot;
	 * StringUtils.mid(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * StringUtils.mid(&quot;abc&quot;, 4, 2)   = &quot;&quot;
	 * StringUtils.mid(&quot;abc&quot;, -2, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the characters from, may be null
	 * @param pos
	 *            the position to start from, negative treated as zero
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the middle characters, <code>null</code> if null String input
	 */
	public static String mid(String str, int pos, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0 || pos > str.length()) {
			return EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (str.length() <= (pos + len)) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);

	}

	/**
	 * <p>
	 * Deletes all whitespaces from a String as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.deleteWhitespace(null)         = null
	 * StringUtils.deleteWhitespace(&quot;&quot;)           = &quot;&quot;
	 * StringUtils.deleteWhitespace(&quot;abc&quot;)        = &quot;abc&quot;
	 * StringUtils.deleteWhitespace(&quot;   ab  c  &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to delete whitespace from, may be null
	 * @return the String without whitespaces, <code>null</code> if null String
	 *         input
	 */
	public static String deleteWhitespace(String str) {
		if (str == null) {
			return null;
		}
		int sz = str.length();
		StringBuffer buffer = new StringBuffer(sz);
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				buffer.append(str.charAt(i));
			}
		}
		return buffer.toString();
	}

	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase()}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.upperCase(null)  = null
	 * StringUtils.upperCase(&quot;&quot;)    = &quot;&quot;
	 * StringUtils.upperCase(&quot;aBc&quot;) = &quot;ABC&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to upper case, may be null
	 * @return the upper cased String, <code>null</code> if null String input
	 */
	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * <p>
	 * Converts a String to lower case as per {@link String#toLowerCase()}.
	 * </p>
	 * <p/>
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.lowerCase(null)  = null
	 * StringUtils.lowerCase(&quot;&quot;)    = &quot;&quot;
	 * StringUtils.lowerCase(&quot;aBc&quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to lower case, may be null
	 * @return the lower cased String, <code>null</code> if null String input
	 */
	public static String lowerCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase();
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode letters.
	 * </p>
	 * <p/>
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String ("")
	 * will return <code>true</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isAlpha(null)   = false
	 * StringUtils.isAlpha(&quot;&quot;)     = true
	 * StringUtils.isAlpha(&quot;  &quot;)   = false
	 * StringUtils.isAlpha(&quot;abc&quot;)  = true
	 * StringUtils.isAlpha(&quot;ab2c&quot;) = false
	 * StringUtils.isAlpha(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains letters, and is non-null
	 */
	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetter(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断一个字符串是否是英文字母不包含中文
	 * 
	 * @param str
	 *            参数说明 ：每个参数一行，注明其取值范围等
	 * @return 返回值：注释出失败、错误、异常时的返回情况
	 */
	public static boolean isAlphaNotCHN(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		char ch;
		for (int i = 0; i < sz; i++) {
			ch = str.charAt(i);

			if (!isAlphaChar(ch)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断一个字符串是否是英文数字字母不包含中文
	 * 
	 * @param str
	 *            参数说明 ：每个参数一行，注明其取值范围等
	 * @return 返回值：注释出失败、错误、异常时的返回情况
	 */
	public static boolean isAlphaNumericNotCHN(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		char ch;
		for (int i = 0; i < sz; i++) {
			ch = str.charAt(i);

			if (!isAlphaNumericChar(ch)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAlphaChar(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}

	public static boolean isAlphaNumericChar(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode letters or digits.
	 * </p>
	 * <p/>
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String ("")
	 * will return <code>true</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isAlphanumeric(null)   = false
	 * StringUtils.isAlphanumeric(&quot;&quot;)     = true
	 * StringUtils.isAlphanumeric(&quot;  &quot;)   = false
	 * StringUtils.isAlphanumeric(&quot;abc&quot;)  = true
	 * StringUtils.isAlphanumeric(&quot;ab c&quot;) = false
	 * StringUtils.isAlphanumeric(&quot;ab2c&quot;) = true
	 * StringUtils.isAlphanumeric(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains letters or digits, and is
	 *         non-null
	 */
	public static boolean isAlphanumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetterOrDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only unicode digits. A decimal point is not
	 * a unicode digit and returns false.
	 * </p>
	 * <p/>
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String ("")
	 * will return <code>true</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isNumeric(null)   = false
	 * StringUtils.isNumeric(&quot;&quot;)     = true
	 * StringUtils.isNumeric(&quot;  &quot;)   = false
	 * StringUtils.isNumeric(&quot;123&quot;)  = true
	 * StringUtils.isNumeric(&quot;12 3&quot;) = false
	 * StringUtils.isNumeric(&quot;ab2c&quot;) = false
	 * StringUtils.isNumeric(&quot;12-3&quot;) = false
	 * StringUtils.isNumeric(&quot;12.3&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains digits, and is non-null
	 */
	public static boolean isNumeric(String str) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the String contains only whitespace.
	 * </p>
	 * <p/>
	 * <p>
	 * <code>null</code> will return <code>false</code>. An empty String ("")
	 * will return <code>true</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.isWhitespace(null)   = false
	 * StringUtils.isWhitespace(&quot;&quot;)     = true
	 * StringUtils.isWhitespace(&quot;  &quot;)   = true
	 * StringUtils.isWhitespace(&quot;abc&quot;)  = false
	 * StringUtils.isWhitespace(&quot;ab2c&quot;) = false
	 * StringUtils.isWhitespace(&quot;ab-c&quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if only contains whitespace, and is non-null
	 * @since 2.0
	 */
	public static boolean isWhitespace(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Capitalizes a String changing the first letter to title case as per
	 * {@link Character#toTitleCase(char)}. No other letters are changed.
	 * </p>
	 * <p/>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.capitalize(null)  = null
	 * StringUtils.capitalize("")    = ""
	 * StringUtils.capitalize("cat") = "Cat"
	 * StringUtils.capitalize("cAt") = "CAt"
	 * </pre>
	 * 
	 * @param str
	 *            the String to capitalize, may be null
	 * @return the capitalized String, <code>null</code> if null String input
	 *         WordUtils#capitalize(String)
	 * @see #uncapitalize(String)
	 * @since 2.0
	 */
	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	/**
	 * <p>
	 * Uncapitalizes a String changing the first letter to title case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 * </p>
	 * <p/>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.uncapitalize(null)  = null
	 * StringUtils.uncapitalize("")    = ""
	 * StringUtils.uncapitalize("Cat") = "cat"
	 * StringUtils.uncapitalize("CAT") = "cAT"
	 * </pre>
	 * 
	 * @param str
	 *            the String to uncapitalize, may be null
	 * @return the uncapitalized String, <code>null</code> if null String input
	 * @see #capitalize(String)
	 * @since 2.0
	 */
	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * 删除无用的xml
	 * 
	 * @param str
	 * @return
	 */
	public static String removeInvalidWML(String str) {
		if (str == null)
			return null;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if ((c >= 0) && (c <= '\31'))
				continue;
			if ((c >= 57344) && (c <= 63743))
				continue;
			if ((c >= 65520) && (c <= 65535))
				continue;
			switch (c) {
			case '$':
			case 'ÿ':
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\t':
				sb.append("  ");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '^':
			case '`':
				break;
			default:
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String[] split(String line, String seperator) {
		if ((line == null) || (seperator == null) || (seperator.length() == 0))
			return null;
		List<String> list = new ArrayList<String>();
		int pos1 = 0;
		while (true) {
			int pos2 = line.indexOf(seperator, pos1);
			if (pos2 < 0) {
				list.add(line.substring(pos1));
				break;
			}
			list.add(line.substring(pos1, pos2));
			pos1 = pos2 + seperator.length();
		}

		for (int i = list.size() - 1; i >= 0; --i) {

			if(((String) list.get(i)).length() == 0)
				list.remove(i);
		}
		return ((String[]) list.toArray(new String[0]));
	}

/*	public static String[] splitWithoutZreoLength(String line, String seperator) {
		String[] strs=split(line, seperator);
		List<String> list=new ArrayList<>();
		for(String s:strs){
		}
	}*/

	public static String Longs2String(long[] longs) {
		StringBuffer sbf = new StringBuffer();
		if (longs != null && longs.length > 0) {
			sbf.append("[");
			for (int i = 0; i < longs.length; i++) {
				sbf.append(longs[i]);
				if (i < longs.length - 1) {
					sbf.append(", ");
				}
			}
			sbf.append("]");
		}
		return sbf.toString();
	}

	public static String genParameterStr(Object... objs) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("(");
		for (int i = 0; i < objs.length; i += 2) {
			sbf.append(objs[i].toString());
			sbf.append("=");
			if (i + 1 < objs.length) {
				if (objs[i + 1] == null) {
					sbf.append("null");
				} else {
					sbf.append(objs[i + 1].toString());
				}
			}
			if (i + 2 < objs.length) {
				sbf.append(",");
			}
		}
		sbf.append(")");
		return sbf.toString();
	}

	/**
	 * @author:jiaqiangxu
	 * @date:2012-3-8
	 * @desc:按指定字节长度截取字符串，如果结果字符串最后一个字符是中文且不够，则略去此字符
	 * @param:s 需要截取的字符串 ;length 截取的字节长度，(中文和全角字符占2字节)
	 * @return:截取后悔的字符串
	 */
	public static String bSubstring(String s, int length) throws Exception {
		if (s == null || s.length() == 0) {
			return "";
		}
		byte[] bytes = s.getBytes("Unicode");
		int n = 0; // 表示当前的字节数
		int i = 2; // 要截取的字节数，从第3个字节开始
		for (; i < bytes.length && n < length; i++) {
			// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
			if (i % 2 == 1) {
				n++; // 在UCS2第二个字节时n加1

			} else {
				// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
				if (bytes[i] != 0) {
					n++;
				}
			}
		}

		// 如果i为奇数时，处理成偶数
		if (i % 2 == 1) {
			// 该UCS2字符是汉字时，去掉这个截一半的汉字
			if (bytes[i - 1] != 0)
				i = i - 1;
			// 该UCS2字符是字母或数字，则保留该字符
			else
				i = i + 1;
		}
		return new String(bytes, 0, i, "Unicode");
	}

	// Joining
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * 连接数组对象的元素为字符串
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.join(null)            = null
	 * StringUtils.join([])              = ""
	 * StringUtils.join([null])          = ""
	 * StringUtils.join(["a", "b", "c"]) = "abc"
	 * StringUtils.join([null, "", "a"]) = "a"
	 * </pre>
	 * 
	 * @param array
	 *            数组对象
	 * @return 串接后的字符串，如果数组对象为 NULL 则返回 NULL
	 */
	public static String join(Object[] array) {
		return join(array, null);
	}

	/**
	 * <p>
	 * 以指定的分隔符串接数组对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.join(null, *)               = null
	 * StringUtils.join([], *)                 = ""
	 * StringUtils.join([null], *)             = ""
	 * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.join(["a", "b", "c"], null) = "abc"
	 * StringUtils.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array
	 *            数组对象
	 * @param separator
	 *            分隔符
	 * @return 串接后的字符串，如果数组对象为 NULL 则返回 NULL
	 */
	public static String join(Object[] array, char separator) {
		if (array == null) {
			return null;
		}

		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * 以指定的分隔符串接数组对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.join(null, *)               = null
	 * StringUtils.join([], *)                 = ""
	 * StringUtils.join([null], *)             = ""
	 * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.join(["a", "b", "c"], null) = "abc"
	 * StringUtils.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array
	 *            数组对象
	 * @param separator
	 *            分隔符
	 * @param startIndex
	 *            数组开始下标
	 * @param endIndex
	 *            数组结束下标
	 * @return 串接后的字符串，如果数组对象为 NULL 则返回 NULL
	 */
	public static String join(Object[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
		StringBuilder buf = new StringBuilder(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * 以指定的分隔符串接数组对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.join(null, *)                = null
	 * StringUtils.join([], *)                  = ""
	 * StringUtils.join([null], *)              = ""
	 * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
	 * StringUtils.join(["a", "b", "c"], null)  = "abc"
	 * StringUtils.join(["a", "b", "c"], "")    = "abc"
	 * StringUtils.join([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 * 
	 * @param array
	 *            数组对象
	 * @param separator
	 *            分隔符
	 * @return 串接后的字符串，如果数组对象为 NULL 则返回 NULL
	 */
	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>
	 * 以指定的分隔符串接数组对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.join(null, *)                = null
	 * StringUtils.join([], *)                  = ""
	 * StringUtils.join([null], *)              = ""
	 * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
	 * StringUtils.join(["a", "b", "c"], null)  = "abc"
	 * StringUtils.join(["a", "b", "c"], "")    = "abc"
	 * StringUtils.join([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 * 
	 * @param array
	 *            数组对象
	 * @param separator
	 *            分隔符
	 * @param startIndex
	 *            数组开始下标
	 * @param endIndex
	 *            数组结束下标
	 * @return 串接后的字符串，如果数组对象为 NULL 则返回 NULL
	 */
	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

		StringBuilder buf = new StringBuilder(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * 以指定的分隔符串接迭代对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * 
	 * @param iterator
	 *            迭代器
	 * @param separator
	 *            分隔符
	 * @return 串接后的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String join(Iterator iterator, char separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return objToString(first);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16,
		// probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			buf.append(separator);
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * 以指定的分隔符串接迭代对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * 
	 * @param iterator
	 *            迭代器
	 * @param separator
	 *            分隔符
	 * @return 串接后的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String join(Iterator iterator, String separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return objToString(first);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16,
		// probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * 以指定的分隔符串接集合对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * 
	 * @param collection
	 *            集合
	 * @param separator
	 *            分隔符
	 * @return 串接后的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String join(Collection collection, char separator) {
		if (collection == null) {
			return null;
		}
		return join(collection.iterator(), separator);
	}

	/**
	 * <p>
	 * 以指定的分隔符串接集合对象
	 * </p>
	 * <p/>
	 * <p>
	 * 生成的字符串头尾不会添加分隔符
	 * </p>
	 * 
	 * @param collection
	 *            集合
	 * @param separator
	 *            分隔符
	 * @return 串接后的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String join(Collection collection, String separator) {
		if (collection == null) {
			return null;
		}
		return join(collection.iterator(), separator);
	}

	/**
	 * 对象转化成字符串，处理 null 对象
	 * 
	 * @param o
	 *            对象
	 * @return 字符串
	 */
	private static String objToString(Object o) {
		return null == o ? "" : o.toString();
	}

	static Set<String> specialWords = new HashSet<String>() {
		private static final long serialVersionUID = -5015742853025220605L;

		{
			add("\\?");
			add("\\*");
			add("\\.");
			add("\\+");
			add("\\)");
			add("\\(");
			add("\\$");
			add("\\{");
			add("\\}");
			add("\\|");
			add("\\^");
			add("\\\\");
			add("@");
			add("&");
			add("&amp;");
			add("#");
			add("!");
			add("（");
			add("）");
			add("｛");
			add("｝");
			add("＋");
			add("．");
			add("＊");
			add("？");
			add("＄");
			add("｜");
			add("＆");
			add("＃");
			add("！");
			add("'");
			add("\"");
		}
	};

	/**
	 * 过滤搜索相关的敏感字符
	 * 
	 * @param keyword
	 *            关键词
	 * @return 过滤后的字符串
	 */
	public static String escapeForSearch(String keyword) {
		if (isBlank(keyword)) {
			return keyword;
		}
		for (String specialWord : specialWords) {
			keyword = keyword.replaceAll(specialWord, "");
		}
		return keyword.trim();
	}

	public static long[] toLongArray(Set<Long> ls) {
		if (ls != null && ls.size() > 0) {
			long[] ret = new long[ls.size()];
			Long[] longArray = ls.toArray(new Long[] {});
			for (int i = 0; i < longArray.length; i++) {
				ret[i] = longArray[i];
			}
			return ret;
		}
		return new long[] {};
	}

	// Defaults
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * 判断字符串如果为空，则返回默认的字符串，否则返回原字符串
	 * </p>
	 * <p/>
	 * 
	 * <pre>
	 * StringUtils.defaultIfEmpty(null, "NULL")  = "NULL"
	 * StringUtils.defaultIfEmpty("", "NULL")    = "NULL"
	 * StringUtils.defaultIfEmpty("bat", "NULL") = "bat"
	 * </pre>
	 * 
	 * @param str
	 *            待校验字符串
	 * @param defaultStr
	 *            默认字符串
	 * @return 原字符串或者默认字符串
	 */
	public static String defaultIfEmpty(String str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	public static String genParameterStringNoEmpty(Object... objs) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < objs.length; i += 2) {
			if (i + 1 < objs.length) {
				if (objs[i + 1] == null) {
					continue;
				}
				if ((objs[i + 1] instanceof String) && StringUtil.isEmpty((String) objs[i + 1])) {
					continue;
				}
				sbf.append(objs[i].toString());
				sbf.append("=");
				sbf.append(objs[i + 1].toString());
				if (i + 3 < objs.length) {
					sbf.append(",");
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 校验是否为double
	 * 
	 * @param str
	 * @return
	 * @date:2013-10-30
	 * @author:<a href="mailto:jiaqiangxu@tencent.com">JiaqiangXu</a>
	 * @version 1.0
	 */
	public static boolean isValidDouble(String str) {
		try {
			if (isEmpty(str)) {
				return false;
			}
			Double longitude = Double.parseDouble(str);
			if (longitude instanceof Double == false) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static String getEncode(String key) {
		try {
			return URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return key;
		}
	}

	// 删除 ￥199.00中的".00" 变成￥199
	public static String removePriceZZ(String str) {
		if (!StringUtil.isEmpty(str)) {
			String _doc = str.replaceAll("\\.00", "");
			return _doc;
		}
		return str;
	}

	public static String priceDispFormater(long score) {
		String price = String.format("%.2f", score / 100.0);
		return removePriceZZ(price);
	}

	public static String maskStr(String srcStr, int outLen) {
		String destStr = srcStr;
		try {
			destStr = StringUtil.bSubstring(destStr, outLen);// 截取8个汉字长度
			if (StringUtil.isNotEmpty(destStr) && !destStr.equals(srcStr)) {
				destStr = destStr + "...";
			}
		} catch (Exception e) {
			// do nothing
		}

		return destStr;
	}

	/**
	 * utf-8 urldecode
	 * 
	 * @param original
	 * @return
	 */
	public static String urlDecodeQuitely(String original) {
		try {
			if (original != null && original.contains("%")) {
				return URLDecoder.decode(original, "utf-8");
			}
		} catch (Exception e) {
			// ignore
		}
		return original;
	}

	/**
	 * 获取编码类型
	 * 
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	/**
	 * 转码
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeStr(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取文本中的url
	 * 
	 * @param original
	 * @return
	 */
	public static String StringUrlMatch(String original) {
		String result = null;
		try {
			Pattern p = Pattern
					.compile(
							"(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
							Pattern.CASE_INSENSITIVE);

			Matcher m = p.matcher(original);
			if (m.find()) {
				result = m.group();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取文本中的url
	 * 
	 * @param original
	 * @return
	 */
	public static String StringUrlMatchLast(String original) {
		String result = null;
		try {
			Pattern p = Pattern
					.compile(
							"((http|www|ftp|)?(://)?[-\\w]+(\\.\\w[-\\w]*)+|(?i:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+(?-i:com\\b|edu\\b|biz\\b|gov\\b|in(?:t|fo)\\b|mil\\b|net\\b|org\\b|[a-z][a-z]\\b))(:\\d+)?(/[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]*(?:[.!,?]+[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]+)*)?",
							// "(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
							// "(http[s]{0,1}|ftp)?(://)?[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&amp;*+?:_/=<>]*)?",
							Pattern.CASE_INSENSITIVE);

			Matcher m = p.matcher(original);
			while (m.find()) {
				result = m.group();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取文本中的url
	 * 
	 * @param original
	 * @return
	 */
	public static List<String> StringListUrlMatch(String original) {
		List<String> result = new ArrayList<String>();
		try {
			Pattern p = Pattern
					.compile(
							"((http|www|ftp|)?(://)?[-\\w]+(\\.\\w[-\\w]*)+|(?i:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+(?-i:com\\b|edu\\b|biz\\b|gov\\b|in(?:t|fo)\\b|mil\\b|net\\b|org\\b|[a-z][a-z]\\b))(:\\d+)?(/[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]*(?:[.!,?]+[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]+)*)?",
							// "(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
							// "(http[s]{0,1}|ftp)?(://)?[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&amp;*+?:_/=<>]*)?",
							Pattern.CASE_INSENSITIVE);

			Matcher m = p.matcher(original);
			while (m.find()) {
				result.add(m.group());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getHtmlStrTitle(String s) {
		String regex;
		String title = "";
		List<String> list = new ArrayList<String>();
		regex = "<title>.*?</title>";
		Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (int i = 0; i < list.size(); i++) {
			title = title + list.get(i);
		}
		return outTag(title);
	}

	/**
	 * 判断字符是否是中文
	 * 
	 * @param c
	 *            字符
	 * @return 是否是中文
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否是乱码
	 * 
	 * @param strName
	 *            字符串
	 * @return 是否是乱码
	 */
	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|t*|r*|n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}

	}

	/** */
	/**
	 * 
	 * @param s
	 * @return 去掉标记
	 */
	public static String outTag(String s) {
		return s.replaceAll("<.*?>", "");
	}
	
	
	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNaturalNumber (String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
			isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
		}
		return true;
	}
	public static String getRandomString(int length){
//产生随机数
		Random random=new Random();
		StringBuffer sb=new StringBuffer();
//循环length次
		for(int i=0; i<length; i++){
//产生0-2个随机数，既与a-z，A-Z，0-9三种可能
			int number=random.nextInt(3);
			long result=0;
			switch(number){
//如果number产生的是数字0；
				case 0:
//产生A-Z的ASCII码
					result=Math.round(Math.random()*25+65);
//将ASCII码转换成字符
					sb.append(String.valueOf((char)result));
					break;
				case 1:
//产生a-z的ASCII码
					result=Math.round(Math.random()*25+97);
					sb.append(String.valueOf((char)result));
					break;
				case 2:
//产生0-9的数字
					sb.append(String.valueOf(new Random().nextInt(10)));
					break;
			}
		}
		return sb.toString();
	}
	public static String concealPhoneNumber(String phone){
		if(phone.length()!=11){
			return null;
		}

		String start=phone.substring(0,3);
		String end=phone.substring(7);
		return start+"****"+end;
	}

	public static void main(String[] args) {
		System.out.println(Double.parseDouble("001.11"));
	}
}
