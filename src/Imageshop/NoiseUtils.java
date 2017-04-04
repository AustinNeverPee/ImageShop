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

public class NoiseUtils {
	/**
	 * 按照比例产生椒盐噪点
	 * 
	 * @param image
	 * @param percent
	 * @return
	 */
	public Image makeNoiseSP(BufferedImage image, double sp, double pp) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 生成随机数
		Random rand = new Random();

		if (sp == 0) {
			// 选择像素点按照概率来变为噪点
			for (int i = image.getMinY(); i < imageHeight; i++) {
				for (int j = image.getMinX(); j < imageWidth; j++) {
					double tmp = rand.nextDouble();
					if (tmp < pp) {
						// set black
						image.setRGB(j, i, new Color(0, 0, 0).getRGB());
					}
				}
			}
		} else if (pp == 0) {
			// 选择像素点按照概率来变为噪点
			for (int i = image.getMinY(); i < imageHeight; i++) {
				for (int j = image.getMinX(); j < imageWidth; j++) {
					double tmp = rand.nextDouble();
					if (tmp < sp) {
						// set white
						image.setRGB(j, i, new Color(255, 255, 255).getRGB());
					}
				}
			}
		} else {
			double P = pp / (1 - sp);

			// 选择像素点按照概率来变为噪点
			for (int i = image.getMinY(); i < imageHeight; i++) {
				for (int j = image.getMinX(); j < imageWidth; j++) {
					double tmp = rand.nextDouble();
					if (tmp < sp) {
						// set white
						image.setRGB(j, i, new Color(255, 255, 255).getRGB());
					} else {
						double tmp2 = rand.nextDouble();
						if (tmp2 < P) {
							// set black
							image.setRGB(j, i, new Color(0, 0, 0).getRGB());
						}
					}
				}
			}
		}

		return image;
	}

	/**
	 * 根据平均值和标准差产生高斯噪点 使用Box-Muller方法
	 * 
	 * @param image
	 * @param mean
	 * @param variance
	 * @return
	 */
	public Image makeNoiseGaussian(BufferedImage image, double mean,
			double variance) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 生成正态分布随机变量加到原图中生成噪声图
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 生成随机数
				Random ran = new Random();
				double r1 = ran.nextDouble();
				double r2 = ran.nextDouble();
				double noise = mean
						+ (Math.sqrt((-2) * Math.log(r2)) * Math.sin(2
								* Math.PI * r1)) * variance;

				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);
				// 取出像素的灰度值
				int gray = image.getColorModel().getRed(data);

				// 加噪声
				gray += noise;

				if (gray > 255) {
					gray = 255;
				} else if (gray < 0) {
					gray = 0;
				}

				image.setRGB(j, i, new Color(gray, gray, gray).getRGB());
			}
		}

		return image;
	}
}

