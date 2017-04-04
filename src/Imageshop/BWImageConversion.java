
package Imageshop;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.lang.reflect.Array;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;

public class BWImageConversion {
    public BWImageConversion() {
    }

	/**
	 * Turn RGB or gray image to B&W image
	 */
	public Image showBW(BufferedImage image) {
		int green = 0, red = 0, blue = 0, rgb;

		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		BufferedImage BWImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_BINARY);
		int[][] gray = new int[imageWidth][imageHeight];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值
				red = image.getColorModel().getRed(data);
				blue = image.getColorModel().getBlue(data);
				green = image.getColorModel().getGreen(data);

				// 计算灰度值，计算公式为r*0.3+g*0.59+b*0.11
				gray[i][j] = (red * 3 + green * 6 + blue * 1) / 10;
			}
		}

		// 确定阀值
		int threshold = 128;
		// 循环设置图片像素灰度值
		// 大于阀值，rgb都设置为255；小于阀值，rgb都设置为0
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				if (getAverageColor(gray, i, j, imageWidth, imageHeight) < threshold) {
					BWImage.setRGB(i, j, new Color(0, 0, 0).getRGB());
				} else {
					BWImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
				}
			}
		}

		return BWImage;
	}

	/**
	 * 计算像素与周围8个像素的平均灰度值 将一个像素点灰度值和它周围的8个灰度值相加再除以9
	 * 
	 * @param gray
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return rs / 9
	 */
	private int getAverageColor(int[][] gray, int x, int y, int w, int h) {
		int rs = gray[x][y] + (x == 0 ? 255 : gray[x - 1][y])
				+ (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])
				+ (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])
				+ (y == 0 ? 255 : gray[x][y - 1])
				+ (y == h - 1 ? 255 : gray[x][y + 1])
				+ (x == w - 1 ? 255 : gray[x + 1][y])
				+ (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])
				+ (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);
		return rs / 9;
	}
}

