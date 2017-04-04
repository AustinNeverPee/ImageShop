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

public class ImageshopProcessor {
    private static final RGBChannelExtraction RGBProcessor 
        = new RGBChannelExtraction();
    private static final BWImageConversion BWProcessor
        = new BWImageConversion();
    private static final ImageScaling scaleProcessor
        = new ImageScaling();
    private static final HistogramUtils histProcessor
        = new HistogramUtils();
    private static final ImageQuantization quantizeProcessor
        = new ImageQuantization();
    private static final ImageFiltering filteringProcessor
        = new ImageFiltering();
    private static final NoiseUtils noiseGenerator
        = new NoiseUtils();
    private static final AdvancedDenoising adnoiseProcessor
        = new AdvancedDenoising();
    private static final FrequencyDomainUtils freqProcessor
        = new FrequencyDomainUtils();
    private static final ImageEnhancement enhProcessor
        = new ImageEnhancement();
    private static final HazeRemoval hazeProcessor
        = new HazeRemoval();
    private static final OtherUtils otherUtils
        = new OtherUtils();

	public ImageshopProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Use red filter to create pictures only containing blue
	 */
	public Image showChanelB(Image arg0) {
        return RGBProcessor.showChanelB(arg0);
	}

	/**
	 * Use red filter to create pictures only containing green
	 */
	public Image showChanelG(Image arg0) {
        return RGBProcessor.showChanelG(arg0);
	}

	/**
	 * Use red filter to create pictures only containing red
	 */
	public Image showChanelR(Image arg0) {
        return RGBProcessor.showChanelR(arg0);
	}

	/**
	 * Turn RGB image to gray image
	 */
	public Image showGray(BufferedImage image) {
        return RGBProcessor.showGray(image);
	}

