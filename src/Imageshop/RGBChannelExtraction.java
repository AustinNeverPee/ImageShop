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

public class RGBChannelExtraction {
    public RGBChannelExtraction() {
    }

	/**
	 * Use red filter to create pictures only containing blue
	 */
	public Image showChanelB(Image arg0) {
		BlueFilter filter = new BlueFilter();
		Toolkit kit = Toolkit.getDefaultToolkit();
		return (kit.createImage(new FilteredImageSource(arg0.getSource(),
				filter)));
	}

	/**
	 * Use red filter to create pictures only containing green
	 */
	public Image showChanelG(Image arg0) {
		GreenFilter filter = new GreenFilter();
		Toolkit kit = Toolkit.getDefaultToolkit();
		return (kit.createImage(new FilteredImageSource(arg0.getSource(),
				filter)));
	}

	/**
	 * Use red filter to create pictures only containing red
	 */
	public Image showChanelR(Image arg0) {
		RedFilter filter = new RedFilter();
		Toolkit kit = Toolkit.getDefaultToolkit();
		return (kit.createImage(new FilteredImageSource(arg0.getSource(),
				filter)));
	}

	/**
	 * Turn RGB image to gray image
	 */
	public Image showGray(BufferedImage image) {
		int green = 0, red = 0, blue = 0, rgb;

		// 获取图像大小
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		BufferedImage grayImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		// 利用两个for循环来对像素矩阵进行操作
		for (int i = image.getMinX(); i < imageWidth; i++) {
			for (int j = image.getMinY(); j < imageHeight; j++) {
				// 获取该点像素，并以object类型表示
				Object data = image.getRaster().getDataElements(i, j, null);

				// 取出某个像素的r、g、b值
				red = image.getColorModel().getRed(data);
				blue = image.getColorModel().getBlue(data);
				green = image.getColorModel().getGreen(data);

				// 重新计算r、g、b值，计算公式为r=r*0.3+g*0.59+b*0.11，g=r, b=g
				red = (red * 3 + green * 6 + blue * 1) / 10;
				green = red;
				blue = green;

				// 将r、g、b再转化为rgb值
				// 因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb
				// rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
				rgb = (red * 256 + green) * 256 + blue;
				if (rgb > 8388608) {
					rgb = rgb - 16777216;
				}

				// 将rgb值写回图片
				grayImage.setRGB(i, j, rgb);
			}
		}

		return grayImage;
	}

	/**
	 * Filter all colors except for blue
	 */
	class BlueFilter extends RGBImageFilter {
		public BlueFilter() {
			canFilterIndexColorModel = true;
		}

		public int filterRGB(int x, int y, int rgb) {
			return (rgb & 0xff0000ff);
		}
	}

	/**
	 * Filter all colors except for green
	 */
	class GreenFilter extends RGBImageFilter {
		public GreenFilter() {
			canFilterIndexColorModel = true;
		}

		public int filterRGB(int x, int y, int rgb) {
			return (rgb & 0xff00ff00);
		}
	}

	/**
	 * Filter all colors except for red
	 */
	class RedFilter extends RGBImageFilter {
		public RedFilter() {
			canFilterIndexColorModel = true;
		}

		public int filterRGB(int x, int y, int rgb) {
			return (rgb & 0xffff0000);
		}
	}
}

