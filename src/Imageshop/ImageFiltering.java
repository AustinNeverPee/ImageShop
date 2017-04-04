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

public class ImageFiltering {
	/**
	 * 对图像做平均值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image averagingFilter(BufferedImage image, int width, int height) {
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

		// 通过平均值滤波生成新图像
		BufferedImage averageImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int redSum, greenSum, blueSum, rgb;
		int size = width * height;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += red[y][x];
							greenSum += green[y][x];
							blueSum += blue[y][x];
						}
					}
				}
				redSum /= size;
				greenSum /= size;
				blueSum /= size;
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				averageImage.setRGB(j, i, rgb);
			}
		}

		return averageImage;
	}

	/**
	 * 对图像做中值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image medianFilter(BufferedImage image, int width, int height) {
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

		// 通过中值滤波生成新图像
		BufferedImage medianImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int size = width * height;
		int[] redArr = new int[size];
		int[] greenArr = new int[size];
		int[] blueArr = new int[size];
		int pixelNumber;
		int rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				pixelNumber = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redArr[pixelNumber] = red[y][x];
							greenArr[pixelNumber] = green[y][x];
							blueArr[pixelNumber] = blue[y][x];
						} else {
							redArr[pixelNumber] = 0;
							greenArr[pixelNumber] = 0;
							blueArr[pixelNumber] = 0;
						}
						pixelNumber++;
					}
				}
				Arrays.sort(redArr);
				Arrays.sort(greenArr);
				Arrays.sort(blueArr);

				rgb = (redArr[size / 2] * 256 + greenArr[size / 2]) * 256
						+ blueArr[size / 2];
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				medianImage.setRGB(j, i, rgb);
			}
		}

		return medianImage;
	}

	/**
	 * 对图像做最大值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image maxFilter(BufferedImage image, int width, int height) {
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

		// 通过最大值滤波生成新图像
		BufferedImage maxImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int size = width * height;
		int[] redArr = new int[size];
		int[] greenArr = new int[size];
		int[] blueArr = new int[size];
		int pixelNumber;
		int rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				pixelNumber = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redArr[pixelNumber] = red[y][x];
							greenArr[pixelNumber] = green[y][x];
							blueArr[pixelNumber] = blue[y][x];
						} else {
							redArr[pixelNumber] = 0;
							greenArr[pixelNumber] = 0;
							blueArr[pixelNumber] = 0;
						}
						pixelNumber++;
					}
				}
				Arrays.sort(redArr);
				Arrays.sort(greenArr);
				Arrays.sort(blueArr);

				rgb = (redArr[size - 1] * 256 + greenArr[size - 1]) * 256
						+ blueArr[size - 1];
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				maxImage.setRGB(j, i, rgb);
			}
		}

		return maxImage;
	}

	/**
	 * 对图像做最小值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image minFilter(BufferedImage image, int width, int height) {
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

		// 通过最小值滤波生成新图像
		BufferedImage minImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int size = width * height;
		int[] redArr = new int[size];
		int[] greenArr = new int[size];
		int[] blueArr = new int[size];
		int pixelNumber;
		int rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				pixelNumber = 0;
				for (int m = 0; m < height; m++) {
					for (int n = 0; n < width; n++) {
						// Zero Padding
						int y = i + m - height / 2;
						int x = j + n - width / 2;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redArr[pixelNumber] = red[y][x];
							greenArr[pixelNumber] = green[y][x];
							blueArr[pixelNumber] = blue[y][x];
						} else {
							redArr[pixelNumber] = 0;
							greenArr[pixelNumber] = 0;
							blueArr[pixelNumber] = 0;
						}
						pixelNumber++;
					}
				}
				Arrays.sort(redArr);
				Arrays.sort(greenArr);
				Arrays.sort(blueArr);

				rgb = (redArr[0] * 256 + greenArr[0]) * 256 + blueArr[0];
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				minImage.setRGB(j, i, rgb);
			}
		}

		return minImage;
	}

	/**
	 * 对图像做拉普拉斯锐化法(a) 其filter mask为：0 1 0 1 -4 1 0 1 0
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image laplacianFilterA(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { 0, 1, 0 }, { 1, -4, 1 }, { 0, 1, 0 } };

		// 通过拉普拉斯锐化法（a）生成新图像
		BufferedImage laplacianAImage = new BufferedImage(imageWidth,
				imageHeight, image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum -= (int) (0.2 * red[y][x] * filter[m][n]);
							greenSum -= (int) (0.2 * green[y][x] * filter[m][n]);
							blueSum -= (int) (0.2 * blue[y][x] * filter[m][n]);
						}
					}
				}
				redSum += red[i][j];
				greenSum += green[i][j];
				blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				laplacianAImage.setRGB(j, i, rgb);
			}
		}

		return laplacianAImage;
	}

	/**
	 * 对图像做拉普拉斯锐化法(b) 其filter mask为：1 1 1 1 -8 1 1 1 1
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image laplacianFilterB(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { 1, 1, 1 }, { 1, -8, 1 }, { 1, 1, 1 } };

		// 通过拉普拉斯锐化法（b）生成新图像
		BufferedImage laplacianBImage = new BufferedImage(imageWidth,
				imageHeight, image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum -= (int) (0.2 * red[y][x] * filter[m][n]);
							greenSum -= (int) (0.2 * green[y][x] * filter[m][n]);
							blueSum -= (int) (0.2 * blue[y][x] * filter[m][n]);
						}
					}
				}
				redSum += red[i][j];
				greenSum += green[i][j];
				blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				laplacianBImage.setRGB(j, i, rgb);
			}
		}

		return laplacianBImage;
	}

	/**
	 * 对图像做拉普拉斯锐化法(C) 其filter mask为：0 -1 0 -1 4 -1 0 -1 0
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image laplacianFilterC(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };

		// 通过拉普拉斯锐化法（c）生成新图像
		BufferedImage laplacianCImage = new BufferedImage(imageWidth,
				imageHeight, image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += (int) (0.2 * red[y][x] * filter[m][n]);
							greenSum += (int) (0.2 * green[y][x] * filter[m][n]);
							blueSum += (int) (0.2 * blue[y][x] * filter[m][n]);
						}
					}
				}
				redSum += red[i][j];
				greenSum += green[i][j];
				blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				laplacianCImage.setRGB(j, i, rgb);
			}
		}

		return laplacianCImage;
	}

	/**
	 * 对图像做拉普拉斯锐化法(d) 其filter mask为：-1 -1 -1 -1 8 -1 -1 -1 -1
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image laplacianFilterD(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };

		// 通过拉普拉斯锐化法（d）生成新图像
		BufferedImage laplacianDImage = new BufferedImage(imageWidth,
				imageHeight, image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += (int) (0.2 * red[y][x] * filter[m][n]);
							greenSum += (int) (0.2 * green[y][x] * filter[m][n]);
							blueSum += (int) (0.2 * blue[y][x] * filter[m][n]);
						}
					}
				}
				redSum += red[i][j];
				greenSum += green[i][j];
				blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				laplacianDImage.setRGB(j, i, rgb);
			}
		}

		return laplacianDImage;
	}

	/**
	 * 对图像做索贝尔边缘检测(d) 其filter mask为：-1 -2 -1 0 0 0 1 2 1
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image sobelFilterD(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

		// 通过索贝尔边缘检测法（d）生成新图像
		BufferedImage sobelDImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += 0.2 * red[y][x] * filter[m][n];
							greenSum += 0.2 * green[y][x] * filter[m][n];
							blueSum += 0.2 * blue[y][x] * filter[m][n];
						}
					}
				}
				redSum += red[i][j];
				greenSum += green[i][j];
				blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				sobelDImage.setRGB(j, i, rgb);
			}
		}

		return sobelDImage;
	}

	/**
	 * 对图像做索贝尔边缘检测(e) 其filter mask为：-1 0 1 -2 0 2 -1 0 1
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image sobelFilterE(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

		// 通过索贝尔边缘检测法（e）生成新图像
		BufferedImage sobelEImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 3; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += red[y][x] * filter[m][n];
							greenSum += green[y][x] * filter[m][n];
							blueSum += blue[y][x] * filter[m][n];
						}
					}
				}
				// redSum += red[i][j];
				// greenSum += green[i][j];
				// blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				sobelEImage.setRGB(j, i, rgb);
			}
		}

		return sobelEImage;
	}

	/**
	 * 对图像做罗伯特梯度算子滤波(b) 其filter mask为：-1 0 0 1
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image robortFilterB(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { -1, 0 }, { 0, 1 } };

		// 通过罗伯特梯度算子滤波（b）生成新图像
		BufferedImage robortBImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 2; m++) {
					for (int n = 0; n < 2; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += red[y][x] * filter[m][n];
							greenSum += green[y][x] * filter[m][n];
							blueSum += blue[y][x] * filter[m][n];
						}
					}
				}
				// redSum += red[i][j];
				// greenSum += green[i][j];
				// blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				robortBImage.setRGB(j, i, rgb);
			}
		}

		return robortBImage;
	}

	/**
	 * 对图像做罗伯特梯度算子滤波(c) 其filter mask为： 0 -1 1 0
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image robortFilterC(BufferedImage image) {
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

		// 定义并初始化filter mask
		int[][] filter = { { 0, -1 }, { 1, 0 } };

		// 通过罗伯特梯度算子滤波（c）生成新图像
		BufferedImage robortCImage = new BufferedImage(imageWidth, imageHeight,
				image.getType());
		int redSum, greenSum, blueSum, rgb;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				redSum = greenSum = blueSum = 0;
				for (int m = 0; m < 2; m++) {
					for (int n = 0; n < 2; n++) {
						// Zero Padding
						int y = i + m - 1;
						int x = j + n - 1;
						if (y >= 0 && y < imageHeight && x >= 0
								&& x < imageWidth) {
							redSum += red[y][x] * filter[m][n];
							greenSum += green[y][x] * filter[m][n];
							blueSum += blue[y][x] * filter[m][n];
						}
					}
				}
				// redSum += red[i][j];
				// greenSum += green[i][j];
				// blueSum += blue[i][j];
				rgb = (redSum * 256 + greenSum) * 256 + blueSum;
				if (rgb > 8388608) {
					rgb -= 16777216;
				}

				robortCImage.setRGB(j, i, rgb);
			}
		}

		return robortCImage;
	}

}