	/**
	 * Turn RGB or gray image to B&W image
	 */
	public Image showBW(BufferedImage image) {
        return BWProcessor.showBW(image);
	}


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
        return scaleProcessor.scale(image, width, height);
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
		return scaleProcessor.scale2(image, width, height);
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
        return scaleProcessor.scale3(image, width, height);
	}

	/**
	 * 将图像转化为量化的灰度图
	 * 
	 * @param image
	 *            原图像
	 * @param level
	 *            灰度级数
	 * @return
	 */
	public Image quantize(BufferedImage image, int level) {
        return quantizeProcessor.quantize(image, level);
	}

	/**
	 * 读入两张图像，按照scale比例进行图片混合
	 * 
	 * @param imageFirst
	 * @param imageSecond
	 * @param scale
	 * @return
	 */
	public Image slowChange(BufferedImage imageFirst,
			BufferedImage imageSecond, int scale) {
        return quantizeProcessor.slowChange(imageFirst, imageSecond, scale);
	}

	/**
	 * 实例化ShowHistogram类，显示灰度直方图
	 * 
	 * @param image
	 */
	public void showHist(BufferedImage image) {
        histProcessor.showHist(image);
	}

	/**
	 * 对读入图像进行直方图均衡化 分别对R、G、B三个通道进行处理
	 * 
	 * @param image
	 * @return
	 */
	public Image histogramEqualization(BufferedImage image) {
        return histProcessor.histogramEqualization(image);
	}

	/**
	 * 按照HW4中要求对读入图像进行直方图均衡化 对R、G、B三个通道的直方图取平均，使用平均直方图对三个通道进行直方图均衡化
	 * 
	 * @param image
	 * @return
	 */
	public Image histogramEqualizationHW4(BufferedImage image) {
        return histProcessor.histogramEqualizationHW4(image);
	}

	/**
	 * 根据所给patch的大小，将图像灰度矩阵分成许多个patch， 随机返回8个patches
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public int[][][] view_as_window(BufferedImage image, int width, int height) {
        return otherUtils.view_as_window(image, width, height);
	}

	/**
	 * 对图像做平均值滤波
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image averagingFilter(BufferedImage image, int width, int height) {
        return filteringProcessor.averagingFilter(image, width, height);
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
        return filteringProcessor.medianFilter(image, width, height);
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
        return filteringProcessor.maxFilter(image, width, height);
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
        return filteringProcessor.minFilter(image, width, height);
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
        return filteringProcessor.laplacianFilterA(image);
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
        return filteringProcessor.laplacianFilterB(image);
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
        return filteringProcessor.laplacianFilterC(image);
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
        return filteringProcessor.laplacianFilterD(image);
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
        return filteringProcessor.sobelFilterD(image);
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
        return filteringProcessor.sobelFilterE(image);
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
        return filteringProcessor.robortFilterB(image);
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
        return filteringProcessor.robortFilterC(image);
	}

	/**
	 * 按照比例产生椒盐噪点
	 * 
	 * @param image
	 * @param percent
	 * @return
	 */
	public Image makeNoiseSP(BufferedImage image, double sp, double pp) {
		// 获取图像大小
        return noiseGenerator.makeNoiseSP(image, sp, pp);
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
        return noiseGenerator.makeNoiseGaussian(image, mean, variance);
	}

	/**
	 * 使用马尔可夫随机场对二值图像降噪 模拟退火算法
	 * 
	 * @param image
	 * @return MarkovImage
	 */
	public Image MarkovDenoise(BufferedImage image) {
        return adnoiseProcessor.MarkovDenoise(image);
	}

	/**
	 * 计算两张大小一致图片的相似度
	 * 
	 * @param imageFirst
	 * @return similarity
	 */
	public double computeSimilarity(BufferedImage imageFirst,
			BufferedImage imageSecond) {
        return otherUtils.computeSimilarity(imageFirst, imageSecond);
	}

	/**
	 * 对读入图片进行傅里叶变换
	 * 
	 * @param image
	 * @return
	 */
	public Image DFT(BufferedImage image) {
        return freqProcessor.DFT(image);
	}

	/**
	 * 对读入图片进行傅里叶逆变换
	 * 
	 * @param image
	 * @return
	 */
	public Image IDFT(BufferedImage image) {
        return freqProcessor.IDFT(image);
	}

	/**
	 * 对读入图片进行快速傅里叶变换
	 * 
	 * @param image
	 * @return
	 */
	public Image FFT(BufferedImage image) {
        return freqProcessor.FFT(image);
	}

	/**
	 * 对读入图片进行快速傅里叶逆变换
	 * 
	 * @param image
	 * @return
	 */
	public Image IFFT(BufferedImage image) {
        return freqProcessor.IFFT(image);
	}

	/**
	 * 在频域对图像做均值滤波
	 * 
	 * @param image
	 * @return
	 */
	public Image averagingFilterFrequency(BufferedImage image) {
        return freqProcessor.averagingFilterFrequency(image);
	}

	/**
	 * 在频域对图像做拉普拉斯滤波
	 * 
	 * @param image
	 * @return
	 */
	public Image laplacianFilterFrequency(BufferedImage image) {
        return freqProcessor.laplacianFilterFrequency(image);
	}

	/**
	 * 在空间域对图像做算数均值滤波 也就是之前的averagingFilter
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image ArithmeticMeanFilter(BufferedImage image, int width, int height) {
        return enhProcessor.ArithmeticMeanFilter(image, width, height);
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
        return enhProcessor.GeometricMeanFilter(image, width, height);
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
        return enhProcessor.HarmonicMeanFilter(image, width, height);
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
        return enhProcessor.ContraharmonicMeanFilter(image, width, height, Q);
	}

	/**
	 * 图像去雾 没有使用Soft Matting
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public Image WSMHazeRemoval(BufferedImage image, int width, int height) {
        return hazeProcessor.WSMHazeRemoval(image, width, height);
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
        return hazeProcessor.GIFHazeRemoval(image, width, height);
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
        return hazeProcessor.BTransmission(image, width, height);
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
        return hazeProcessor.GIFTransmission(image, width, height);
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

	/**
	 * 顺时针或逆时针旋转图像90°
	 * 
	 * @param image
	 * @param flag
	 * @return
	 */
	public Image Rotate(BufferedImage image, int flag) {
        return otherUtils.Rotate(image, flag);
	}
	
	/**
	 * 水平或竖直翻转图像
	 * 
	 * @param image
	 * @param flag
	 * @return
	 */
	public Image Flip(BufferedImage image, int flag) {
        return otherUtils.Flip(image, flag);
	}

}
