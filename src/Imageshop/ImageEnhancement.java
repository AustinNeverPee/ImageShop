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

public class ImageEnhancement {
	/**
	 * 在空间域对图像做算数均值滤波 也就是之前的averagingFilter
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image ArithmeticMeanFilter(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的灰度值
		int[][] gray = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				gray[i][j] = image.getColorModel().getRed(data);
			}
		}

		// 通过滤波生成新图像
		BufferedImage amfImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int graySum;
		int size = width * height;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				graySum = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							graySum += gray[y][x];
						}
					}
				}
				graySum /= size;

				amfImage.setRGB(j, i,
						new Color(graySum, graySum, graySum).getRGB());
			}
		}

		return amfImage;
	}

	/**
	 * 在空间域对图像做几何均值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image GeometricMeanFilter(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的灰度值
		int[][] gray = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出像素的灰度值
				gray[i][j] = image.getColorModel().getRed(data);
			}
		}

		// 通过滤波生成新图像
		BufferedImage gmfImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		double graySum;
		int size;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				graySum = 1;
				size = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							graySum *= gray[y][x];
							size++;
						}
					}
				}
				graySum = Math.pow(graySum, 1.0 / size);

				gmfImage.setRGB(j, i, new Color((int) graySum, (int) graySum,
						(int) graySum).getRGB());
			}
		}

		return gmfImage;
	}

	/**
	 * 在空间域对图像做调和均值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image HarmonicMeanFilter(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的灰度值
		int[][] gray = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				gray[i][j] = image.getColorModel().getRed(data);
			}
		}

		// 通过滤波生成新图像
		BufferedImage hmfImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		double graySum;
		int size;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				graySum = 0;
				size = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth && gray[y][x] != 0) {
							graySum += 1.0 / gray[y][x];
							size++;
						}
					}
				}
				graySum = size / graySum;
				if (graySum > 255) {
					graySum = 255;
				}

				hmfImage.setRGB(j, i, new Color((int) graySum, (int) graySum,
						(int) graySum).getRGB());
			}
		}

		return hmfImage;
	}

	/**
	 * 在空间域对图像做反调和均值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image ContraharmonicMeanFilter(BufferedImage image, int width,
			int height, double Q) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的灰度值
		int[][] gray = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				gray[i][j] = image.getColorModel().getRed(data);
			}
		}

		// 通过滤波生成新图像
		BufferedImage cmfImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		double graySum, graySum1;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				graySum = graySum1 = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							graySum += Math.pow(gray[y][x], -0.5);
							graySum1 += Math.pow(gray[y][x], -1.5);
						}
					}
				}
				if (graySum != 0) {
					graySum /= graySum1;
				} else {
					graySum = 255;
				}

				if (graySum > 255) {
					graySum = 255;
				}

				cmfImage.setRGB(j, i, new Color((int) graySum, (int) graySum,
						(int) graySum).getRGB());
			}
		}

		return cmfImage;
	}
}


