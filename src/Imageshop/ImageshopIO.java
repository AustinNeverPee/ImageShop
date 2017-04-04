/*
 * ImageIO 图像读写
 * @author austin
 * 修改� 2014.10.11
 */

package Imageshop;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;

import java.io.*;
import java.util.logging.*;

/**
 * Implement the interface IImageIO
 * Override the myRead and myWrite method
 */
public class ImageshopIO {
    /**
     * Read BMP file without using API provided by Java.
     * @return the image if the operation is successful or null otherwise
     */
	public Image imageRead(String arg0) {
		try {
			File f = new File(arg0);
			BufferedImage bi = ImageIO.read(f);
			return bi;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Image)null;
	}

    /**
     * Write pictures into file.
     * @return Image if successful or null otherwise
     */
    public Image imageWrite(BufferedImage arg0, String arg1) {
        try {
            //JAVA API can only write the pictures in the memory into file.
            //So, we create a file buffer to store the pictures temporarily.
            File imgFile = new File(arg1);
            BufferedImage bi = new BufferedImage(arg0.getWidth(null), arg0.getHeight(null), arg0.getType());
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(arg0, 0, 0, null);
            g2.dispose();
            
            //淇濆瓨鍥剧墖
            ImageIO.write(bi, "bmp", imgFile);
            return arg0;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return arg0;
    }
}