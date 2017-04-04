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

public class FrequencyDomainUtils {
	/**
	 * 对读入图片进行傅里叶变换
	 * 
	 * @param image
	 * @return
	 */
	public Image DFT(BufferedImage image) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 对于灰度图和二值图来说，只用对灰度进行处理
		// 对于真彩图来说，必须要分别对RGB三个通道进行处理
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
				|| image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			// 获取原图每个点的灰度值
			int[][] gray = new int[imageHeight][imageWidth];
			for (int i = image.getMinY(); i < imageHeight; i++) {
				for (int j = image.getMinX(); j < imageWidth; j++) {
					// 获取该点像素，并以object类型表示
					Object data = image.getRaster().getDataElements(j, i, null);

					// 取出某个像素的r值，对于灰度图来说，三者是一样的
					gray[i][j] = image.getColorModel().getRed(data);
				}
			}

			// 傅里叶变换后的矩阵
			Complex[][] complexArr = new Complex[imageHeight][imageWidth];
			// 使用DFT公式计算频域矩阵
			for (int u = 0; u < imageHeight; u++) {
				for (int v = 0; v < imageWidth; v++) {
					System.out.println("u: " + u + "; " + "v: " + v);

					// 初始化矩阵中元素
					complexArr[u][v] = new Complex(0, 0);

					for (int i = image.getMinY(); i < imageHeight; i++) {
						for (int j = image.getMinX(); j < imageWidth; j++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ imageHeight));
							tmp1.setIm(-Math.sin(2 * Math.PI * u * i
									/ imageHeight));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ imageWidth));
							tmp2.setIm(-Math.sin(2 * Math.PI * v * j
									/ imageWidth));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp1 = tmp1.times(gray[i][j] * Math.pow(-1, i + j));
							complexArr[u][v] = complexArr[u][v].plus(tmp1);
						}
					}
				}
			}

			// 定义取频谱后的矩阵
			double[][] spectrumArr = new double[imageHeight][imageWidth];
			double min = 9999, max = 0;
			// 计算每个元素的频谱
			for (int u = 0; u < imageHeight; u++) {
				for (int v = 0; v < imageWidth; v++) {
					spectrumArr[u][v] = complexArr[u][v].abs();
					min = spectrumArr[u][v] > min ? min : spectrumArr[u][v];
					max = spectrumArr[u][v] > max ? spectrumArr[u][v] : max;
				}
			}

			// 定义频域的图像并对每点赋值
			BufferedImage DFTImage = new BufferedImage(imageWidth, imageHeight,
					image.getType());
			double logSpec;
			int scaleSpec;
			max = Math.log10(max);
			min = Math.log10(min);
			for (int u = 0; u < imageHeight; u++) {
				for (int v = 0; v < imageWidth; v++) {
					// 为了更好的显示，对每个值取对数
					logSpec = Math.log10(spectrumArr[u][v]);

					// 对每点的值标定
					scaleSpec = (int) ((logSpec - min) * 255 / (max - min));
					System.out.println(scaleSpec);

					DFTImage.setRGB(v, u, new Color(scaleSpec, scaleSpec,
							scaleSpec).getRGB());
				}
			}

			return DFTImage;
		} else {
			// TO DO
			// 对真彩图做傅立叶变换
			return null;
		}
	}

	/**
	 * 对读入图片进行傅里叶逆变换
	 * 
	 * @param image
	 * @return
	 */
	public Image IDFT(BufferedImage image) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// 对于灰度图和二值图来说，只用对灰度进行处理
		// 对于真彩图来说，必须要分别对RGB三个通道进行处理
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
				|| image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			// 获取原图每个点的灰度值
			int[][] gray = new int[imageHeight][imageWidth];
			for (int i = image.getMinY(); i < imageHeight; i++) {
				for (int j = image.getMinX(); j < imageWidth; j++) {
					// 获取该点像素，并以object类型表示
					Object data = image.getRaster().getDataElements(j, i, null);

					// 取出某个像素的r值，对于灰度图来说，三者是一样的
					gray[i][j] = image.getColorModel().getRed(data);
				}
			}

			// 傅里叶变换后的矩阵
			Complex[][] complexArr = new Complex[imageHeight][imageWidth];
			// 使用DFT公式计算频域矩阵
			for (int u = 0; u < imageHeight; u++) {
				System.out.println("u: " + u + ";");
				for (int v = 0; v < imageWidth; v++) {
					// 初始化矩阵中元素
					complexArr[u][v] = new Complex(0, 0);

					for (int i = image.getMinY(); i < imageHeight; i++) {
						for (int j = image.getMinX(); j < imageWidth; j++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ imageHeight));
							tmp1.setIm(-Math.sin(2 * Math.PI * u * i
									/ imageHeight));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ imageWidth));
							tmp2.setIm(-Math.sin(2 * Math.PI * v * j
									/ imageWidth));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp1 = tmp1.times(gray[i][j]);
							complexArr[u][v] = complexArr[u][v].plus(tmp1);
						}
					}
				}
			}

			// 傅里叶逆变换后的矩阵
			Complex[][] complexArr2 = new Complex[imageHeight][imageWidth];
			// 使用IDFT公式计算频域矩阵
			for (int i = 0; i < imageHeight; i++) {
				System.out.println("i: " + i + ";");
				for (int j = 0; j < imageWidth; j++) {
					// 初始化矩阵中元素
					complexArr2[i][j] = new Complex(0, 0);

					for (int u = image.getMinY(); u < imageHeight; u++) {
						for (int v = image.getMinX(); v < imageWidth; v++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ imageHeight));
							tmp1.setIm(Math.sin(2 * Math.PI * u * i
									/ imageHeight));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ imageWidth));
							tmp2.setIm(Math.sin(2 * Math.PI * v * j
									/ imageWidth));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp1 = tmp1.times(complexArr[u][v]);
							complexArr2[i][j] = complexArr2[i][j].plus(tmp1);
						}
					}

					complexArr2[i][j] = complexArr2[i][j]
							.times(1.0 / (imageHeight * imageWidth));
				}
			}

			// 定义做傅立叶逆变换之后的图像
			BufferedImage IDFTImage = new BufferedImage(imageWidth,
					imageHeight, image.getType());
			int pixelGray;
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					pixelGray = (int) (complexArr2[i][j].re());
					IDFTImage.setRGB(j, i, new Color(pixelGray, pixelGray,
							pixelGray).getRGB());
				}
			}

			return IDFTImage;
		} else {
			// TO DO
			// 对真彩图做傅立叶逆变换
			return null;
		}
	}

	/**
	 * 对读入图片进行快速傅里叶变换
	 * 
	 * @param image
	 * @return
	 */
	public Image FFT(BufferedImage image) {
		return null;
	}

	/**
	 * 对读入图片进行快速傅里叶逆变换
	 * 
	 * @param image
	 * @return
	 */
	public Image IFFT(BufferedImage image) {
		return null;
	}

	/**
	 * 在频域对图像做均值滤波
	 * 
	 * @param image
	 * @return
	 */
	public Image averagingFilterFrequency(BufferedImage image) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int widthPadded = imageWidth + 11;
		int heightPadded = imageHeight + 11;

		// 对于灰度图和二值图来说，只用对灰度进行处理
		// 对于真彩图来说，必须要分别对RGB三个通道进行处理
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
				|| image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			// 获取原图每个点的灰度值，并作零延拓
			int[][] gray = new int[heightPadded][widthPadded];
			for (int i = image.getMinY(); i < heightPadded; i++) {
				for (int j = image.getMinX(); j < widthPadded; j++) {
					if (i < imageHeight && j < imageWidth) {
						// 获取该点像素，并以object类型表示
						Object data = image.getRaster().getDataElements(j, i,
								null);

						// 取出某个像素的r值，对于灰度图来说，三者是一样的
						gray[i][j] = image.getColorModel().getRed(data);
					} else {
						// 零延拓
						gray[i][j] = 0;
					}
				}
			}

			// 定义均值滤波，同时作零延拓
			double[][] filter = new double[heightPadded][widthPadded];
			for (int i = 0; i < heightPadded; i++) {
				for (int j = 0; j < widthPadded; j++) {
					if (i < 11 && j < 11) {
						filter[i][j] = 1.0 / 121;
					} else {
						// 零延拓
						filter[i][j] = 0;
					}
				}
			}

			// 傅里叶变换后的矩阵
			Complex[][] grayComplexArr = new Complex[heightPadded][widthPadded];
			Complex[][] filterComplexArr = new Complex[heightPadded][widthPadded];
			// 定义滤波后的矩阵
			Complex[][] complexArr = new Complex[heightPadded][widthPadded];
			// 使用DFT公式对原图和滤波作傅里叶变换，并将两个矩阵点乘运算
			for (int u = 0; u < heightPadded; u++) {
				System.out.println("u: " + u + ";");
				for (int v = 0; v < widthPadded; v++) {
					// 初始化矩阵中元素
					grayComplexArr[u][v] = new Complex(0, 0);
					filterComplexArr[u][v] = new Complex(0, 0);

					for (int i = image.getMinY(); i < heightPadded; i++) {
						for (int j = image.getMinX(); j < widthPadded; j++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex(), tmp3 = new Complex(), tmp4 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ heightPadded));
							tmp1.setIm(-Math.sin(2 * Math.PI * u * i
									/ heightPadded));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ widthPadded));
							tmp2.setIm(-Math.sin(2 * Math.PI * v * j
									/ widthPadded));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp3 = tmp1.times(gray[i][j]);
							tmp4 = tmp1.times(filter[i][j]);
							grayComplexArr[u][v] = grayComplexArr[u][v]
									.plus(tmp3);
							filterComplexArr[u][v] = filterComplexArr[u][v]
									.plus(tmp4);
						}
					}

					complexArr[u][v] = grayComplexArr[u][v]
							.times(filterComplexArr[u][v]);
				}
			}

			// 傅里叶逆变换后的矩阵
			Complex[][] complexArr2 = new Complex[imageHeight][imageWidth];
			// 使用IDFT公式计算频域矩阵
			for (int i = 0; i < imageHeight; i++) {
				System.out.println("i: " + i + ";");
				for (int j = 0; j < imageWidth; j++) {
					// 初始化矩阵中元素
					complexArr2[i][j] = new Complex(0, 0);

					for (int u = image.getMinY(); u < heightPadded; u++) {
						for (int v = image.getMinX(); v < widthPadded; v++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ heightPadded));
							tmp1.setIm(Math.sin(2 * Math.PI * u * i
									/ heightPadded));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ widthPadded));
							tmp2.setIm(Math.sin(2 * Math.PI * v * j
									/ widthPadded));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp1 = tmp1.times(complexArr[u][v]);
							complexArr2[i][j] = complexArr2[i][j].plus(tmp1);
						}
					}

					complexArr2[i][j] = complexArr2[i][j]
							.times(1.0 / (heightPadded * widthPadded));
				}
			}

			// 定义做傅立叶逆变换之后的图像
			BufferedImage IDFTImage = new BufferedImage(imageWidth,
					imageHeight, image.getType());
			int pixelGray;
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					pixelGray = (int) (complexArr2[i][j].re());
					// if (pixelGray < 0 || pixelGray > 255) {
					System.out.println(pixelGray);
					// }
					IDFTImage.setRGB(j, i, new Color(pixelGray, pixelGray,
							pixelGray).getRGB());
				}
			}

			return IDFTImage;
		} else {
			// TO DO
			// 对真彩图做频域的均值滤波
			return null;
		}
	}

	/**
	 * 在频域对图像做拉普拉斯滤波
	 * 
	 * @param image
	 * @return
	 */
	public Image laplacianFilterFrequency(BufferedImage image) {
		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int widthPadded = imageWidth + 3;
		int heightPadded = imageHeight + 3;

		// 对于灰度图和二值图来说，只用对灰度进行处理
		// 对于真彩图来说，必须要分别对RGB三个通道进行处理
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
				|| image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			// 获取原图每个点的灰度值，并作零延拓
			int[][] gray = new int[heightPadded][widthPadded];
			for (int i = image.getMinY(); i < heightPadded; i++) {
				for (int j = image.getMinX(); j < widthPadded; j++) {
					if (i < imageHeight && j < imageWidth) {
						// 获取该点像素，并以object类型表示
						Object data = image.getRaster().getDataElements(j, i,
								null);

						// 取出某个像素的r值，对于灰度图来说，三者是一样的
						gray[i][j] = image.getColorModel().getRed(data);
					} else {
						// 零延拓
						gray[i][j] = 0;
					}
				}
			}

			// 定义拉普拉斯滤波，同时作零延拓
			double[][] filter = new double[heightPadded][widthPadded];
			for (int i = 0; i < heightPadded; i++) {
				for (int j = 0; j < widthPadded; j++) {
					if (i < 3 && j < 3) {
						filter[i][j] = -1.0 / 9;
					} else {
						// 零延拓
						filter[i][j] = 0;
					}
				}
			}
			filter[1][1] = 8.0 / 9;

			// 傅里叶变换后的矩阵
			Complex[][] grayComplexArr = new Complex[heightPadded][widthPadded];
			Complex[][] filterComplexArr = new Complex[heightPadded][widthPadded];
			// 定义滤波后的矩阵
			Complex[][] complexArr = new Complex[heightPadded][widthPadded];
			// 使用DFT公式对原图和滤波作傅里叶变换，并将两个矩阵点乘运算
			for (int u = 0; u < heightPadded; u++) {
				System.out.println("u: " + u + ";");
				for (int v = 0; v < widthPadded; v++) {
					// 初始化矩阵中元素
					grayComplexArr[u][v] = new Complex(0, 0);
					filterComplexArr[u][v] = new Complex(0, 0);

					for (int i = image.getMinY(); i < heightPadded; i++) {
						for (int j = image.getMinX(); j < widthPadded; j++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex(), tmp3 = new Complex(), tmp4 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ heightPadded));
							tmp1.setIm(-Math.sin(2 * Math.PI * u * i
									/ heightPadded));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ widthPadded));
							tmp2.setIm(-Math.sin(2 * Math.PI * v * j
									/ widthPadded));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp3 = tmp1.times(gray[i][j]);
							tmp4 = tmp1.times(filter[i][j]);
							grayComplexArr[u][v] = grayComplexArr[u][v]
									.plus(tmp3);
							filterComplexArr[u][v] = filterComplexArr[u][v]
									.plus(tmp4);
						}
					}

					complexArr[u][v] = grayComplexArr[u][v]
							.times(filterComplexArr[u][v]);
				}
			}

			// 傅里叶逆变换后的矩阵
			Complex[][] complexArr2 = new Complex[imageHeight][imageWidth];
			// 使用IDFT公式计算频域矩阵
			for (int i = 0; i < imageHeight; i++) {
				System.out.println("i: " + i + ";");
				for (int j = 0; j < imageWidth; j++) {
					// 初始化矩阵中元素
					complexArr2[i][j] = new Complex(0, 0);

					for (int u = image.getMinY(); u < heightPadded; u++) {
						for (int v = image.getMinX(); v < widthPadded; v++) {
							// 定义两个相乘的复数
							Complex tmp1 = new Complex(), tmp2 = new Complex();

							// 给两个复数的实部、虚部赋值
							tmp1.setRe(Math.cos(2 * Math.PI * u * i
									/ heightPadded));
							tmp1.setIm(Math.sin(2 * Math.PI * u * i
									/ heightPadded));
							tmp2.setRe(Math.cos(2 * Math.PI * v * j
									/ widthPadded));
							tmp2.setIm(Math.sin(2 * Math.PI * v * j
									/ widthPadded));

							// 相乘并累加到矩阵元素中
							tmp1 = tmp1.times(tmp2);
							tmp1 = tmp1.times(complexArr[u][v]);
							complexArr2[i][j] = complexArr2[i][j].plus(tmp1);
						}
					}

					complexArr2[i][j] = complexArr2[i][j]
							.times(1.0 / (heightPadded * widthPadded));
				}
			}

			// 定义做傅立叶逆变换之后的图像
			BufferedImage IDFTImage = new BufferedImage(imageWidth,
					imageHeight, image.getType());
			int pixelGray;
			for (int i = 0; i < imageHeight; i++) {
				for (int j = 0; j < imageWidth; j++) {
					pixelGray = (int) (complexArr2[i][j].re() * 0.15 + gray[i][j]);
					if (pixelGray < 0) {
						pixelGray = 0;
					} else if (pixelGray > 255) {
						pixelGray = 255;
					}

					System.out.println(pixelGray);
					IDFTImage.setRGB(j, i, new Color(pixelGray, pixelGray,
							pixelGray).getRGB());
				}
			}

			return IDFTImage;
		} else {
			// TO DO
			// 对真彩图做频域的拉普拉斯滤波
			return null;
		}
	}
}

