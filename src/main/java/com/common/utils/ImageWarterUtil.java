package com.common.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/*******************************************************************************
 * Description: 图片水印工具类
 * @version 1.0
 */
public class ImageWarterUtil {

	// 水印透明度
	private static float alpha = 0.5f;
	// 水印横向位置
	private static int positionWidth = 150;
	// 水印纵向位置
	private static int positionHeight = 300;
	// 水印文字字体
	private static Font font = new Font("楷体", Font.BOLD, 13);

	//private static GraphicsEnvironment environment;
	// 水印文字颜色
	private static Color color = Color.red;
	
	private static int interval = 0; 

	/**
	 * 
	 * @param alpha
	 *            水印透明度
	 * @param positionWidth
	 *            水印横向位置
	 * @param positionHeight
	 *            水印纵向位置
	 * @param font
	 *            水印文字字体
	 * @param color
	 *            水印文字颜色
	 */
	public static void setImageMarkOptions(float alpha, int positionWidth, int positionHeight, Font font, Color color) {
		if (alpha != 0.0f)
			ImageWarterUtil.alpha = alpha;
		if (positionWidth != -1)
			ImageWarterUtil.positionWidth = positionWidth;
		if (positionHeight != -1)
			ImageWarterUtil.positionHeight = positionHeight;
		if (font != null)
			ImageWarterUtil.font = font;
		if (color != null)
			ImageWarterUtil.color = color;
	}

	/**
	 * 给图片添加水印图片
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void markImageByIcon(Integer userId,String newIconPath,String iconPath, String srcImgPath, String targerPath, boolean isArray ,Integer interval) {
		markImageByIcon( userId, newIconPath,iconPath, srcImgPath, targerPath, null, isArray, interval);
	}

	/**
	 * 给图片添加水印图片、可设置水印图片旋转角度
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 * @param isArray
	 * 			  是否阵列
	 */
	public static void markImageByIcon(Integer userId,String newIconPath,String iconPath, String srcImgPath, String targerPath, Integer degree, boolean isArray ,Integer interval) {
		OutputStream os = null;
		try {

			Image srcImg = ImageIO.read(new File(srcImgPath));

			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			// 1、得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 2、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);
			// 3、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}

			//4.1生成新的水印图片
			String newPath=newIconPath+userId+"/water_mark_new.png";
			int[] width=getImageWidthHeight(srcImgPath);
			int newWidth=(width[0]+width[1])/16;
			ImageUtil.resizeImageBig(iconPath,newPath,newWidth,newWidth);
			// 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(newPath);

			// 5、得到Image对象。
			Image img = imgIcon.getImage();

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			// 6、水印图片的位置  阵列时使用
//			if (isArray) {
//				for (int height = interval + imgIcon.getIconHeight(); height < buffImg.getHeight(); height = height
//						+ interval + imgIcon.getIconHeight()) {
//					for (int weight = interval + imgIcon.getIconWidth(); weight < buffImg.getWidth(); weight = weight
//							+ interval + imgIcon.getIconWidth()) {
//						g.drawImage(img, weight - imgIcon.getIconWidth(), height - imgIcon.getIconHeight(), null);
//					}
//				}
//			}
			//阵列时注释掉
			g.drawImage(img, positionWidth, positionHeight, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			// 7、释放资源
			g.dispose();

			// 8、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

			System.out.println("图片完成添加水印图片");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给图片添加水印文字
	 * 
	 * @param logoText
	 *            水印文字
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void markImageByText(String logoText, String srcImgPath, String targerPath) {
		markImageByText(logoText, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * 
	 * @param logoText
	 * @param srcImgPath
	 * @param targerPath
	 * @param degree
	 */
	public static void markImageByText(String logoText, String srcImgPath, String targerPath, Integer degree) {

		InputStream is = null;
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			// 2、得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 3、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,0, null);
			// 4、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}
			// 5、设置水印文字颜色
			g.setColor(color);
			// 6、设置水印文字Font
			g.setFont(font);
			// 7、设置水印文字透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
			g.drawString(logoText, positionWidth, positionHeight);
			// 9、释放资源
			g.dispose();
			// 10、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

			System.out.println("图片完成添加水印文字");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int[] getImageWidthHeight(String path){
		int widthHeight[]=new int[2];
		try {
			
			File picture = new File(path);
            BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture)); 
//            System.out.println(String.format("%.1f",picture.length()/1024.0));// 源图大小
//            System.out.println(sourceImg.getWidth()); // 源图宽度
//            System.out.println(sourceImg.getHeight()); // 源图高度
			widthHeight[0] = sourceImg.getWidth();
			widthHeight[1] = sourceImg.getHeight();
		} catch (Exception e) {
			
		}
		return widthHeight;
	}


	/**
	 * @param srcImgPath 源图片路径
	 * @param tarImgPath 保存的图片路径
	 * @param waterMarkContent 水印内容

	 */
	public static void addWaterMark(String srcImgPath, String tarImgPath, String waterMarkContent) {

		try {
			// 读取原图片信息
			File srcImgFile = new File(srcImgPath);//得到文件
			Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
			int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
			int srcImgHeight = srcImg.getHeight(null);//获取图片的高
			// 加水印
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			g.setColor(Color.white); //根据图片的背景设置水印颜色
			font = new Font("黑体", Font.BOLD, (srcImgWidth+srcImgHeight)/60);
			//System.out.println(fonts[font.getSize()-2]);

			g.setFont(font);              //设置字体

			//设置水印的坐标
			int x = srcImgWidth - 1*getWatermarkLength(waterMarkContent, g)-srcImgWidth/40;
			int y = srcImgHeight - srcImgHeight/40;//1*getWatermarkLength(waterMarkContent, g);
			g.drawString(waterMarkContent, x, y);  //画出水印
			g.dispose();
			// 输出图片
			FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
			ImageIO.write(bufImg, "jpg", outImgStream);
			System.out.println("添加水印完成");
			outImgStream.flush();
			outImgStream.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}



	public static void main(String[] args) {
		String srcImgPath = "d:/456.png";
		String logoText = "复 印 无 效";
		String iconPath = "d:/1.png";

		String targerTextPath = "d:/456.png";
		String targerTextPath2 = "d:/qie_text_rotate.jpg";

		String targerIconPath = "d:/qie_icon.jpg";
		String targerIconPath2 = "d:/qie_icon_rotate.jpg";

//		System.out.println("给图片添加水印文字开始...");
//		 给图片添加水印文字
//		markImageByText(logoText, srcImgPath, targerTextPath);
//		System.out.println("给图片添加水印文字结束...");
		// 给图片添加水印文字,水印文字旋转-45
//		markImageByText(logoText, srcImgPath, targerTextPath2, -45);
//		System.out.println("给图片添加水印文字结束...");

		System.out.println("给图片添加水印图片开始...");
		setImageMarkOptions(0.1f, 125, 520, null, null);
		// 给图片添加水印图片
		//markImageByIcon(iconPath, srcImgPath, srcImgPath,true,0);
//		 给图片添加水印图片,水印图片旋转-45
//		markImageByIcon(iconPath, srcImgPath, targerIconPath2, -45);
		System.out.println("给图片添加水印图片结束...");

	}

}
