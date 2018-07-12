package com.common.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.file.FileMetadataDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

public class ImageUtil {
	static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
//	/*** 
//     * 功能 :调整图片大小 开发：wuyechun 2011-7-22 
//     * @param srcImgPath 原图片路径 
//     * @param distImgPath  转换大小后图片路径 
//     * @param width   转换后图片宽度 
//     * @param height  转换后图片高度 
//     */  
//    public static void resizeImage(String srcImgPath, String distImgPath,  
//            int width, int height) throws IOException {  
//  
//        File srcFile = new File(srcImgPath);  
//        Image srcImg = ImageIO.read(srcFile);  
//        BufferedImage buffImg = null;  
//        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
//        buffImg.getGraphics().drawImage(  
//                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,  
//                0, null);  
//        
////        buffImg.getGraphics().drawImage(  
////                srcImg, 0, 0,width, height, null);  
//  
//        ImageIO.write(buffImg, "PNG", new File(distImgPath));  
//  
//    }  
    
	/*** 
     * 对图片等比截取，放缩 
     * @param srcImgPath 原图片路径 
     * @param destImgPath  转换大小后图片路径 
     * @param width   转换后图片目标宽度 
     * @param height  转换后图片目标高度
     * @return
     * int[0] 转换后图片实际宽度 
     * int[1] 转换后图片实际宽度 
     */  
    public static int[] resizeImage(String srcImgPath, String destImgPath,  
            int width, int height) throws IOException {  
    	if(width<300 && height<300) {
    		// 小图模式截取
    		return resizeImageSmall(srcImgPath, destImgPath, width, height);
    	}else{
    		// 大图模式截取
    		return resizeImageBig(srcImgPath, destImgPath, width, height);
    	}
    }
    /*** 
     * 小图模式对图片等比截取，放缩 
     * @param srcImgPath 原图片路径 
     * @param destImgPath  转换大小后图片路径 
     * @param width   转换后图片目标宽度 
     * @param height  转换后图片目标高度
     * @return
     * int[0] 转换后图片实际宽度 
     * int[1] 转换后图片实际宽度 
     */  
    public static int[] resizeImageSmall(String srcImgPath, String destImgPath,  
            int width, int height) throws IOException {  
    	// 文件后缀
    	String suffix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1);
    	float destRatio = (float)width/height; 
        BufferedImage srcImg = ImageIO.read(new File(srcImgPath)); 
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        float srcRatio = (float)srcWidth/srcHeight;
        // 为了放缩后的图片不变形，需要重新计算可截取的区域，宽高比例为目标宽高比例
        int newX = 0;
        int newY = 0;
        int newWidth = 0;
        int newHeight = 0;
        if(destRatio <= srcRatio){
        	// 目标宽高比例 小于等于 原始图片比例，那么原始图片可截取缩小的区域以原始图片高度为基准进行转换
        	newWidth = (int) (srcHeight*destRatio);
        	newHeight = srcHeight;
        	newX = (srcWidth - newWidth)/2;
        	newY = 0;
        }else{
        	// 目标宽高比例 小于 原始图片比例，那么原始图片可截取缩小的区域以原始图片宽度为基准进行转换
        	newWidth = srcWidth;
        	newHeight = (int) (srcWidth/destRatio);
        	newX = 0;
        	newY = (srcHeight - newHeight)/2;
        }
        // 先截取需要的区域，在等比放缩
        Image tmpImg = srcImg.getSubimage(newX, newY, newWidth, newHeight).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        Graphics g = buffImg.getGraphics();
        g.drawImage(tmpImg, 0, 0, null);  
        g.dispose();

