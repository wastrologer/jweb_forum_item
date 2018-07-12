package com.pojo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ImageValidateCode {
	
	 // 图片的宽度。
    private int width = 160;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 150;
    // 验证码
    private String imageCode = null;
    // 验证码图片Buffer
    private BufferedImage buffImg = null;
    
    private String resultCode = null; 

    // 验证码范围,去掉0(数字)和O(拼音)容易混淆的(小写的1和L也可以去掉,大写不用了)
    private char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
//    private String[] codeSequence = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
//    		"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
//    private char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//    		'\u96f6', '\u4e00', '\u4e8c', '\u4e09', '\u56db', '\u4e94', '\u516d', '\u4e03', '\u516b', '\u4e5d'};
    
//    private char[] algorithm = {'\u002b', '\u002d', '\u002a', '\u52a0', '\u51cf', '\u4e58'};
//    private String[] algorithm = {'+', '-', '*', '加', '减', '乘'};
    
//    private char[] result = {'\u003d', '\u7b49'};
//    private String[] result = {'=', '等'};

    /**
     * 默认构造函数,设置默认参数
     */
    public ImageValidateCode() {
        this.createCode();
    }

    /**
     * @param width  图片宽
     * @param height 图片高
     */
    public ImageValidateCode(int width, int height) {
        this.width = width;
        this.height = height;
        this.createCode();
    }

    /**
     * @param width     图片宽
     * @param height    图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     */
    public ImageValidateCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.createCode();
    }

    public void createCode() {
        int x = 0, fontHeight = 0, codeY = 0;
        int red = 0, green = 0, blue = 0;

        x = width / (codeCount + 2);//每个字符的宽度(左右各空出一个字符)
        fontHeight = height - 2;//字体的高度
        codeY = height - 4;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体,可以修改为其它的
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
//        Font font = new Font("Times New Roman", Font.ROMAN_BASELINE, fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            // 设置随机开始和结束坐标
            int xs = random.nextInt(width);//x坐标开始
            int ys = random.nextInt(height);//y坐标开始
            int xe = xs + random.nextInt(width / 8);//x坐标结束
            int ye = ys + random.nextInt(height / 8);//y坐标结束

            // 产生随机的颜色值，让输出的每个干扰线的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // randomCode记录随机产生的验证码
        StringBuffer randomCode = new StringBuffer();
        
        
        //获取参与运算的两个数字
//        int[] number = getParam(random, (byte)1);
        //获取运算符
//        int[] symbol = getParam(random, (byte)2);
      //获取等于号
//        int[] resultSymbol  = getParam(random, (byte)3);
//        addStr(red, green, blue, g, x, codeY, 1, random, randomCode, codeSequence[number[0]]);
//        addStr(red, green, blue, g, x, codeY, 2, random, randomCode, algorithm[symbol[0]]);
//        addStr(red, green, blue, g, x, codeY, 3, random, randomCode, codeSequence[number[1]]);
//        addStr(red, green, blue, g, x, codeY, 4, random, randomCode, result[resultSymbol[0]]);
        
//        imageCode = randomCode.toString();
//        resultCode = operationResults(number, symbol);
//        System.out.println(code + str);
        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        // 将四位数字的验证码保存到Session中。
        imageCode = randomCode.toString();
    }
    
    private String operationResults(int[] number,int[] symbol){
    	String value = null;
    	int a = number[0] >= 10 ? number[0] -10 : number[0];
    	int b = number[1] >= 10 ? number[1] -10 : number[1];
    	if (symbol[0] == 0) {
    		int result = a + b;  
    		value = result + "";
		}else if (symbol[0] == 1) {
			int result = a - b;  
    		value = result + "";
		}else if (symbol[0] == 2) {
			int result = a * b;  
    		value = result + "";
		}else if (symbol[0] == 3) {
			int result = a + b;  
    		value = result + "";
		}else if (symbol[0] == 4) {
			int result = a - b;  
    		value = result + "";
		}else if (symbol[0] == 5) {
			int result = a * b;  
    		value = result + "";
		}
    	
    	
//    	if (algorithm[symbol[0]].equals("+")) {
//    		int result = a + b;  
//    		value = result + "";
//		}else if (algorithm[symbol[0]].equals("-")) {
//			int result = a - b;  
//    		value = result + "";
//		}else if (algorithm[symbol[0]].equals("*")) {
//			int result = a * b;  
//    		value = result + "";
//		}else if (algorithm[symbol[0]].equals("加")) {
//			int result = a + b;  
//    		value = result + "";
//		}else if (algorithm[symbol[0]].equals("减")) {
//			int result = a - b;  
//    		value = result + "";
//		}else if (algorithm[symbol[0]].equals("乘")) {
//			int result = a * b;  
//    		value = result + "";
//		}
    	return value;
    }
    
    private StringBuffer addStr(int red,int green,int blue,Graphics2D g,int x,int codeY, int num, 
    		Random random,
    		StringBuffer randomCode,char value){
    	
    	String strRand = String.valueOf(value);
        // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
        red = random.nextInt(255);
        green = random.nextInt(255);
        blue = random.nextInt(255);
        g.setColor(new Color(red, green, blue));
        g.drawString(strRand, (num + 1) * x, codeY);
        // 将产生的四个随机数组合在一起。
        randomCode.append(strRand);
        
    	return randomCode;
    }
    
//    private int[] getParam(Random random,Byte type){
//    	int[] str = null;
//    	if (type == 1) {
//    		//获取参与运算的数字 的下标
//    		str = new int[]{random.nextInt(codeSequence.length),random.nextInt(codeSequence.length)};
//		}else if(type == 2){
//			//获取参与运算的运算符
//			str = new int[]{random.nextInt(algorithm.length)};
//		}else if (type == 3) {
//			//获取等于号
//			str = new int[]{random.nextInt(result.length)};
//		}
//    	return str;
//    }
    

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }
    
    
    
    public String getImageCode() {
		return imageCode;
	}


	public String getResultCode() {
		return resultCode;
	}

	/**
     * 测试函数,默认生成到d盘
     * @param args
     */
    public static void main(String[] args) throws IOException {
    	String path="D:/"+"图片验证码"+".png";
    	ImageValidateCode vCode = new ImageValidateCode(120,40,7,30);
		System.out.println(vCode.getImageCode()+" >"+path);
		vCode.write(path);
    }
}
