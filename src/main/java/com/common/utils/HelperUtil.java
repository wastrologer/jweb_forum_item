package com.common.utils;

import com.common.entity.CommonEnum;
import com.common.entity.Constants;

import java.util.Date;
import java.util.UUID;

public class HelperUtil {

	// 当前时间戳除以1000
	public static Long getSecondStamp() {
		return new Date().getTime() / 1000;
	}

	// 根据MaterialId得到素材类型
	public static Byte getMaterialType(String materialId) {
		String materialPostfix = materialId.substring(materialId.length() - 3).toLowerCase();
		Byte materialType = CommonEnum.MaterialType.OTHER.value;

		if (Constants.POSTFIX_IMG.indexOf(materialPostfix) != -1) {
			materialType = CommonEnum.MaterialType.PIC.value;
		} else if (Constants.POSTFIX_AUDIO.indexOf(materialPostfix) != -1) {
			materialType = CommonEnum.MaterialType.VOICE.value;
		} else if (Constants.POSTFIX_VIDEO.indexOf(materialPostfix) != -1) {
			materialType = CommonEnum.MaterialType.VIEDO.value;
		}
		return materialType;
	}

	public static String[] charsSample = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static String[] digitSample = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public static String[] alphabetSample = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
			"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(alphabetSample[x % 0x1A]);
		}
		return shortBuffer.toString();
	}

	public static String formatConstntsString(String template, String[] replaceStrs, String target) {
		try {
			for (int i = 0; i < replaceStrs.length; i++) {
				template = template.replace(target + (i + 1), replaceStrs[i]);
			}
		} catch (Exception e) {
		}
		return template;
	}
	
	public static synchronized String genInviteCode() {
		String str = "";
		str += (int) (Math.random() * 9 + 1);
		for (int i = 0; i < 5; i++) {
			str += (int) (Math.random() * 10);
		}
		return str;
//        return String.valueOf(getRandNum(1,999999));
    }
	
	/**
	 * 获取10位的消费验证码
	 * @return
	 */
	public static String getConsumeCode() {
		String str = "";
		str += (int) (Math.random() * 9 + 1);
		for (int i = 0; i < 9; i++) {
			str += (int) (Math.random() * 10);
		}
		return str;
    }
	
	public static int getRandNum(int min, int max) {
	    int randNum = min + (int)(Math.random() * ((max - min) + 1));
	    return randNum;
	}

	public static void main(String[] args) {
		System.out.println(genInviteCode());
//		while (true) {
//			try {
//				Thread.sleep(500);
//				System.out.println(generateShortUuid());
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		for (int i=0; i<1000000; i++) {
//			String s = genInviteCode();
//			if (s.length() != 6) {
//				System.out.println("----------------------------->"+s);
//			}
//			System.out.println(s);
//		}
		
		
	}

}
