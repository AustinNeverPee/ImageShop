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

public class ImageQuantization {
	/**
	 * 将图像转化为量化的灰度图
	 * 
	 * @param image
	 *            原图像
	 * @param level
	 *            灰度级数
	 * @return
	 */
	public Image quantize(BufferedImage image, int level) {
		int red, green, blue, simplePixel, actualPixel;

		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		int[][] gray = new int[imageWidth][imageHeight];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red = image.getColorModel().getRed(data);
				blue = image.getColorModel().getBlue(data);
				green = image.getColorModel().getGreen(data);

				// 计算灰度值，计算公式为r*0.3+g*0.59+b*0.11
				gray[i][j] = (red * 3 + green * 6 + blue * 1) / 10;
			}
		}

		// 定义输出图像
		BufferedImage levelImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		// 确定阀值
		int threshold = 256 / level;
		// 循环设置图片像素灰度值
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				simplePixel = gray[i][j] / threshold;
				actualPixel = (simplePixel * (256 / (level - 1))) > 255 ? 255
						: simplePixel * (256 / (level - 1));

				levelImage.setRGB(i, j, new Color(actualPixel, actualPixel,
						actualPixel).getRGB());
			}
		}
		return levelImage;
	}

	/**
	 * 读入两张图像，按照scale比例进行图片混合
	 * 
	 * @param imageFirst
	 * @param imageSecond
	 * @param scale
	 * @return
	 */
	public Image slowChange(BufferedImage imageFirst,
			BufferedImage imageSecond, int scale) {
		int red1, red2, blue1, blue2, green1, green2, rgb;

		int imageWidth = imageFirst.getWidth();
		int imageHeight = imageFirst.getHeight();

		BufferedImage mixImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);

		for (int i = imageFirst.getMinX(); i < imageWidth; i++) {
			for (int j = imageFirst.getMinY(); j < imageHeight; j++) {
				// 获取两张图像该点像素，并以object类型表示
				Object data1 = imageFirst.getRaster().getDataElements(i, j,
						null);
				Object data2 = imageSecond.getRaster().getDataElements(i, j,
						null);

				// 取出两张图像该点像素的r、g、b值
				red1 = imageFirst.getColorModel().getRed(data1);
				blue1 = imageFirst.getColorModel().getBlue(data1);
				green1 = imageFirst.getColorModel().getGreen(data1);
				red2 = imageSecond.getColorModel().getRed(data2);
				blue2 = imageSecond.getColorModel().getBlue(data2);
				green2 = imageSecond.getColorModel().getGreen(data2);

				// 计算混合图像改点像素的r、g、b值
				red1 = (red1 * (100 - scale) + red2 * scale) / 100;
				blue1 = (blue1 * (100 - scale) + blue2 * scale) / 100;
				green1 = (green1 * (100 - scale) + green2 * scale) / 100;

				// 将r、g、b再转化为rgb值
				// 因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb
				// rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
				rgb = (red1 * 256 + green1) * 256 + blue1;
				if (rgb > 8388608) {
					rgb = rgb - 16777216;
				}

				// 将rgb值写回图片
				mixImage.setRGB(i, j, rgb);
			}
		}

		return mixImage;
	}
}

