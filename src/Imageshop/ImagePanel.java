/*
 * 用于显示图像的panel
 * @author austin
 * 最后修改时间 2014.10.11
 */

package Imageshop;

import java.awt.*;

import javax.swing.*;


public class ImagePanel extends JPanel {
	private Image image;
	private int WIDTH;

	public ImagePanel(Image image, int WIDTH) {
		this.image = image;
		this.WIDTH = WIDTH;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (image == null) {
			return;
		}
		
		//获取图片大小
		int width = image.getWidth(this);
		int height = image.getHeight(this);
		int x = (WIDTH - width) / 2;
		//从窗口中央画出图片
		g.drawImage(image, x, 0, width, height,  this);  
		g = null;
	}
}