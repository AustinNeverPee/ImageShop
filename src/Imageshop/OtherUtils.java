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

public class OtherUtils {
	/**
	 * 根据所给patch的大小，将图像灰度矩阵分成许多个patch， 随机返回8个patches
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public int[][][] view_as_window(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int[][] grey = new int[imageHeight][imageWidth];

		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				grey[i][j] = image.getColorModel().getRed(data);
			}
		}

		int[][][] arr_patch = new int[8][height][width];
		int patchNumber = (imageHeight - height + 1) * (imageWidth - width + 1);
		// 随机返回8个patch
		Vector<Integer> patchRandom = new Vector<Integer>(8);
		int pr;
		for (int i = 0; i < 8; i++) {
			while (true) {
				Random r = new Random();
				pr = Math.abs(r.nextInt()) % patchNumber;

				// 判断是否存在和之前元素重复
				boolean flag = false;
				for (int j = 0; j < i; j++) {
					if (pr == patchRandom.get(j)) {
						flag = true;
						break;
					}
				}

				if (!flag) {
					patchRandom.add(pr);
					break;
				}
			}
		}

		// 根据随机生成的patch序号，添加patch到三维数组中
		int tempNumber = 0;
		int randomNumber = 0;
		for (int i = 0; i <= imageHeight - height; i++) {
			for (int j = 0; j <= imageWidth - width; j++) {
				tempNumber++;
				if (patchRandom.indexOf(Integer.valueOf(tempNumber)) != -1) {
					patchRandom.remove(patchRandom.indexOf(Integer
							.valueOf(tempNumber)));
					for (int m = 0; m < height; m++) {
						for (int n = 0; n < width; n++) {
							arr_patch[randomNumber][m][n] = grey[i + m][j + n];
						}
					}

					randomNumber++;
				}
			}
		}

		return arr_patch;
	}

	/**
	 * 计算两张大小一致图片的相似度
	 * 
	 * @param imageFirst
	 * @return similarity
	 */
	public double computeSimilarity(BufferedImage imageFirst,
			BufferedImage imageSecond) {
		// 定义相似度
		double similarity;

		// 定义相似点个数
		double count = 0;

		// 获取图像大小
		int imageWidth = imageFirst.getWidth();
		int imageHeight = imageFirst.getHeight();

		// 定义两张图象每个点RGB的值
		int red1, green1, blue1, red2, green2, blue2;
		for (int i = imageFirst.getMinY(); i < imageHeight; i++) {
			for (int j = imageFirst.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data1 = imageFirst.getRaster().getDataElements(j, i,
						null);
				Object data2 = imageSecond.getRaster().getDataElements(j, i,
						null);

				// 分别取出两张图象每个像素的r、g、b值
				red1 = imageFirst.getColorModel().getRed(data1);
				green1 = imageFirst.getColorModel().getGreen(data1);
				blue1 = imageFirst.getColorModel().getBlue(data1);
				red2 = imageSecond.getColorModel().getRed(data2);
				green2 = imageSecond.getColorModel().getGreen(data2);
				blue2 = imageSecond.getColorModel().getBlue(data2);

				if (red1 == red2 && green1 == green2 && blue1 == blue2) {
					count++;
				}
			}
		}

		similarity = count / (imageHeight * imageWidth) * 100;

		return similarity;
	}

	/**
	 * 顺时针或逆时针旋转图像90°
	 * 
	 * @param image
	 * @param flag
	 * @return
	 */
	public Image Rotate(BufferedImage image, int flag) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点RGB的值
		int[][] red = new int[imageHeight][imageWidth];
		int[][] green = new int[imageHeight][imageWidth];
		int[][] blue = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red[i][j] = image.getColorModel().getRed(data);
				green[i][j] = image.getColorModel().getGreen(data);
				blue[i][j] = image.getColorModel().getBlue(data);
			}
		}

		BufferedImage rotateImg = new BufferedImage(imageHeight, imageWidth,
				image.getType());
		if (flag == 1) {
			// 顺时针旋转90°
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					rotateImg.setRGB(imageHeight - 1 - i, j, new Color(
							red[i][j], green[i][j], blue[i][j]).getRGB());
				}
			}
		} else {
			// 逆时针旋转90°
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					rotateImg.setRGB(i, imageWidth - 1 - j, new Color(
							red[i][j], green[i][j], blue[i][j]).getRGB());
				}
			}
		}

		return rotateImg;
	}

	/**
	 * 水平或竖直翻转图像
	 * 
	 * @param image
	 * @param flag
	 * @return
	 */
	public Image Flip(BufferedImage image, int flag) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点RGB的值
		int[][] red = new int[imageHeight][imageWidth];
		int[][] green = new int[imageHeight][imageWidth];
		int[][] blue = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				red[i][j] = image.getColorModel().getRed(data);
				green[i][j] = image.getColorModel().getGreen(data);
				blue[i][j] = image.getColorModel().getBlue(data);
			}
		}

		BufferedImage flipImg = new BufferedImage(imageHeight, imageWidth,
				image.getType());
		if (flag == 1) {
			// 水平翻转
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					flipImg.setRGB(imageWidth - 1 - j, i, new Color(
							red[i][j], green[i][j], blue[i][j]).getRGB());
				}
			}
		} else {
			// 竖直翻转
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					flipImg.setRGB(j, imageHeight - 1 - i, new Color(
							red[i][j], green[i][j], blue[i][j]).getRGB());
				}
			}
		}

		return flipImg;
	}
}