        ImageIO.write(buffImg, suffix, new File(destImgPath));  
        int[] widthHeight = new int[2];
        widthHeight[0] = width;
        widthHeight[1] = height;
        return widthHeight;
    }
    
    /*** 
     * 大图模式将原图按照目标图片比例完整的放缩至目标图片区域。
     * @param srcImgPath 原图片路径 
     * @param destImgPath  转换大小后图片路径 
     * @param width   转换后图片目标宽度 
     * @param height  转换后图片目标高度
     * @return
     * int[0] 转换后图片实际宽度 
     * int[1] 转换后图片实际宽度 
     */  
    public static int[] resizeImageBig(String srcImgPath, String destImgPath,  
            int width, int height) throws IOException {  
    	// 文件后缀
    	String suffix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1);
        BufferedImage srcImg = ImageIO.read(new File(srcImgPath)); 
        // 为了能将原图全部放缩进去，需要重新计算放缩后宽、高，以及放至目标位图中的x、y坐标值
        int newWidth = 0;
        int newHeight = 0;
        int newX = 0;
        int newY = 0;

        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        
        float widthRatio = (float)srcWidth/width;
        float heightRatio = (float)srcHeight/height;
        
        if(widthRatio <= 1){
        	if(heightRatio <= 1){
        		// 原图宽高都小于等于目标图宽高
        		newWidth = srcWidth;
                newHeight = srcHeight;
//        		newX = (width - srcWidth)/2;
//                newY = (height - srcHeight)/2;
        	}else{
        		// 原图宽小于等于目标图宽
        		// 原图高大于目标图高
//                newY = 0;
                newHeight = height;
                newWidth = srcWidth* height/srcHeight;
//                newX = (width - newWidth)/2;
        	}
        }else{
			if(heightRatio <= 1){
				// 原图宽大于目标图宽
        		// 原图高小于等于目标图高
//				newX = 0;
                newWidth = width;
                newHeight = srcHeight * width/srcWidth;
//                newY = (height - newHeight)/2;
        	}else{
        		// 原图宽大于目标图宽
        		// 原图高大于目标图高
//        		newX = 0;
//                newY = 0;
                if(widthRatio<=heightRatio){
                	// 以原图高为基准进行原图等比放缩
                	newHeight = height;
                	newWidth = srcWidth* height/srcHeight;
//                	newX = (width - newWidth)/2;
                }else{
                	// 以原图宽为基准进行原图等比放缩
                	newWidth = width;
                	newHeight = srcHeight * width/srcWidth;
//                	newY = (height - newHeight)/2;
                }
        	}
        }
        
     // 先截取需要的区域，在等比放缩
        Image tmpImg = srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    	BufferedImage buffImg = new BufferedImage(newWidth, newHeight, srcImg.getType());//BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D)buffImg.getGraphics();
//        buffImg = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT); 
//        g.dispose(); 
//        g = buffImg.createGraphics();  
        g.drawImage(tmpImg, newX, newY, null);  
        g.dispose();

        ImageIO.write(buffImg, suffix, new File(destImgPath));
        
//		logger.info("img -----------> " + suffix);
//        if (suffix.equals("png")) {
//            BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);  
//            Graphics2D g2d = to.createGraphics();  
//            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);  
//            g2d.dispose();  
//            g2d = to.createGraphics();  
//            Image from = srcImg.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_AREA_AVERAGING);  
//  
//            g2d.drawImage(from, 0, 0, null);  
//            g2d.dispose();  
//  
//            ImageIO.write(to, "png", new File(destImgPath));
//        	
//        } else {
//        	
//        }
        int[] widthHeight = new int[2];
        widthHeight[0] = newWidth;
        widthHeight[1] = newHeight;
         
        return widthHeight;
    }
    
    /**
     * 读取图片文件拍摄时间和文件最后修改时间
     * @param filePath
     * @return dateArray
     *   dateArray[0] date类型   文件拍摄时间 
     *   dateArray[1] date类型   文件最后修改时间
     *   dataArray[2] string类型   图片角度 （8 图片向左，1 图片向上, 6 图片向右，3 图片向下）
     */
    public static Object[] getImgDateTime(String filePath){
    	Object[] infoArray= new Object[3];
    	File imgFile = new File(filePath);
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
			Iterator<Directory> exif = metadata.getDirectories().iterator();
			while (exif.hasNext()) {
				Directory dir = (Directory) exif.next();
				if (dir instanceof ExifIFD0Directory) {
					Date date = dir.getDate(ExifIFD0Directory.TAG_DATETIME,
							TimeZone.getDefault());
					infoArray[0] = date;
					infoArray[2] = dir.getString(ExifIFD0Directory.TAG_ORIENTATION);
				} else if (dir instanceof FileMetadataDirectory) {
					Date date = dir.getDate(
							FileMetadataDirectory.TAG_FILE_MODIFIED_DATE,
							TimeZone.getDefault());
					infoArray[1] = date;
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ImageProcessingException e) {
			logger.error(e.getMessage(), e);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		} catch(Throwable t){
			logger.error(t.getMessage(), t);
		}
    	return infoArray;
    }
    
    public static void main2(String[] args)throws Exception{
    	long s = System.currentTimeMillis();
    	String path = "C:/Users/Administrator/Pictures/ZFB/";
    	resizeImageSmall(path+"1.png",
    			path+"1_new.png",95,95);
    	System.out.println(System.currentTimeMillis() - s);
    	
//    	Object[] dateArray = getImgDateTime("C:\\Users\\Administrator\\Pictures\\QQ截图20150228163427.png");
//    	if(dateArray[0] != null){
//    		System.out.println(((Date)dateArray[0]).toLocaleString());
//    	}
//    	if(dateArray[1] != null){
//    		System.out.println(((Date)dateArray[1]).toLocaleString());
//    	}
    }
    
    /**
     * 对图片进行旋转
     * @param srcImgPath
     * @param destImgPath
     * @param degree
     * @return
     * @throws IOException
     */
    public static void rotateImg(String srcImgPath, String destImgPath, int degree) throws IOException { 
    	BufferedImage image = ImageIO.read(new File(srcImgPath)); 
    	// 文件后缀
    	String suffix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1);
        int iw = image.getWidth();//原始图象的宽度 
        int ih = image.getHeight();//原始图象的高度 
        int w = 0; 
        int h = 0; 
        int x = 0; 
        int y = 0; 
        degree = degree % 360; 
        if (degree < 0) 
            degree = 360 + degree;//将角度转换到0-360度之间 
        double ang = Math.toRadians(degree);//将角度转为弧度 
  
        /**
         *确定旋转后的图象的高度和宽度
         */ 
  
        if (degree == 180 || degree == 0 || degree == 360) { 
            w = iw; 
            h = ih; 
        } else if (degree == 90 || degree == 270) { 
            w = ih; 
            h = iw; 
        } else { 
            //int d = iw + ih; 
            //w = (int) (d * Math.abs(Math.cos(ang))); 
              //h = (int) (d * Math.abs(Math.sin(ang)));
            double cosVal = Math.abs(Math.cos(ang));
            double sinVal = Math.abs(Math.sin(ang));
            w = (int) (sinVal*ih) + (int) (cosVal*iw);
            h = (int) (sinVal*iw) + (int) (cosVal*ih);
        } 
  
        x = (w / 2) - (iw / 2);//确定原点坐标 
        y = (h / 2) - (ih / 2); 
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType()); 
        Graphics2D gs = (Graphics2D)rotatedImage.getGraphics(); 
        rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT); 
          
        AffineTransform at = new AffineTransform(); 
        at.rotate(ang, w / 2, h / 2);//旋转图象 
        at.translate(x, y); 
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC); 
        op.filter(image, rotatedImage); 
        image = rotatedImage; 
          
        ImageIO.write(image, suffix, new File(destImgPath)); 
