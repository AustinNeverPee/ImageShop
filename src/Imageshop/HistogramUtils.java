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

public class HistogramUtils {
	/**
	 * 实例化ShowHistogram类，显示灰度直方图
	 * 
	 * @param image
	 */
	public void showHist(BufferedImage image) {
		int red, blue, green;

		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取各点灰度
		int[][] grey = new int[imageWidth][imageHeight];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red = image.getColorModel().getRed(data);
				blue = image.getColorModel().getBlue(data);
				green = image.getColorModel().getGreen(data);
				// 计算灰度值，计算公式为r*0.3+g*0.59+b*0.11
				grey[i][j] = (red * 3 + green * 6 + blue * 1) / 10;
			}
		}

		ShowHistogram sh = new ShowHistogram();
		sh.getData(grey, imageWidth, imageHeight);
	}

	/**
	 * 对读入图像进行直方图均衡化 分别对R、G、B三个通道进行处理
	 * 
	 * @param image
	 * @return
	 */
	public Image histogramEqualization(BufferedImage image) {
		int red, blue, green, rgb;
		int greyLevel = 256;

		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取各点RGB直方图
		int[] redhis = new int[256];
		int[] greenhis = new int[256];
		int[] bluehis = new int[256];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red = image.getColorModel().getRed(data);
				green = image.getColorModel().getGreen(data);
				blue = image.getColorModel().getBlue(data);

				redhis[red]++;
				greenhis[green]++;
				bluehis[blue]++;
			}
		}

		BufferedImage histImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int[] credhis = new int[256];
		int[] cgreenhis = new int[256];
		int[] cbluehis = new int[256];
		int totalPixel = imageWidth * imageHeight;
		// 根据直方图均衡化公式计算各点RGB累计概率分布
		for (int i = 0; i < greyLevel; i++) {
			double sum = 0;
			for (int j = 0; j < i; j++) {
				sum += ((double) redhis[j]) / totalPixel;
			}
			credhis[i] = (int) (sum * 255.0);

			sum = 0;
			for (int j = 0; j < i; j++) {
				sum += ((double) greenhis[j]) / totalPixel;
			}
			cgreenhis[i] = (int) (sum * 255.0);

			sum = 0;
			for (int j = 0; j < i; j++) {
				sum += ((double) bluehis[j]) / totalPixel;
			}
			cbluehis[i] = (int) (sum * 255.0);
		}
		// 算出直方图均衡化后各点灰度值
		for (int i = 0; i < imageWidth; i++) {
			for (int j = 0; j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red = image.getColorModel().getRed(data);
				green = image.getColorModel().getGreen(data);
				blue = image.getColorModel().getBlue(data);

				red = credhis[red];
				green = cgreenhis[green];
				blue = cbluehis[blue];

				// 将r、g、b再转化为rgb值
				// 因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb
				// rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
				rgb = (red * 256 + green) * 256 + blue;
				if (rgb > 8388608) {
					rgb = rgb - 16777216;
				}

				// 将rgb值写回图片
				histImage.setRGB(i, j, rgb);
			}
		}

		return histImage;
	}

	/**
	 * 按照HW4中要求对读入图像进行直方图均衡化 对R、G、B三个通道的直方图取平均，使用平均直方图对三个通道进行直方图均衡化
	 * 
	 * @param image
	 * @return
	 */
	public Image histogramEqualizationHW4(BufferedImage image) {
		int red, blue, green, rgb;
		int greyLevel = 256;

		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取各点RGB直方图
		int[] redhis = new int[256];
		int[] greenhis = new int[256];
		int[] bluehis = new int[256];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red = image.getColorModel().getRed(data);
				green = image.getColorModel().getGreen(data);
				blue = image.getColorModel().getBlue(data);

				redhis[red]++;
				greenhis[green]++;
				bluehis[blue]++;
			}
		}

		// 对r、g、b三个通道的直方图取平均
		int[] averagehis = new int[256];
		for (int i = 0; i < 256; i++) {
			averagehis[i] = (redhis[i] + greenhis[i] + bluehis[i]) / 3;
		}

		BufferedImage histImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int[] chis = new int[256];
		int totalPixel = imageWidth * imageHeight;
		// 根据直方图均衡化公式计算各点RGB累计概率分布
		for (int i = 0; i < greyLevel; i++) {
			double sum = 0;
			for (int j = 0; j < i; j++) {
				sum += ((double) averagehis[j]) / totalPixel;
			}
			chis[i] = (int) (sum * 255.0);
		}
		// 算出直方图均衡化后各点灰度值
		for (int i = 0; i < imageWidth; i++) {
			for (int j = 0; j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red = image.getColorModel().getRed(data);
				green = image.getColorModel().getGreen(data);
				blue = image.getColorModel().getBlue(data);

				red = chis[red];
				green = chis[green];
				blue = chis[blue];

				// 将r、g、b再转化为rgb值
				// 因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb
				// rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
				rgb = (red * 256 + green) * 256 + blue;
				if (rgb > 8388608) {
					rgb = rgb - 16777216;
				}

				// 将rgb值写回图片
				histImage.setRGB(i, j, rgb);
			}
		}

		return histImage;
	}
}

