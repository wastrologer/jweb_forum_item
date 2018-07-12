package com.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;
@Component
public class QrCodeUtil {
    Integer defaultSize=200;//二维码size

    public void createQrCode(Integer size,String url,HttpServletResponse response){
        //二维码容错率，分四个等级：H、L 、M、 Q

        ErrorCorrectionLevel level = ErrorCorrectionLevel.H;

        //String qrName = "test.png"; //生成二维码图片名称
        //生成二维码中的设置
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
        hints.put(EncodeHintType.ERROR_CORRECTION, level); //容错率
        hints.put(EncodeHintType.MARGIN, 0);  //二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因，

        String content = url;


        try {
            //int size = 200;  //二维码图片大小
            if(size==null||size<10){
                size=defaultSize;
            }

            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size,hints); //生成bitMatrix

            int margin = 5;  //自定义白边边框宽度

            bitMatrix = updateBit(bitMatrix, margin);  //生成新的bitMatrix

            //因为二维码生成时，白边无法控制，去掉原有的白边，再添加自定义白边后，二维码大小与size大小就存在差异了，
            // 为了让新生成的二维码大小还是size大小，根据size重新生成图片

            BufferedImage bi =  MatrixToImageWriter.toBufferedImage(bitMatrix);
            bi = zoomInImage(bi,size,size);//根据size放大、缩小生成的二维码
            ImageIO.write(bi, "png", response.getOutputStream()); //生成二维码图片
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void createQrCode(String url,HttpServletResponse response){
        createQrCode(defaultSize,url,response);
    }

    private BitMatrix updateBit(BitMatrix matrix, int margin){
        int tempM = margin*2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for(int i= margin; i < resWidth- margin; i++){   //循环，将二维码图案绘制到新的bitMatrix中
            for(int j=margin; j < resHeight-margin; j++){
                if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
                    resMatrix.set(i,j);
                }
            }
        }
        return resMatrix;
    }



    /**
     * 图片放大缩小
     */
    public static BufferedImage  zoomInImage(BufferedImage  originalImage, int width, int height){
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0,0,width,height,null);
        g.dispose();
        return newImage;

    }
}
