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

public class HazeRemoval {
	/**
	 * 图像去雾 没有使用Soft Matting
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image WSMHazeRemoval(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的R、G、B三值
		int[][] Ired = new int[imageHeight][imageWidth];
		int[][] Igreen = new int[imageHeight][imageWidth];
		int[][] Iblue = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				Ired[i][j] = image.getColorModel().getRed(data);
				Igreen[i][j] = image.getColorModel().getGreen(data);
				Iblue[i][j] = image.getColorModel().getBlue(data);
			}
		}

		// 计算图像的暗通道
		int min;
		int[][] Idark = new int[imageHeight][imageWidth];
		int[] Idark1D = new int[imageHeight * imageWidth];
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				min = 255;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							min = Ired[y][x] < min ? Ired[y][x] : min;
							min = Igreen[y][x] < min ? Igreen[y][x] : min;
							min = Iblue[y][x] < min ? Iblue[y][x] : min;
						}
					}
				}

				Idark[i][j] = min;
				Idark1D[i * imageWidth + j] = min;
			}
		}

		// 通过暗通道计算A
		Arrays.sort(Idark1D);
		int Anum = (int) (imageHeight * imageWidth * 0.001);
		int darkSum = 0;
		for (int i = 1; i <= Anum; i++) {
			darkSum += Idark1D[imageHeight * imageWidth - Anum];
		}
		double A = ((double) darkSum) / Anum;
		A = A < 220 ? A : 220;

		// 计算 Transmission
		double w = 0.95;
		double[][] t = new double[imageHeight][imageWidth];
		double t_max = 0, t_min = 255;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = 1 - w * Idark[i][j] / A;

				if (t[i][j] > t_max) {
					t_max = t[i][j];
				}
				if (t[i][j] < t_min) {
					t_min = t[i][j];
				}
			}
		}
		// 标定t
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = (t[i][j] - t_min) / (t_max - t_min);
			}
		}

		// 计算去雾之后图像的R、G、B三值
		BufferedImage hazeFreeImage = new BufferedImage(imageWidth,
				imageHeight, image.getType());
		double[][] Jred = new double[imageHeight][imageWidth];
		double[][] Jgreen = new double[imageHeight][imageWidth];
		double[][] Jblue = new double[imageHeight][imageWidth];
		double Jred_max = 0, Jred_min = 255;
		double Jgreen_max = 0, Jgreen_min = 255;
		double Jblue_max = 0, Jblue_min = 255;
		double t0 = 0.4;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				Jred[i][j] = (Ired[i][j] - A) / max(t[i][j], t0) + A;
				Jgreen[i][j] = (Igreen[i][j] - A) / max(t[i][j], t0) + A;
				Jblue[i][j] = (Iblue[i][j] - A) / max(t[i][j], t0) + A;

				if (Jred[i][j] > Jred_max) {
					Jred_max = Jred[i][j];
				}
				if (Jred[i][j] < Jred_min) {
					Jred_min = Jred[i][j];
				}
				if (Jgreen[i][j] > Jgreen_max) {
					Jgreen_max = Jgreen[i][j];
				}
				if (Jgreen[i][j] < Jgreen_min) {
					Jgreen_min = Jgreen[i][j];
				}
				if (Jblue[i][j] > Jblue_max) {
					Jblue_max = Jblue[i][j];
				}
				if (Jblue[i][j] < Jblue_min) {
					Jblue_min = Jblue[i][j];
				}
			}
		}

		// 生成无雾新图像
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				// 标定
				Jred[i][j] = 255 * (Jred[i][j] - Jred_min)
						/ (Jred_max - Jred_min);
				Jgreen[i][j] = 255 * (Jgreen[i][j] - Jgreen_min)
						/ (Jgreen_max - Jgreen_min);
				Jblue[i][j] = 255 * (Jblue[i][j] - Jblue_min)
						/ (Jblue_max - Jblue_min);

				hazeFreeImage.setRGB(j, i, new Color((int) Jred[i][j],
						(int) Jgreen[i][j], (int) Jblue[i][j]).getRGB());
			}
		}

		return hazeFreeImage;
	}

	/**
	 * 图像去雾 使用Guided Image Filtering
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image GIFHazeRemoval(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的R、G、B三值，同时获取灰度矩阵
		int[][] Ired = new int[imageHeight][imageWidth];
		int[][] Igreen = new int[imageHeight][imageWidth];
		int[][] Iblue = new int[imageHeight][imageWidth];
		double[][] Igray = new double[imageHeight][imageWidth];
		double Igray_max = 0, Igray_min = 255;
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				Ired[i][j] = image.getColorModel().getRed(data);
				Igreen[i][j] = image.getColorModel().getGreen(data);
				Iblue[i][j] = image.getColorModel().getBlue(data);

				Igray[i][j] = Ired[i][j] * 0.3 + Igreen[i][j] * 0.59
						+ Iblue[i][j] * 0.11;
				if (Igray[i][j] > Igray_max) {
					Igray_max = Igray[i][j];
				}
				if (Igray[i][j] < Igray_min) {
					Igray_min = Igray[i][j];
				}
			}
		}

		// 将Igray标定在0-1之间
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				Igray[i][j] = (Igray[i][j] - Igray_min)
						/ (Igray_max - Igray_min);
			}
		}

		// 计算图像的暗通道
		int min;
		int[][] Idark = new int[imageHeight][imageWidth];
		int[] Idark1D = new int[imageHeight * imageWidth];
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				min = 255;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							min = Ired[y][x] < min ? Ired[y][x] : min;
							min = Igreen[y][x] < min ? Igreen[y][x] : min;
							min = Iblue[y][x] < min ? Iblue[y][x] : min;
						}
					}
				}

				Idark[i][j] = min;
				Idark1D[i * imageWidth + j] = min;
			}
		}

		// 通过暗通道计算A
		Arrays.sort(Idark1D);
		int Anum = (int) (imageHeight * imageWidth * 0.001);
		int darkSum = 0;
		for (int i = 1; i <= Anum; i++) {
			darkSum += Idark1D[imageHeight * imageWidth - Anum];
		}
		double A = ((double) darkSum) / Anum;
		// A = A < 220 ? A : 220;

		// 估计 Transmission
		double w = 0.95;
		double[][] t = new double[imageHeight][imageWidth];
		double t_max = 0, t_min = 255;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = 1 - w * Idark[i][j] / A;

				if (t[i][j] > t_max) {
					t_max = t[i][j];
				}
				if (t[i][j] < t_min) {
					t_min = t[i][j];
				}
			}
		}
		// 标定t
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = (t[i][j] - t_min) / (t_max - t_min);
			}
		}

		// Refine Transmission
		// Guided Image Filtering
		double i_mean, p_mean, i_corr, ip_corr, i_var, ip_cov;
		double i_mean_sum, p_mean_sum, i_corr_sum, ip_corr_sum, a_mean_sum, b_mean_sum;
		double[][] a = new double[imageHeight][imageWidth];
		double[][] b = new double[imageHeight][imageWidth];
		double[][] a_mean = new double[imageHeight][imageWidth];
		double[][] b_mean = new double[imageHeight][imageWidth];
		int count;
		double eps = 0.001;
		Boolean first_flag;

		for (int i = 0; i < imageHeight; i++) {
			first_flag = true;
			i_mean_sum = p_mean_sum = i_corr_sum = ip_corr_sum = 0;
			count = 0;
			for (int j = 0; j < imageWidth; j++) {
				if (first_flag == true) {
					for (int m = 0; m < height * 6; m++) {
						for (int n = 0; n < width * 6; n++) {
							// Zero Padding
							int y = i + m - height / 2;
							int x = j + n - width / 2;
							if (y >= 0 && y < imageHeight && x >= 0
									&& x < imageWidth) {
								i_mean_sum += Igray[y][x];
								p_mean_sum += t[y][x];
								i_corr_sum += Igray[y][x] * Igray[y][x];
								ip_corr_sum += Igray[y][x] * t[y][x];
								count++;
							}
						}
					}

					first_flag = false;
				} else {
					// 加新到达的最后一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + (width * 6 - 1) - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							i_mean_sum += Igray[y][x];
							p_mean_sum += t[y][x];
							i_corr_sum = Igray[y][x] * Igray[y][x];
							ip_corr_sum = Igray[y][x] * t[y][x];
							count++;
						}
					}

					// 减原来的第一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j - 2 - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							i_mean_sum -= Igray[y][x];
							p_mean_sum -= t[y][x];
							i_corr_sum -= Igray[y][x] * Igray[y][x];
							ip_corr_sum -= Igray[y][x] * t[y][x];
							count--;
						}
					}
				}

				// Step 1
				i_mean = i_mean_sum / count;
				p_mean = p_mean_sum / count;
				i_corr = i_corr_sum / count;
				ip_corr = ip_corr_sum / count;

				// Step 2
				i_var = i_corr - Math.pow(i_mean, 2);
				ip_cov = ip_corr - i_mean * p_mean;

				// Step 3
				a[i][j] = ip_cov / (i_var + eps);
				b[i][j] = p_mean - a[i][j] * i_mean;
			}
		}

		// Step 4
		for (int i = 0; i < imageHeight; i++) {
			first_flag = true;
			a_mean_sum = b_mean_sum = 0;
			count = 0;
			for (int j = 0; j < imageWidth; j++) {
				if (first_flag == true) {
					for (int m = 0; m < height * 6; m++) {
						for (int n = 0; n < width * 6; n++) {
							// Zero Padding
							int y = i + m - height / 2;
							int x = j + n - width / 2;
							if (y >= 0 && y < imageHeight && x >= 0
									&& x < imageWidth) {
								a_mean_sum += a[y][x];
								b_mean_sum += b[y][x];
								count++;
							}
						}
					}

					first_flag = false;
				} else {
					// 加新到达的最后一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + (width * 6 - 1) - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							a_mean_sum += a[y][x];
							b_mean_sum += b[y][x];
							count++;
						}
					}

					// 减原来的第一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j - 2 - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							a_mean_sum -= a[y][x];
							b_mean_sum -= b[y][x];
							count--;
						}
					}
				}

				a_mean[i][j] = a_mean_sum / count;
				b_mean[i][j] = b_mean_sum / count;
			}
		}

		// Step 5
		double[][] q = new double[imageHeight][imageWidth];
		double q_max = 0, q_min = 255;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				q[i][j] = a_mean[i][j] * Igray[i][j] + b_mean[i][j];

				if (q[i][j] > q_max) {
					q_max = q[i][j];
				}
				if (q[i][j] < q_min) {
					q_min = q[i][j];
				}
			}
		}
		// 对q标定
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				q[i][j] = (q[i][j] - q_min) / (q_max - q_min);
			}
		}

		// 计算去雾之后图像的R、G、B三值
		BufferedImage hazeFreeImage = new BufferedImage(imageWidth,
				imageHeight, image.getType());
		double[][] Jred = new double[imageHeight][imageWidth];
		double[][] Jgreen = new double[imageHeight][imageWidth];
		double[][] Jblue = new double[imageHeight][imageWidth];
		double Jred_max = 0, Jred_min = 255;
		double Jgreen_max = 0, Jgreen_min = 255;
		double Jblue_max = 0, Jblue_min = 255;
		double q0 = 0.4;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				Jred[i][j] = (Ired[i][j] - A) / max(q[i][j], q0) + A;
				Jgreen[i][j] = (Igreen[i][j] - A) / max(q[i][j], q0) + A;
				Jblue[i][j] = (Iblue[i][j] - A) / max(q[i][j], q0) + A;

				if (Jred[i][j] > Jred_max) {
					Jred_max = Jred[i][j];
				}
				if (Jred[i][j] < Jred_min) {
					Jred_min = Jred[i][j];
				}
				if (Jgreen[i][j] > Jgreen_max) {
					Jgreen_max = Jgreen[i][j];
				}
				if (Jgreen[i][j] < Jgreen_min) {
					Jgreen_min = Jgreen[i][j];
				}
				if (Jblue[i][j] > Jblue_max) {
					Jblue_max = Jblue[i][j];
				}
				if (Jblue[i][j] < Jblue_min) {
					Jblue_min = Jblue[i][j];
				}
			}
		}

		// 生成无雾新图像
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				// 标定
				Jred[i][j] = 255 * (Jred[i][j] - Jred_min)
						/ (Jred_max - Jred_min);
				Jgreen[i][j] = 255 * (Jgreen[i][j] - Jgreen_min)
						/ (Jgreen_max - Jgreen_min);
				Jblue[i][j] = 255 * (Jblue[i][j] - Jblue_min)
						/ (Jblue_max - Jblue_min);

				hazeFreeImage.setRGB(j, i, new Color((int) Jred[i][j],
						(int) Jgreen[i][j], (int) Jblue[i][j]).getRGB());
			}
		}

		return hazeFreeImage;
	}

	/**
	 * 输出Basic中透射率图
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image BTransmission(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的R、G、B三值，同时获取灰度矩阵
		int[][] Ired = new int[imageHeight][imageWidth];
		int[][] Igreen = new int[imageHeight][imageWidth];
		int[][] Iblue = new int[imageHeight][imageWidth];
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				Ired[i][j] = image.getColorModel().getRed(data);
				Igreen[i][j] = image.getColorModel().getGreen(data);
				Iblue[i][j] = image.getColorModel().getBlue(data);
			}
		}

		// // 计算图像的暗通道
		// int min;
		// DarkChannel[] Idark = new DarkChannel[imageHeight * imageWidth];
		// for (int i = 0; i < imageHeight; i++) {
		// for (int j = 0; j < imageWidth; j++) {
		// min = 255;
		// for (int m = 0; m < height; m++) {
		// for (int n = 0; n < width; n++) {
		// // Zero Padding
		// int y = i + m - height / 2;
		// int x = j + n - width / 2;
		// if (y >= 0 && y < imageHeight && x >= 0
		// && x < imageWidth) {
		// min = Ired[y][x] < min ? Ired[y][x] : min;
		// min = Igreen[y][x] < min ? Igreen[y][x] : min;
		// min = Iblue[y][x] < min ? Iblue[y][x] : min;
		// }
		// }
		// }
		//
		// DarkChannel dc = new DarkChannel(i, j, min);
		// Idark[i * imageWidth + j] = dc;
		// }
		// }
		//
		// // 通过暗通道计算A
		// // 对Idark冒泡排序
		// for (int i = 1; i < Idark.length; i++) {
		// for (int j = 0; j < Idark.length - i; j++) {
		// if (Idark[j].value < Idark[j + 1].value) {
		// DarkChannel tmp = Idark[j];
		// Idark[j] = Idark[j + 1];
		// Idark[j + 1] = tmp;
		// }
		// }
		// }
		// // 取得暗通道中前0.001中最亮点的下标
		// int Anum = (int) (imageHeight * imageWidth * 0.001);
		// int[] brightest_x = new int[Anum];
		// int[] brightest_y = new int[Anum];
		// for (int i = 0; i < Anum; i++) {
		// brightest_x[i] = Idark[i].x;
		// brightest_y[i] = Idark[i].y;
		// }
		// // 获取暗通道中前0.001中最亮点中强度最大点的下标
		// int max_x = 0, max_y = 0;
		// int max_intensity = 0;
		// for (int i = 0; i < Anum; i++) {
		// int intensity = (Ired[brightest_x[i]][brightest_y[i]]
		// + Igreen[brightest_x[i]][brightest_y[i]] +
		// Iblue[brightest_x[i]][brightest_y[i]]) / 3;
		// if (intensity > max_intensity) {
		// max_intensity = intensity;
		// max_x = brightest_x[i];
		// max_y = brightest_y[i];
		// }
		// }
		// // 获取三个通道的A
		// int A_red = Ired[max_x][max_y];
		// int A_green = Igreen[max_x][max_y];
		// int A_blue = Iblue[max_x][max_y];
		//
		// // 估计 Transmission
		// double w = 0.95;
		// double[][] t = new double[imageHeight][imageWidth];
		// double min_div_a, red_div_a, green_div_a, blue_div_a;
		// double t_max = 0, t_min = 9999;
		// for (int i = 0; i < imageHeight; i++) {
		// for (int j = 0; j < imageWidth; j++) {
		// min_div_a = 9999;
		// for (int m = 0; m < height; m++) {
		// for (int n = 0; n < width; n++) {
		// // Zero Padding
		// int y = i + m - height / 2;
		// int x = j + n - width / 2;
		// if (y >= 0 && y < imageHeight && x >= 0
		// && x < imageWidth) {
		// red_div_a = (double) Ired[y][x] / A_red;
		// green_div_a = (double) Igreen[y][x] / A_green;
		// blue_div_a = (double) Iblue[y][x] / A_blue;
		//
		// min_div_a = red_div_a < min_div_a ? red_div_a : min_div_a;
		// min_div_a = green_div_a < min_div_a ? green_div_a : min_div_a;
		// min_div_a = blue_div_a < min_div_a ? blue_div_a : min_div_a;
		// }
		// }
		// }
		//
		// t[i][j] = 1 - w * min_div_a;
		//
		// if (t[i][j] > t_max) {
		// t_max = t[i][j];
		// }
		// if (t[i][j] < t_min) {
		// t_min = t[i][j];
		// }
		// }
		// }

		// 计算图像的暗通道
		int min;
		int[][] Idark = new int[imageHeight][imageWidth];
		int[] Idark1D = new int[imageHeight * imageWidth];
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				min = 255;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							min = Ired[y][x] < min ? Ired[y][x] : min;
							min = Igreen[y][x] < min ? Igreen[y][x] : min;
							min = Iblue[y][x] < min ? Iblue[y][x] : min;
						}
					}
				}

				Idark[i][j] = min;
				Idark1D[i * imageWidth + j] = min;
			}
		}

		// 通过暗通道计算A
		Arrays.sort(Idark1D);
		int Anum = (int) (imageHeight * imageWidth * 0.001);
		int darkSum = 0;
		for (int i = 1; i <= Anum; i++) {
			darkSum += Idark1D[imageHeight * imageWidth - Anum];
		}
		double A = ((double) darkSum) / Anum;
		A = A < 220 ? A : 220;

		// 计算 Transmission
		double w = 0.95;
		double[][] t = new double[imageHeight][imageWidth];
		double t_max = 0, t_min = 255;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = 1 - w * Idark[i][j] / A;

				if (t[i][j] > t_max) {
					t_max = t[i][j];
				}
				if (t[i][j] < t_min) {
					t_min = t[i][j];
				}
			}
		}
		// 标定t
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = 255 * (t[i][j] - t_min) / (t_max - t_min);
			}
		}

		BufferedImage transmissionImage = new BufferedImage(imageWidth,
				imageHeight, BufferedImage.TYPE_BYTE_GRAY);
		// 生成透射率图像
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				transmissionImage.setRGB(j, i, new Color((int) t[i][j],
						(int) t[i][j], (int) t[i][j]).getRGB());
			}
		}

		return transmissionImage;
	}

	/**
	 * 输出Guided Image Filtering中透射率图
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image GIFTransmission(BufferedImage image, int width, int height) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 获取原图每个点的R、G、B三值，同时获取灰度矩阵
		int[][] Ired = new int[imageHeight][imageWidth];
		int[][] Igreen = new int[imageHeight][imageWidth];
		int[][] Iblue = new int[imageHeight][imageWidth];
		double[][] Igray = new double[imageHeight][imageWidth];
		double Igray_max = 0, Igray_min = 255;
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(j, i, null);

				// 取出某个像素的r、g、b值，对于灰度图来说，三者是一样的
				Ired[i][j] = image.getColorModel().getRed(data);
				Igreen[i][j] = image.getColorModel().getGreen(data);
				Iblue[i][j] = image.getColorModel().getBlue(data);

				Igray[i][j] = Ired[i][j] * 0.3 + Igreen[i][j] * 0.59
						+ Iblue[i][j] * 0.11;
				if (Igray[i][j] > Igray_max) {
					Igray_max = Igray[i][j];
				}
				if (Igray[i][j] < Igray_min) {
					Igray_min = Igray[i][j];
				}
			}
		}

		// 将Igray标定在0-1之间
		for (int i = image.getMinY(); i < imageHeight; i++) {
			for (int j = image.getMinX(); j < imageWidth; j++) {
				Igray[i][j] = (Igray[i][j] - Igray_min)
						/ (Igray_max - Igray_min);
			}
		}

		// 计算图像的暗通道
		int min;
		int[][] Idark = new int[imageHeight][imageWidth];
		int[] Idark1D = new int[imageHeight * imageWidth];
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				min = 255;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							min = Ired[y][x] < min ? Ired[y][x] : min;
							min = Igreen[y][x] < min ? Igreen[y][x] : min;
							min = Iblue[y][x] < min ? Iblue[y][x] : min;
						}
					}
				}

				Idark[i][j] = min;
				Idark1D[i * imageWidth + j] = min;
			}
		}

		// 通过暗通道计算A
		Arrays.sort(Idark1D);
		int Anum = (int) (imageHeight * imageWidth * 0.001);
		int darkSum = 0;
		for (int i = 1; i <= Anum; i++) {
			darkSum += Idark1D[imageHeight * imageWidth - Anum];
		}
		double A = ((double) darkSum) / Anum;
		A = A < 220 ? A : 220;

		// 估计 Transmission
		double w = 0.95;
		double[][] t = new double[imageHeight][imageWidth];
		double t_max = 0, t_min = 255;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = 1 - w * Idark[i][j] / A;

				if (t[i][j] > t_max) {
					t_max = t[i][j];
				}
				if (t[i][j] < t_min) {
					t_min = t[i][j];
				}
			}
		}
		// 标定t
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				t[i][j] = (t[i][j] - t_min) / (t_max - t_min);
			}
		}

		// Refine Transmission
		// Guided Image Filtering
		double i_mean, p_mean, i_corr, ip_corr, i_var, ip_cov;
		double i_mean_sum, p_mean_sum, i_corr_sum, ip_corr_sum, a_mean_sum, b_mean_sum;
		double[][] a = new double[imageHeight][imageWidth];
		double[][] b = new double[imageHeight][imageWidth];
		double[][] a_mean = new double[imageHeight][imageWidth];
		double[][] b_mean = new double[imageHeight][imageWidth];
		int count;
		double eps = 0.001;
		Boolean first_flag;

		for (int i = 0; i < imageHeight; i++) {
			first_flag = true;
			i_mean_sum = p_mean_sum = i_corr_sum = ip_corr_sum = 0;
			count = 0;
			for (int j = 0; j < imageWidth; j++) {
				if (first_flag == true) {
					for (int m = 0; m < height * 6; m++) {
						for (int n = 0; n < width * 6; n++) {
							// Zero Padding
							int y = i + m - height / 2;
							int x = j + n - width / 2;
							if (y >= 0 && y < imageHeight && x >= 0
									&& x < imageWidth) {
								i_mean_sum += Igray[y][x];
								p_mean_sum += t[y][x];
								i_corr_sum += Igray[y][x] * Igray[y][x];
								ip_corr_sum += Igray[y][x] * t[y][x];
								count++;
							}
						}
					}

					first_flag = false;
				} else {
					// 加新到达的最后一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + (width * 6 - 1) - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							i_mean_sum += Igray[y][x];
							p_mean_sum += t[y][x];
							i_corr_sum = Igray[y][x] * Igray[y][x];
							ip_corr_sum = Igray[y][x] * t[y][x];
							count++;
						}
					}

					// 减原来的第一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j - 2 - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							i_mean_sum -= Igray[y][x];
							p_mean_sum -= t[y][x];
							i_corr_sum -= Igray[y][x] * Igray[y][x];
							ip_corr_sum -= Igray[y][x] * t[y][x];
							count--;
						}
					}
				}

				// Step 1
				i_mean = i_mean_sum / count;
				p_mean = p_mean_sum / count;
				i_corr = i_corr_sum / count;
				ip_corr = ip_corr_sum / count;

				// Step 2
				i_var = i_corr - Math.pow(i_mean, 2);
				ip_cov = ip_corr - i_mean * p_mean;

				// Step 3
				a[i][j] = ip_cov / (i_var + eps);
				b[i][j] = p_mean - a[i][j] * i_mean;
			}
		}

		// Step 4
		for (int i = 0; i < imageHeight; i++) {
			first_flag = true;
			a_mean_sum = b_mean_sum = 0;
			count = 0;
			for (int j = 0; j < imageWidth; j++) {
				if (first_flag == true) {
					for (int m = 0; m < height * 6; m++) {
						for (int n = 0; n < width * 6; n++) {
							// Zero Padding
							int y = i + m - height / 2;
							int x = j + n - width / 2;
							if (y >= 0 && y < imageHeight && x >= 0
									&& x < imageWidth) {
								a_mean_sum += a[y][x];
								b_mean_sum += b[y][x];
								count++;
							}
						}
					}

					first_flag = false;
				} else {
					// 加新到达的最后一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + (width * 6 - 1) - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							a_mean_sum += a[y][x];
							b_mean_sum += b[y][x];
							count++;
						}
					}

					// 减原来的第一列
					for (int m = 0; m < height * 6; m++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j - 2 - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							a_mean_sum -= a[y][x];
							b_mean_sum -= b[y][x];
							count--;
						}
					}
				}

				a_mean[i][j] = a_mean_sum / count;
				b_mean[i][j] = b_mean_sum / count;
			}
		}

		// Step 5
		double[][] q = new double[imageHeight][imageWidth];
		double q_max = 0, q_min = 255;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				q[i][j] = a_mean[i][j] * Igray[i][j] + b_mean[i][j];

				if (q[i][j] > q_max) {
					q_max = q[i][j];
				}
				if (q[i][j] < q_min) {
					q_min = q[i][j];
				}
			}
		}
		// 对q标定
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				q[i][j] = 255 * (q[i][j] - q_min) / (q_max - q_min);
			}
		}

		BufferedImage transmissionImage = new BufferedImage(imageWidth,
				imageHeight, BufferedImage.TYPE_BYTE_GRAY);
		// 生成透射率图像
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				transmissionImage.setRGB(j, i, new Color((int) q[i][j],
						(int) q[i][j], (int) q[i][j]).getRGB());
				// transmissionImage.setRGB(j, i, new Color((int)Igray[i][j],
				// (int)Igray[i][j], (int)Igray[i][j]).getRGB());
			}
		}

		return transmissionImage;
	}

	/**
	 * 返回两个数中更大的一个
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	double max(double arg1, double arg2) {
		return arg1 > arg2 ? arg1 : arg2;
	}
}

