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

public class ImageScaling {
	/**
	 * 最临近插值算法（等距离采样法） 按给定大小对图片进行放大或缩小
	 * 
	 * @param image
	 *            原始图片
	 * @param width
	 *            给定宽度
	 * @param height
	 *            给定高度
	 * @return
	 */
	public Image scale(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 计算缩放比例
		float k1 = (float) width / imageWidth;
		float k2 = (float) height / imageHeight;
		// 计算采样的行、列间距
		float ii = 1 / k1;
		float jj = 1 / k2;

		// 获取图片类型
		int imgType = image.getType();
		// 获取原图片r,g,b,存储在二维矩阵中
		int[][] green = new int[imageWidth][imageHeight];
		int[][] red = new int[imageWidth][imageHeight];
		int[][] blue = new int[imageWidth][imageHeight];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值
				red[i][j] = image.getColorModel().getRed(data);
				blue[i][j] = image.getColorModel().getBlue(data);
				green[i][j] = image.getColorModel().getGreen(data);
			}
		}

		BufferedImage scaleImage = new BufferedImage(width, height, imgType);
		int rgb, pixelRed, pixelGreen, pixelBlue;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// 为新图片像素值赋值
				pixelRed = red[(int) (i * ii)][(int) (j * jj)];
				pixelGreen = green[(int) (i * ii)][(int) (j * jj)];
				pixelBlue = blue[(int) (i * ii)][(int) (j * jj)];

				// 将r、g、b再转化为rgb值
				// 因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb
				// rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
				rgb = (pixelRed * 256 + pixelGreen) * 256 + pixelBlue;
				if (rgb > 8388608) {
					rgb = rgb - 16777216;
				}

				// 将rgb值写回图片
				scaleImage.setRGB(i, j, rgb);
			}
		}
		return scaleImage;
	}

	/**
	 * 双线性差值放大与均值缩小 按给定大小对图片进行放大或缩小
	 * 
	 * @param image
	 *            原始图片
	 * @param width
	 *            给定宽度
	 * @param height
	 *            给定高度
	 * @return
	 */
	public Image scale2(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 计算缩放比例
		double k1 = (float) width / imageWidth;
		double k2 = (float) height / imageHeight;
		// 计算采样的行、列间距
		double ii = 1 / k1;
		double jj = 1 / k2;

		// 获取图片类型
		int imgType = image.getType();
		// 获取原图片r,g,b,存储在二维矩阵中
		int[][] green = new int[imageWidth][imageHeight];
		int[][] red = new int[imageWidth][imageHeight];
		int[][] blue = new int[imageWidth][imageHeight];
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值
				red[i][j] = image.getColorModel().getRed(data);
				blue[i][j] = image.getColorModel().getBlue(data);
				green[i][j] = image.getColorModel().getGreen(data);
			}
		}

		BufferedImage scaleImage = new BufferedImage(width, height, imgType);
		int rgb, pixelRed, pixelGreen, pixelBlue;
		// 根据双线性插值公式进行计算
		// f(i+u,j+v) = (1-u)(1-v)f(i,j) + (1-u)vf(i,j+1) + u(1-v)f(i+1,j) +
		// uvf(i+1,j+1)
		// 计算得到的坐标
		double srcRow, srcCol;
		// 坐标的整数部分
		int I, J;
		// 坐标小数部分
		double U, V;
		// 四个相邻像素点的权重系数
		double coffiecient1, coffiecient2, coffiecient3, coffiecient4;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// 计算并分离坐标整数、小数部分，计算4个权重系数
				srcRow = i * ii;
				srcCol = j * jj;
				I = (int) Math.floor(srcRow);
				J = (int) Math.floor(srcCol);
				U = srcRow - I;
				V = srcCol - J;
				coffiecient1 = (1.0d - U) * (1.0d - V);
				coffiecient2 = (1.0d - U) * V;
				coffiecient3 = U * (1.0d - V);
				coffiecient4 = U * V;

				if (I + 1 == imageWidth || J + 1 == imageHeight) { // 如果像素点位于边缘，则直接对像素进行赋值
					pixelRed = red[I][J];
					pixelGreen = green[I][J];
					pixelBlue = blue[I][J];
				} else { // 如果像素点不位于边缘位置，则根据线性插值公式为新图片像素值赋值
					pixelRed = (int) (coffiecient1 * red[I][J] + coffiecient2
							* red[I][J + 1] + coffiecient3 * red[I + 1][J] + coffiecient4
							* red[I + 1][J + 1]);
					pixelGreen = (int) (coffiecient1 * green[I][J]
							+ coffiecient2 * green[I][J + 1] + coffiecient3
							* green[I + 1][J] + coffiecient4
							* green[I + 1][J + 1]);
					pixelBlue = (int) (coffiecient1 * blue[I][J] + coffiecient2
							* blue[I][J + 1] + coffiecient3 * blue[I + 1][J] + coffiecient4
							* blue[I + 1][J + 1]);
				}
				// 将r、g、b再转化为rgb值
				// 因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb
				// rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
				rgb = (pixelRed * 256 + pixelGreen) * 256 + pixelBlue;
				if (rgb > 8388608) {
					rgb = rgb - 16777216;
				}

				// 将rgb值写回图片
				scaleImage.setRGB(i, j, rgb);
			}
		}
		return scaleImage;
	}

	/**
	 * 双立方插值算法放缩 按给定大小对图片进行放大或缩小
	 * 
	 * @param image
	 *            原始图片
	 * @param width
	 *            给定宽度
	 * @param height
	 *            给定高度
	 * @return
	 */
	public Image scale3(BufferedImage image, int width, int height) {
		// To Do!
		// 完成双立方差值算法
		return (Image) null;
	}

}