//        ByteArrayOutputStream byteOut= new ByteArrayOutputStream(); 
//        ImageOutputStream iamgeOut = ImageIO.createImageOutputStream(byteOut); 
//          
//        ImageIO.write(image, "png", iamgeOut); 
//        InputStream inputStream = new ByteArrayInputStream(byteOut.toByteArray()); 
//          
//        return inputStream; 
    } 
    
    public static void main(String[] args)throws Exception{
    	long s = System.currentTimeMillis();
    	String path = "C:/Users/Administrator/Pictures/ios/2/";
    	resizeImageBig(path+"3.jpg",
    			path+"3_new.jpg",300,1000);
    	System.out.println(System.currentTimeMillis() - s);
    	
//    	// 为了能将原图全部放缩进去，需要重新计算放缩后宽、高，以及放至目标位图中的x、y坐标值
//    	int newX = 0;
//        int newY = 0;
//        int newWidth = 0;
//        int newHeight = 0;
//       
//        
//        int width = 760;
//        int height = 760;
//        int srcWidth = 800;
//        int srcHeight = 780;
//        
//        float widthRatio = (float)srcWidth/width;
//        float heightRatio = (float)srcHeight/height;
//        
//        if(widthRatio <= 1){
//        	if(heightRatio <= 1){
//        		// 原图宽高都小于等于目标图宽高
//        		newWidth = srcWidth;
//                newHeight = srcHeight;
//        		newX = (width - srcWidth)/2;
//                newY = (height - srcHeight)/2;
//        	}else{
//        		// 原图宽小于等于目标图宽
//        		// 原图高大于目标图高
//                newY = 0;
//                newHeight = height;
//                newWidth = srcWidth* height/srcHeight;
//                newX = (width - newWidth)/2;
//        	}
//        }else{
//			if(heightRatio <= 1){
//				// 原图宽大于目标图宽
//        		// 原图高小于等于目标图高
//				newX = 0;
//                newWidth = width;
//                newHeight = srcHeight * width/srcWidth;
//                newY = (height - newHeight)/2;
//        	}else{
//        		// 原图宽大于目标图宽
//        		// 原图高大于目标图高
//        		newX = 0;
//                newY = 0;
//                if(widthRatio<=heightRatio){
//                	// 以原图高为基准进行原图等比放缩
//                	newHeight = height;
//                	newWidth = srcWidth* height/srcHeight;
//                	newX = (width - newWidth)/2;
//                }else{
//                	// 以原图宽为基准进行原图等比放缩
//                	newWidth = width;
//                	newHeight = srcHeight * width/srcWidth;
//                	newY = (height - newHeight)/2;
//                }
//        	}
//        }
//        System.out.println("newX="+newX);
//        System.out.println("newY="+newY);
//        System.out.println("newWidth="+newWidth);
//        System.out.println("newHeidht="+newHeight);
    }
    
    
    
 }
