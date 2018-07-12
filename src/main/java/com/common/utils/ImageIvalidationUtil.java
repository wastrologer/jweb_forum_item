package com.common.utils;

import javax.servlet.ServletRequest;

import java.util.Random;

import java.awt.Font;

import javax.servlet.http.HttpServletRequest;

import java.awt.Color;

import java.awt.image.BufferedImage;

import java.awt.Graphics;

public class ImageIvalidationUtil {

/*    方法createImage(ServletRequest request,String imageName)产生图片并以imageName为键名保存到会话中，
    返回图片上显示的字符串，这个字符也以“valiCode”为键名保存在会话中。*/
    public static String createImage(ServletRequest request,String imageName){

        HttpServletRequest servletRequest=(HttpServletRequest)request;

        Random random=new Random();

        String valiCode="";

        for (int i = 0; i < 4; i++) {

            String rand = String.valueOf(random.nextInt(10));

            valiCode+=rand;

        }

        if(servletRequest.getSession()==null)

            servletRequest.getSession().setAttribute("valiCode",valiCode);

        else

            servletRequest.getSession(false).setAttribute("valiCode",valiCode);



        int width = 60, height = 20;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        g.setColor(new Color(0xDCDCDC));

        g.fillRect(0, 0, width, height);

        g.setColor(Color.black);

        g.drawRect(0,0,width-1,height-1);

        g.setFont(new Font("Atlantic Inline",Font.PLAIN,18));

        g.setColor(Color.black);

        g.drawString(valiCode.substring(0,1), 8, 17);

        g.drawString(valiCode.substring(1,2), 20, 15);

        g.drawString(valiCode.substring(2,3), 35, 18);

        g.drawString(valiCode.substring(3,4), 45, 15);

        g.dispose();

        servletRequest.getSession(false).setAttribute(imageName,image);

        return valiCode;

    }

}
