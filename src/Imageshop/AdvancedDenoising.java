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

public class AdvancedDenoising {
	public Image MarkovDenoise(BufferedImage image) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的灰度值
		// 如果灰度值为0，则赋值-1；如果灰度值为255，则赋值为1
		int gray;
		int[][] noiseGray = new int[imageHeight][imageWidth];
		int[][] newGray = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的灰度值
				gray = image.getColorModel().getRed(data);

				if (gray == 0) {
					noiseGray[i][j] = -1;
					newGray[i][j] = -1;
				} else if (gray == 255) {
					noiseGray[i][j] = 1;
					newGray[i][j] = 1;
				}
			}
		}

		// 定义马尔可夫随机场能量公式的三个系数
		double c1 = 0;
		double c2 = 1.0;
		double c3 = 2.1;

		// 定义像素值为-1和1时的两种能量值
		double E1, E2;

		// 定义八邻域之和
		int neighbour8;

		// 定义突跳的概率
		int p = 1;
		int P = 1000;

		// 定义突跳概率减小的比例
		int decrease = 1;

		// 按照公式更新降噪图像灰度值矩阵，使用模拟退火算法
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 计算八邻域之和
				neighbour8 = 0;
				for (int h = -1; h < 2; h++) {
					for (int w = -1; w < 2; w++) {
						int col = i + h;
						int row = j + w;

						if ((i != 0 && j != 0) && col >= 0 && col < imageHeight
								&& row >= 0 && row < imageWidth) {
							neighbour8 += newGray[col][row];
						}
					}
				}

				// 计算两个能量值
				E1 = c1 * 1 - c2 * 1 * neighbour8 - c3 * 1 * noiseGray[i][j];
				E2 = c1 * (-1) - c2 * (-1) * neighbour8 - c3 * (-1)
						* noiseGray[i][j];

				// 比较两个能力值，选择能量小的作为新的像素值
				if (E1 < E2) {
					newGray[i][j] = 1;
				} else if (E1 > E2) {
					newGray[i][j] = -1;
				}

				// 随机退回到之前操作过的任意一点重新开始
				Random randomProb = new Random();
				// 突跳概率逐渐减小
				P += decrease;
				int prob = randomProb.nextInt(P);
				if (prob < p) {
					// 生成随机数
					Random randomCol = new Random();
					Random randomRow = new Random();
					int col = randomCol.nextInt(i + 1);
					int row = randomRow.nextInt(j + 1);
					i = col;
					j = row - 1;
				}
			}
		}

		// 通过新的像素值矩阵生成新的图像
		BufferedImage MarkovImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_BINARY);
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				if (newGray[i][j] == 1) {
					MarkovImage.setRGB(j, i, new Color(255, 255, 255).getRGB());
				} else if (newGray[i][j] == -1) {
					MarkovImage.setRGB(j, i, new Color(0, 0, 0).getRGB());
				}
			}
		}

		return MarkovImage;
	}
}

