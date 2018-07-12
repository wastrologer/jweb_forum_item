package com.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

public class Base64Util {

	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

	private static byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++) {
			codes[i] = -1;
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			codes[i] = (byte) (i - 'A');
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}

		for (int i = 'a'; i <= 'z'; i++) {
			codes[i] = (byte) (26 + i - 'a');
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}
		for (int i = '0'; i <= '9'; i++) {
			codes[i] = (byte) (52 + i - '0');
			// LoggerUtil.debug(i + "&" + codes[i] + " ");
		}
		codes['+'] = 62;
		codes['/'] = 63;
	}

	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 功能：编码byte[]
	 * 
	 * @author 宋立君
	 * @date 2014年07月03日
	 * @param data
	 *            源
	 * @return char[]
	 */
	public static char[] encode(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;

			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * 功能：解码
	 * 
	 * @author 宋立君
	 * @date 2014年07月03日
	 * @param data
	 *            编码后的字符数组
	 * @return byte[]
	 */
	public static byte[] decode(char[] data) {

		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || codes[data[ix]] < 0) {
				--tempLen; // ignore non-valid chars and padding
			}
		}
		// calculate required length:
		// -- 3 bytes for every 4 valid base64 chars
		// -- plus 2 bytes if there are 3 extra base64 chars,
		// or plus 1 byte if there are 2 extra.

		int len = (tempLen / 4) * 3;
		if ((tempLen % 4) == 3) {
			len += 2;
		}
		if ((tempLen % 4) == 2) {
			len += 1;

		}
		byte[] out = new byte[len];

		int shift = 0; // # of excess bits stored in accum
		int accum = 0; // excess bits
		int index = 0;

		// we now go through the entire array (NOT using the 'tempLen' value)
		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : codes[data[ix]];

			if (value >= 0) { // skip over non-code
				accum <<= 6; // bits shift up by 6 each time thru
				shift += 6; // loop, with new bits being put in
				accum |= value; // at the bottom.
				if (shift >= 8) { // whenever there are 8 or more shifted in,
					shift -= 8; // write them out (from the top, leaving any
					out[index++] = // excess at the bottom for next iteration.
					(byte) ((accum >> shift) & 0xff);
				}
			}
		}

		// if there is STILL something wrong we just have to throw up now!
		if (index != out.length) {
			throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
		}

		return out;
	}

}